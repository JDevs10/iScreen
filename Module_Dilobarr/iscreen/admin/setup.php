<?php
/* Copyright (C) 2004-2017 Laurent Destailleur  <eldy@users.sourceforge.net>
 * Copyright (C) 2019 SuperAdmin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * \file    iscreen/admin/setup.php
 * \ingroup iscreen
 * \brief   iscreen setup page.
 */

// Load Dolibarr environment
$res=0;
// Try main.inc.php into web root known defined into CONTEXT_DOCUMENT_ROOT (not always defined)
if (! $res && ! empty($_SERVER["CONTEXT_DOCUMENT_ROOT"])) $res=@include($_SERVER["CONTEXT_DOCUMENT_ROOT"]."/main.inc.php");
// Try main.inc.php into web root detected using web root calculated from SCRIPT_FILENAME
$tmp=empty($_SERVER['SCRIPT_FILENAME'])?'':$_SERVER['SCRIPT_FILENAME'];$tmp2=realpath(__FILE__); $i=strlen($tmp)-1; $j=strlen($tmp2)-1;
while($i > 0 && $j > 0 && isset($tmp[$i]) && isset($tmp2[$j]) && $tmp[$i]==$tmp2[$j]) { $i--; $j--; }
if (! $res && $i > 0 && file_exists(substr($tmp, 0, ($i+1))."/main.inc.php")) $res=@include(substr($tmp, 0, ($i+1))."/main.inc.php");
if (! $res && $i > 0 && file_exists(dirname(substr($tmp, 0, ($i+1)))."/main.inc.php")) $res=@include(dirname(substr($tmp, 0, ($i+1)))."/main.inc.php");
// Try main.inc.php using relative path
if (! $res && file_exists("../../main.inc.php")) $res=@include("../../main.inc.php");
if (! $res && file_exists("../../../main.inc.php")) $res=@include("../../../main.inc.php");
if (! $res) die("Include of main fails");

global $langs, $user;

// Libraries
require_once DOL_DOCUMENT_ROOT . "/core/lib/admin.lib.php";
require_once '../lib/iscreen.lib.php';
//require_once "../class/myclass.class.php";

// Translations
$langs->loadLangs(array("admin", "iscreen@iscreen"));

// Access control
if (! $user->admin) accessforbidden();

// Parameters
$action = GETPOST('action', 'alpha');
$backtopage = GETPOST('backtopage', 'alpha');

$arrayofparameters=array(
	//'ISCREEN_MYPARAM1'=>array('css'=>'minwidth200','enabled'=>1),
	'Présenter les produits aléatoires' => array('css' => 'minwidth10', 'enabled'=>1),
	'Présenter les dernières produits' => array('css' => 'minwidth10', 'enabled'=>1),
	'Présenter les produits de les categorie'=> array('css' => 'minwidth10', 'enabled'=>1),
	'Présenter les produits de promotion'=> array('css' => 'minwidth10', 'enabled'=>1)
);

//Set form Values
// Get the current config in the database
$sql = "SELECT * FROM llx_iscreen_iscreenobject where rowid = 1";
$res = $db->query($sql);

if ($res->num_rows > 0) {
	$row = $db->fetch_array($sql);

	$p_aleatoir_iscreen = $row['p_aleatoir'];
	$a_category_iscreen = $row['a_category'];
	$category_x_iscreen = $row['category_x'];
	$p_recente_iscreen = $row['p_recente'];

}else{
	
	$p_aleatoir_iscreen = 'Nothing';
	$a_category_iscreen = 'Nothing';
	$category_x_iscreen = 'Nothing';
	$p_recente_iscreen = 'Nothing';
}

$categorieList = array();
$sql1 = "SELECT rowid, label FROM llx_categorie";
$res1 = $db->query($sql1);

$categorieList[] = array('rowid' => -1, 'label' => "Selectionner");
while ($data = $db->fetch_array($res1)) {

	$categorieList[] = $data;
}
//print("<pre>".print_r($categorieList,true)."</pre>");

//print("p_aleatoir_iscreen: ".$p_aleatoir_iscreen." || p_recente_iscreen: ".$p_recente_iscreen);

/*
 * Actions
 */
if ((float) DOL_VERSION >= 6)
{
	include DOL_DOCUMENT_ROOT.'/core/actions_setmoduleoptions.inc.php';
}

