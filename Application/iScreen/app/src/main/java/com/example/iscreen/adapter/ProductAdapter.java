package com.example.iscreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.OnItemClickListener;
import com.example.iscreen.interfaces.ProduitsAdapterListener;
import com.example.iscreen.pages.home.DetailProduct;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RandomAdapterViewHolder> {
    private final String TAG = ProductAdapter.class.getSimpleName();
    private Context mContext;
    private List<ProduitEntry> randomList;
    private ProduitsAdapterListener productListener;
    private static OnItemClickListener onItemClickListener1;
    private AppDatabase db;

    private int mainLayoutWidth = 0;
    private int mainLayoutHeight = 0;

    public ProductAdapter(Context context, List<ProduitEntry> randomList, int mainLayoutWidth, int mainLayoutHeight, ProduitsAdapterListener productListener){
        this.mContext = context;
        this.randomList = randomList;
        this.mainLayoutWidth = mainLayoutWidth;
        this.mainLayoutHeight = mainLayoutHeight;
        this.productListener = productListener;
        this.db = AppDatabase.getInstance(this.mContext);
    }

    public class RandomAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout mainLayout;
        ImageView product_iv;
        TextView product_name;
        TextView product_price;

        public RandomAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.custom_random_catalog_mainLayout);
            product_iv = itemView.findViewById(R.id.custom_random_catalog_item_productimage);
            product_name = itemView.findViewById(R.id.custom_random_catalog_item_productname);
            product_price = itemView.findViewById(R.id.custom_random_catalog_item_productprice);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener1.setOnItemClick(v, randomList.get(getAdapterPosition()));
        }

        public void setOnItemClickListener (final OnItemClickListener onItemClickListener) {
            onItemClickListener1 = onItemClickListener;
        }
    }

    @NonNull
    @Override
    public RandomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_catalog_item, viewGroup, false);
        return new RandomAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RandomAdapterViewHolder viewHolder, final int i) {

        // Show Product Title
        if (db.configurationDao().getCurrentConfig().get(0).isProductTitle()){
            viewHolder.product_name.setVisibility(View.VISIBLE);
            viewHolder.product_name.setText(randomList.get(i).getLabel());
        }else{
            viewHolder.product_name.setText("");
            viewHolder.product_name.setVisibility(View.GONE);
        }

        // Show Product Price
        if (db.configurationDao().getCurrentConfig().get(0).isProductPrice()){
            viewHolder.product_price.setVisibility(View.VISIBLE);
            viewHolder.product_price.setText(randomList.get(i).getPrice());
        }else{
            viewHolder.product_price.setText("");
            viewHolder.product_price.setVisibility(View.GONE);
        }

        // Show Product Image
        if (randomList.get(i).getFile_content() != null) {
            File imgFile = new File(randomList.get(i).getFile_content());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.product_iv.setImageBitmap(myBitmap);
            } else {
                viewHolder.product_iv.setImageResource(R.drawable.no_image_available);
            }
        } else {
            viewHolder.product_iv.setImageResource(R.drawable.no_image_available);
        }

        // Set custom product size (responsible)
        viewHolder.mainLayout.setLayoutParams(new FrameLayout.LayoutParams(mainLayoutWidth, mainLayoutHeight));

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Feature is desactivated or commented!!\nShow Product '"+randomList.get(i).getLabel()+"' In Detail !!!");
                // productListener.onDetailsSelected(randomList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }

    public void setOnItemClickListener (final OnItemClickListener onItemClickListener) {
        this.onItemClickListener1 = onItemClickListener;
    }
}
