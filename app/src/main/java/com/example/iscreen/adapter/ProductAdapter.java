package com.example.iscreen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.entity.ProduitEntry;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RandomAdapterViewHolder> {
    private final String TAG = ProductAdapter.class.getSimpleName();
    private Context mContext;
    private List<ProduitEntry> randomList;

    private int mainLayoutWidth = 0;
    private int mainLayoutHeight = 0;

    public ProductAdapter(Context context, List<ProduitEntry> randomList, int mainLayoutWidth, int mainLayoutHeight){
        this.mContext = context;
        this.randomList = randomList;
        this.mainLayoutWidth = mainLayoutWidth;
        this.mainLayoutHeight = mainLayoutHeight;
    }

    public class RandomAdapterViewHolder extends RecyclerView.ViewHolder {
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
    }

    @NonNull
    @Override
    public RandomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_catalog_item, viewGroup, false);
        return new RandomAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomAdapterViewHolder viewHolder, int i) {
        viewHolder.product_name.setText(randomList.get(i).getLabel());
        viewHolder.product_price.setText(randomList.get(i).getPrice());

        if (randomList.get(i).getFile_content() != null) {

            File imgFile = new File(randomList.get(i).getFile_content());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.product_iv.setImageBitmap(myBitmap);
            } else {
                viewHolder.product_iv.setImageResource(R.drawable.iscreen_no_image);
            }
        } else {
            viewHolder.product_iv.setImageResource(R.drawable.iscreen_no_image);
        }

        // Set custom product size (responsible)
        viewHolder.mainLayout.setLayoutParams(new FrameLayout.LayoutParams(mainLayoutWidth, mainLayoutHeight));
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }


}