if($action == 'update_iscreen_info')
{
	//print("<pre>".print_r($_POST,true)."</pre>");
	$p_aleatoir_iscreen = $_POST['p_aleatoir_iscreen'];
	$a_category_iscreen = $_POST['a_category_iscreen'];
	$category_x_iscreen = $_POST['category_x_iscreen'];
	$p_recente_iscreen = $_POST['p_recente_iscreen'];

	# || strlen($category_x_iscreen) > 1

	if (strlen($p_aleatoir_iscreen) > 1 || strlen($a_category_iscreen) > 1 || strlen($p_recente_iscreen) > 1) {
		?>
		<script>
			alert('Un ou plusieur valeur(s) dans les champs ne sont pas respecté !\nVeuillez les valeurs 0 ou 1.');
		</script>
		<?php
		print_r("category_x_iscreen: ".$category_x_iscreen);
	}else{
	
		if($p_aleatoir_iscreen>0)
		{
			$p_aleatoir_iscreen = 1;
		}
		if ($a_category_iscreen>0)
		{
			$a_category_iscreen = 1;
		}
		if($p_recente_iscreen>0)
		{
			$p_recente_iscreen = 1;
		}
		
		print_r("res->num_rows: ".$res->num_rows);
		if ($res->num_rows == 0) {
			
			//If first insert or no values in Database
			$sql = "INSERT INTO llx_iscreen_iscreenobject (rowid,p_aleatoir,a_category,category_x,p_recente) VALUES(1,'".$p_aleatoir_iscreen."','".$a_category_iscreen."','".$category_x_iscreen."','".$p_recente_iscreen."')";
			$db->query($sql);

		}else{
			//Update values in Database
			$sql = "UPDATE llx_iscreen_iscreenobject SET p_aleatoir='".$p_aleatoir_iscreen."' WHERE rowid = 1";
			$res = $db->query($sql);
			$sql = "UPDATE llx_iscreen_iscreenobject SET a_category='".$a_category_iscreen."' WHERE rowid = 1";
			$res = $db->query($sql);
			$sql = "UPDATE llx_iscreen_iscreenobject SET category_x='".$category_x_iscreen."' WHERE rowid = 1";
			$res = $db->query($sql);
			$sql = "UPDATE llx_iscreen_iscreenobject SET p_recente='".$p_recente_iscreen."' WHERE rowid = 1";
			$res = $db->query($sql);

		}
		
		$db->commit();
		header("location:setup.php");
	}
	
}


/*
 * View
 */

$page_name = "iscreenSetup";
llxHeader('', $langs->trans($page_name));

// Subheader
$linkback = '<a href="'.($backtopage?$backtopage:DOL_URL_ROOT.'/admin/modules.php?restore_lastsearch_values=1').'">'.$langs->trans("BackToModuleList").'</a>';

print load_fiche_titre($langs->trans($page_name), $linkback, 'object_iscreen@iscreen');

// Configuration header
$head = iscreenAdminPrepareHead();
dol_fiche_head($head, 'settings', '', -1, "iscreen@iscreen");

// Setup page goes here
//echo $langs->trans("iscreenSetupPage");

?>
<h3>Setup : Configure iScreen App here. (0 = deactivate | 1 = activate)</h3>

<form id="form_iscreen" name="form_iscreen" method="POST" action="setup.php">
	
	<input type="hidden" name="action" value="update_iscreen_info">
	<h3>Présenter les produits aléatoires : </h3>
	<input type="text" name="p_aleatoir_iscreen" maxlength="1" value="<?php print $p_aleatoir_iscreen ?>"/>

	<h3>Activer la publicitée des produits aléatoir de chaque categorie. : </h3>
	<input type="text" name="a_category_iscreen" maxlength="1" value="<?php print $a_category_iscreen ?>"/>

	<h3>Présenter les produits de la categorie : </h3>
	<select name="category_x_iscreen">
		<?php
		foreach ($categorieList as $key => $value) {
			
			# Set selected category list from db config
			if ($value['rowid'] == $category_x_iscreen) {
				?>
				<option value="<?php print $value['rowid'] ?>" selected="true" > <?php print $value['label'] ?></option>
				<?php
			}
			?>
			<option value="<?php print $value['rowid'] ?>"> <?php print $value['label'] ?></option>
		<?php
		}
		?>
	</select>


	<h3>Présenter les dernières produits : </h3><input type="text" name="p_recente_iscreen" maxlength="1" value="<?php print $p_recente_iscreen ?>"/>
	<br>
	<br>
	<br>
	<br>
	<div id="maj_const" onclick="document.getElementById('form_iscreen').submit()">Update</div>

</form>
<style>
#maj_const{
	border: 1px solid black;
	width: 100px;
	text-align: center;
	padding: 7px 0px;
	font-size: 15px;
	font-weight: bold;
	background: cornflowerblue;
	text-shadow: 1px 1px 1px black;
	color: white;
	cursor: pointer;
	transition:0.2s ease;
}
#maj_const:hover{
	opacity:0.8;
}
 </style>

<?php


// Page end
dol_fiche_end();

llxFooter();
$db->close();
