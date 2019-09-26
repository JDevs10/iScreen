<?php
/* Copyright (C) 2015   Jean-François Ferry     <jfefe@aternatik.fr>
 * Copyright (C) 2019 SuperAdmin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

use Luracast\Restler\RestException;

dol_include_once('/iscreen/class/iscreenobject.class.php');



/**
 * \file    iscreen/class/api_iscreen.class.php
 * \ingroup iscreen
 * \brief   File for API management of iscreenobject.
 */

/**
 * API class for iscreen iscreenobject
 *
 * @smart-auto-routing false
 * @access protected
 * @class  DolibarrApiAccess {@requires user,external}
 */
class iscreenApi extends DolibarrApi
{
    /**
     * @var array   $FIELDS     Mandatory fields, checked when create and update object
     */
    static $FIELDS = array(
        'name'
    );


    /**
     * @var iscreenObject $iscreenobject {@type iscreenObject}
     */
    public $iscreenobject;

    /**
     * Constructor
     *
     * @url     GET /
     *
     */
    function __construct()
    {
		global $db, $conf;
		$this->db = $db;
        $this->iscreenobject = new iscreenObject($this->db);
    }

    /**
     * Get properties of a iscreenobject object
     *
     * Return an array with iscreenobject informations
     *
     * @param 	int 	$id ID of iscreenobject
     * @return 	array|mixed data without useless information
	 *
     * @url	GET getConfig/{id}
     * @throws 	RestException
     */
    function get($id)
    {
		global $db, $conf;

        $sql = "SELECT * FROM ".MAIN_DB_PREFIX."iscreen_iscreenobject WHERE rowid = ".$id;

        $categorieList = array();
        $res = $db->query($sql);
        
        $data = $db->fetch_array($res);

        $arraySize = (count($data) / 2);
        for ($i=0; $i < $arraySize; $i++) { 
            # code...
            unset($data[$i]);
        }
        
        //print("<pre>".print_r($data,true)."</pre>");
        //print("<pre>".print_r(json_encode($data),true)."</pre>");

        return $data;
    }


    /**
     * List iscreenobjects
     *
     * Get a list of iScreen Configurations
     *
     * @param string	       $sortfield	        Sort field
     * @param string	       $sortorder	        Sort order
     * @param int		       $limit		        Limit for list
     * @param int		       $page		        Page number
     * @param string           $sqlfilters          Other criteria to filter answers separated by a comma. Syntax example "(t.ref:like:'SO-%') and (t.date_creation:<:'20160101')"
     * @return  array                               Array of order objects
     *
     * @throws RestException
     *
     * @url	GET /iscreenobjects/
     */
    /*
    function index($sortfield = "t.rowid", $sortorder = 'ASC', $limit = 100, $page = 0, $sqlfilters = '') {
        global $db, $conf;

        $obj_ret = array();

        $socid = DolibarrApiAccess::$user->societe_id ? DolibarrApiAccess::$user->societe_id : '';

        $restictonsocid = 0;	// Set to 1 if there is a field socid in table of object

        // If the internal user must only see his customers, force searching by him
        if ($restictonsocid && ! DolibarrApiAccess::$user->rights->societe->client->voir && !$socid) $search_sale = DolibarrApiAccess::$user->id;

        $sql = "SELECT t.rowid";
        if ($restictonsocid && (!DolibarrApiAccess::$user->rights->societe->client->voir && !$socid) || $search_sale > 0) $sql .= ", sc.fk_soc, sc.fk_user"; // We need these fields in order to filter by sale (including the case where the user can only see his prospects)
        $sql.= " FROM ".MAIN_DB_PREFIX."iscreenobject_mytable as t";

        if ($restictonsocid && (!DolibarrApiAccess::$user->rights->societe->client->voir && !$socid) || $search_sale > 0) $sql.= ", ".MAIN_DB_PREFIX."societe_commerciaux as sc"; // We need this table joined to the select in order to filter by sale
        $sql.= " WHERE 1 = 1";

		// Example of use $mode
        //if ($mode == 1) $sql.= " AND s.client IN (1, 3)";
        //if ($mode == 2) $sql.= " AND s.client IN (2, 3)";

        $tmpobject = new iscreenObject($db);
        if ($tmpobject->ismultientitymanaged) $sql.= ' AND t.entity IN ('.getEntity('iscreenobject').')';
        if ($restictonsocid && (!DolibarrApiAccess::$user->rights->societe->client->voir && !$socid) || $search_sale > 0) $sql.= " AND t.fk_soc = sc.fk_soc";
        if ($restictonsocid && $socid) $sql.= " AND t.fk_soc = ".$socid;
        if ($restictonsocid && $search_sale > 0) $sql.= " AND t.rowid = sc.fk_soc";		// Join for the needed table to filter by sale
        // Insert sale filter
        if ($restictonsocid && $search_sale > 0)
        {
            $sql .= " AND sc.fk_user = ".$search_sale;
        }
        if ($sqlfilters)
        {
            if (! DolibarrApi::_checkFilters($sqlfilters))
            {
                throw new RestException(503, 'Error when validating parameter sqlfilters '.$sqlfilters);
            }
	        $regexstring='\(([^:\'\(\)]+:[^:\'\(\)]+:[^:\(\)]+)\)';
            $sql.=" AND (".preg_replace_callback('/'.$regexstring.'/', 'DolibarrApi::_forge_criteria_callback', $sqlfilters).")";
        }

        $sql.= $db->order($sortfield, $sortorder);
        if ($limit)	{
            if ($page < 0)
            {
                $page = 0;
            }
            $offset = $limit * $page;

            $sql.= $db->plimit($limit + 1, $offset);
        }

        $result = $db->query($sql);
        if ($result)
        {
            $num = $db->num_rows($result);
            while ($i < $num)
            {
                $obj = $db->fetch_object($result);
                $iscreenobject_static = new iscreenObject($db);
                if($iscreenobject_static->fetch($obj->rowid)) {
                    $obj_ret[] = $this->_cleanObjectDatas($iscreenobject_static);
                }
                $i++;
            }
        }
        else {
            throw new RestException(503, 'Error when retrieve iscreenobject list');
        }
        if( ! count($obj_ret)) {
            throw new RestException(404, 'No iscreenobject found');
        }
		return $obj_ret;
    }
*/
    /**
     * Create iscreenobject object. Setup : Configure iScreen App here. (0 = deactivate | 1 = activate)
     *
     * @param string           $p_aleatoir_iscreen           Produits aléatoires
     * @param string           $a_category_iscreen           Produits aléatoires de la categorie
     * @param string           $category_x_iscreen           Produits de la categorie
     * @param string           $p_recente_iscreen            Dernières produits
     * 
     * @return int             ID of iscreenobject
     *
     * @url	POST createConfig/{p_aleatoir_iscreen}/{a_category_iscreen}/{category_x_iscreen}/{p_recente_iscreen}
     */
    function post($p_aleatoir_iscreen = "1", $a_category_iscreen = "-1", $category_x_iscreen = "-1", $p_recente_iscreen = "0")
    {
        global $db, $conf;

        $sql = "INSERT INTO ".MAIN_DB_PREFIX."iscreen_iscreenobject (rowid,p_aleatoir,a_category,category_x,p_recente) 
                VALUES(1,'".$p_aleatoir_iscreen."','".$a_category_iscreen."','".$category_x_iscreen."','".$p_recente_iscreen."')";

        
        //var_dump($db->query($sql));
        if ($db->query($sql)) {
            # Querry executed with success
            return 1;
        }else{
            throw new RestException(400, "iScreen Configurations exist, try updating the Configurations!");
        }

    }

