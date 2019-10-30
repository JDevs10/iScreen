package com.example.iscreen.pages.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.adapter.ProductVirtualAdapter;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.FindProductVirtualListener;
import com.example.iscreen.interfaces.ProductVirtualAdapterListener;
import com.example.iscreen.remote.model.ProductVirtual;
import com.example.iscreen.remote.rest.FindProductVirtualREST;
import com.example.iscreen.task.FindProductVirtualTask;
import com.example.iscreen.utility.IScreenUtility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailProduct extends AppCompatActivity implements FindProductVirtualListener, ProductVirtualAdapterListener {
    private final String TAG = DetailProduct.class.getSimpleName();

    private ProduitEntry selectedProduct;

    private List<ProductVirtual> productVirtualList;
    private ProductVirtualAdapter productVirtualAdapter;
    private ProductVirtual mProductVirtual;

    private ImageView mPosterIV;
    private RecyclerView mVirtualProductRecycle;
    private TextView mRefTV, mProductName, mUnitPrice, mPriceHT, mPriceTTC, mVirtualProductStatus, mUnitStock, mUnitStockType, mTvaTV, mDescription, mNotes;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        //Check / Set Full Screen Mode
        new IScreenUtility().fullScreenMode(this, this);

        db = AppDatabase.getInstance(this);
        if (getIntent().getExtras().getString("ref_produit") != null){
            selectedProduct = db.productDao().getProductByRef(getIntent().getExtras().getString("ref_produit"));
            Log.e(TAG, "onCreate: " + selectedProduct.getRef() +
                    " produitID=" + selectedProduct.getId() +
                    " En Stock= "+ selectedProduct.getStock_reel() +
                    " description=" + selectedProduct.getDescription() +
                    " produitID=" + selectedProduct.getFile_content());
        }

        executeFindproductVirtual();

        mPosterIV = (ImageView) findViewById(R.id.detail_product_image_img);
        mVirtualProductRecycle = (RecyclerView) findViewById(R.id.detail_product_virtualProducts_rv);
        mRefTV = (TextView) findViewById(R.id.detail_product_ref_tv);
        mProductName = (TextView) findViewById(R.id.detail_product_name_tv);
        mUnitPrice = (TextView) findViewById(R.id.detail_product_prix_unitaire_tv);
        mPriceHT = (TextView) findViewById(R.id.detail_product_prixDeVente_ht_tv);
        mPriceTTC = (TextView) findViewById(R.id.detail_product_prixDeVente_ttc_tv);
        mVirtualProductStatus = (TextView) findViewById(R.id.detail_product_PV_statut);
        mUnitStock = (TextView) findViewById(R.id.detail_product_uniteEnStock_tv);
        mUnitStockType = (TextView) findViewById(R.id.detail_product_uniteEnStock_type_tv);
        mTvaTV = (TextView) findViewById(R.id.detail_product_tva);
        mDescription = (TextView) findViewById(R.id.detail_product_description_tv);
        mNotes = (TextView) findViewById(R.id.detail_product_note_tv);

        // fix image in view
        mPosterIV.setAdjustViewBounds(true);
        mPosterIV.setScaleType(ImageView.ScaleType.FIT_XY);

        // set virtual product
        productVirtualList = new ArrayList<>();

        productVirtualAdapter = new ProductVirtualAdapter(DetailProduct.this, productVirtualList, DetailProduct.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailProduct.this, LinearLayoutManager.HORIZONTAL, false);
        mVirtualProductRecycle.setLayoutManager(mLayoutManager);
        mVirtualProductRecycle.setItemAnimator(new DefaultItemAnimator());
        mVirtualProductRecycle.setAdapter(productVirtualAdapter);


        if (savedInstanceState != null) {
            selectedProduct = getIntent().getExtras().getParcelable("ref_produit");
        }

        initValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void executeFindproductVirtual() {
        FindProductVirtualTask task = new FindProductVirtualTask(DetailProduct.this, selectedProduct.getId(), DetailProduct.this);
        task.execute();
    }

    @Override
    public void onFindProductVirtualCompleted(FindProductVirtualREST findProductVirtualREST) {
        if (findProductVirtualREST != null && findProductVirtualREST.getProductVirtuals() != null) {
            if (findProductVirtualREST.getProductVirtuals().size() > 0) {
//                Log.e(TAG, "onFindProductVirtualCompleted: size="+findProductVirtualREST.getProductVirtuals().size()+
//                        " product_parent_id="+findProductVirtualREST.getProduct_parent_id());

                double price0 = Double.parseDouble(selectedProduct.getPrice()) * Integer.parseInt(findProductVirtualREST.getProductVirtuals().get(0).getQty());
                double priceTTC0 = Double.parseDouble(selectedProduct.getPrice_ttc()) * Integer.parseInt(findProductVirtualREST.getProductVirtuals().get(0).getQty());
                findProductVirtualREST.getProductVirtuals().get(0).setPrice("" + price0);
                findProductVirtualREST.getProductVirtuals().get(0).setPrice_ttc("" + priceTTC0);

                for (int i = 1; i < findProductVirtualREST.getProductVirtuals().size(); i++) {
                    double price = Double.parseDouble(findProductVirtualREST.getProductVirtuals().get(i - 1).getPrice()) * Integer.parseInt(findProductVirtualREST.getProductVirtuals().get(i).getQty());
                    double priceTTC = Double.parseDouble(findProductVirtualREST.getProductVirtuals().get(i - 1).getPrice_ttc()) * Integer.parseInt(findProductVirtualREST.getProductVirtuals().get(i).getQty());
                    findProductVirtualREST.getProductVirtuals().get(i).setPrice("" + price);
                    findProductVirtualREST.getProductVirtuals().get(i).setPrice_ttc("" + priceTTC);
                }

                productVirtualList.addAll(findProductVirtualREST.getProductVirtuals());

                productVirtualAdapter.notifyDataSetChanged();

                int activePos = productVirtualList.size() >= 1 ? 1 : 0;

                updateProductValues(productVirtualList.get(activePos));
            }
        }
    }

    @Override
    public void onProductVirtualClicked(ProductVirtual productVirtual, int position) {
        Log.e(TAG, "onProductVirtualClicked: position=" + position +
                " label=" + productVirtual.getLabel() +
                " price=" + productVirtual.getPrice() +
                " Qty=" + productVirtual.getQty());

        if (!mProductVirtual.getRowid().equals(productVirtual.getRowid())) {
            updateProductValues(productVirtual);
        }
    }

    private void updateProductValues(ProductVirtual productVirtual) {
        mProductVirtual = productVirtual;

        Log.e(TAG, "updateProductValues: qty=" + mProductVirtual.getQty());

        mPriceHT.setText(String.format("%s ", IScreenUtility.amountFormat2(mProductVirtual.getPrice())));
        mPriceTTC.setText(String.format("%s ", IScreenUtility.amountFormat2(mProductVirtual.getPrice_ttc())));
        mUnitStock.setText(String.format("%s", mProductVirtual.getStock()));
        mTvaTV.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getTva_tx())));


        String[] label = mProductVirtual.getLabel().split(" ");

        mProductName.setText(selectedProduct.getLabel());
        mRefTV.setText(selectedProduct.getRef());
        mUnitPrice.setText(String.format("%s",IScreenUtility.amountFormat2(selectedProduct.getPrice())));
        mPriceHT.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getPrice())));
        mPriceTTC.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getPrice_ttc())));
        mVirtualProductStatus.setText(String .format(" / %s", label[label.length - 1]));
        mUnitStock.setText(String.format("%s", mProductVirtual.getStock()));
        mUnitStockType.setText(String .format(" %s(S)", label[label.length - 1]));
        mTvaTV.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getTva_tx())));
        mDescription.setText(IScreenUtility.getDescProduit(selectedProduct.getDescription()));
        mNotes.setText(mProductVirtual.getNote());

        /*
        String prixU = mPriceUnitaireET.getText().toString().replace(",", ".");
        prixU = prixU.equals("") ? mProduitParcelable.getPrice() : prixU;
        double price = Double.parseDouble(prixU) * Integer.parseInt(mProductVirtual.getQty());

        mPriceET.setText(IScreenUtility.roundOffTo2DecPlaces("" + mProductVirtual.getPrice()));

        String[] label = mProductVirtual.getLabel().split(" ");

        mPriceNature.setText(String.format("/ %s", label[label.length - 1]));
        mQuantiteNature.setText(String.format("%s(S)", label[label.length - 1]));
        */
    }

    public void initValues() {
        mProductVirtual = new ProductVirtual();
        mProductVirtual.setRowid("" + selectedProduct.getId());
        mProductVirtual.setFk_product_fils("" + selectedProduct.getId());
        mProductVirtual.setFk_product_pere("" + selectedProduct.getId());
        mProductVirtual.setQty("1");
        mProductVirtual.setRef(selectedProduct.getRef());
        mProductVirtual.setDatec(selectedProduct.getDate_creation());
        mProductVirtual.setLabel(selectedProduct.getLabel() + " UNITÉ");
        mProductVirtual.setDescription(selectedProduct.getDescription());
        mProductVirtual.setNote_public(selectedProduct.getNote_public());
        mProductVirtual.setNote(selectedProduct.getNote());
        mProductVirtual.setPrice(selectedProduct.getPrice());
        mProductVirtual.setPrice_ttc(selectedProduct.getPrice_ttc());
        mProductVirtual.setPrice_min(selectedProduct.getPrice_min());
        mProductVirtual.setPrice_min_ttc(selectedProduct.getPrice_min_ttc());
        mProductVirtual.setPrice_base_type(selectedProduct.getPrice_base_type());
        mProductVirtual.setTva_tx(selectedProduct.getTva_tx());
        mProductVirtual.setLocal_poster_path(selectedProduct.getFile_content());
        mProductVirtual.setSeuil_stock_alerte(selectedProduct.getSeuil_stock_alerte());
        mProductVirtual.setStock("" + selectedProduct.getStock_reel());
        productVirtualList.add(mProductVirtual);

        productVirtualAdapter.notifyDataSetChanged();

        String[] label = mProductVirtual.getLabel().split(" ");

        mProductName.setText(selectedProduct.getLabel());
        mRefTV.setText(selectedProduct.getRef());
        mUnitPrice.setText(String.format("%s",IScreenUtility.amountFormat2(selectedProduct.getPrice())));
        mPriceHT.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getPrice())));
        mPriceTTC.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getPrice_ttc())));
        mVirtualProductStatus.setText(String .format(" / %s", label[label.length - 1]));
        mUnitStock.setText(String.format("%s", mProductVirtual.getStock()));
        mUnitStockType.setText(String .format(" %s(S)", label[label.length - 1]));
        mTvaTV.setText(String.format("%s", IScreenUtility.amountFormat2(mProductVirtual.getTva_tx())));
        mDescription.setText(IScreenUtility.getDescProduit(selectedProduct.getDescription()));
        mNotes.setText(mProductVirtual.getNote());

        /*
        double price = Double.parseDouble(selectedProduct.getPrice()) * Integer.parseInt(mProductVirtual.getQty());
        mPriceET.setText(ISalesUtility.roundOffTo2DecPlaces("" + price));

        String[] label = mProductVirtual.getLabel().split(" ");

        mPriceNature.setText(String.format("/ %s", label[label.length - 1]));
        mQuantiteNature.setText(String.format("%s(S)", label[label.length - 1]));

        mQuantiteNumberBtn.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mQuantiteNumberBtn, InputMethodManager.SHOW_IMPLICIT);
        */

        if (selectedProduct.getFile_content() != null) {

            File imgFile = new File(selectedProduct.getFile_content());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mPosterIV.setImageBitmap(myBitmap);
            } else {
                mPosterIV.setImageResource(R.drawable.iscreen_no_image);
            }
        } else {
            mPosterIV.setImageResource(R.drawable.iscreen_no_image);
        }
    }

}