    /**
     * Update iscreenobject object. Setup : Configure iScreen App here. (0 = deactivate | 1 = activate)
     *
     * @param int              $rowid                        Id
     * @param string           $p_aleatoir_iscreen           Produits aléatoires 0||1
     * @param string           $a_category_iscreen           Produits aléatoires de la categorie 0||1
     * @param string           $category_x_iscreen           Id categorie
     * @param string           $p_recente_iscreen            Dernières produits 0||1
     * 
     * @return int             ID of iscreenobject
     *
     * @url	PUT updateConfig/{rowid}/{p_aleatoir_iscreen}/{a_category_iscreen}/{category_x_iscreen}/{p_recente_iscreen}
     */
    function put($rowid, $p_aleatoir_iscreen, $a_category_iscreen, $category_x_iscreen, $p_recente_iscreen)
    {
        global $db, $conf;

        $sql_1 = "UPDATE ".MAIN_DB_PREFIX."iscreen_iscreenobject SET p_aleatoir='".$p_aleatoir_iscreen."' WHERE rowid = ".$rowid;
        $sql_2 = "UPDATE ".MAIN_DB_PREFIX."iscreen_iscreenobject SET a_category='".$a_category_iscreen."' WHERE rowid = ".$rowid;
        $sql_3 = "UPDATE ".MAIN_DB_PREFIX."iscreen_iscreenobject SET category_x='".$category_x_iscreen."' WHERE rowid = ".$rowid;
        $sql_4 = "UPDATE ".MAIN_DB_PREFIX."iscreen_iscreenobject SET p_recente='".$p_recente_iscreen."' WHERE rowid = ".$rowid;
        
        //var_dump($db->query($sql));
        if ($db->query($sql_1) && $db->query($sql_2) && $db->query($sql_3) && $db->query($sql_4)) {
            # Querry executed with success
            return 1;
        }else{
            throw new RestException(400);
        }
    }

    /**
     * Delete iscreenobject
     *
     * @param   int     $rowid   iscreenObject ID
     * @return  array
     *
     * @url	DELETE deleteConfig/{rowid}
     */
    function delete($rowid)
    {
    	global $db, $conf;

        $sql = "DELETE FROM ".MAIN_DB_PREFIX."iscreen_iscreenobject WHERE rowid = ".$rowid;
        
        //var_dump($db->query($sql));
        if ($db->query($sql)) {
            # Querry executed with success
            return 1;
        }else{
            throw new RestException(400);
        }

    }


    /**
     * Clean sensible object datas
     *
     * @param   object  $object    Object to clean
     * @return    array    Array of cleaned object properties
     */
    function _cleanObjectDatas($object)
    {
    	$object = parent::_cleanObjectDatas($object);

    	/*unset($object->note);
    	unset($object->address);
    	unset($object->barcode_type);
    	unset($object->barcode_type_code);
    	unset($object->barcode_type_label);
    	unset($object->barcode_type_coder);*/

    	return $object;
    }

    /**
     * Validate fields before create or update object
     *
     * @param array $data   Data to validate
     * @return array
     *
     * @throws RestException
     */
    function _validate($data)
    {
        $iscreenobject = array();
        foreach (iscreenObjectApi::$FIELDS as $field) {
            if (!isset($data[$field]))
                throw new RestException(400, "$field field missing");
            $iscreenobject[$field] = $data[$field];
        }
        return $iscreenobject;
    }
}
