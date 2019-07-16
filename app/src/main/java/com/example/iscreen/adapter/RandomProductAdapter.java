package com.example.iscreen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.entity.ProduitEntry;

import java.util.List;

public class RandomProductAdapter extends RecyclerView.Adapter<RandomProductAdapter.RandomAdapterViewHolder> {
    private final String TAG = RandomProductAdapter.class.getSimpleName();
    private Context mContext;
    private List<ProduitEntry> randomList;

    public RandomProductAdapter(Context context, List<ProduitEntry> randomList){
        this.mContext = context;
        this.randomList = randomList;
    }

    public class RandomAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView product_iv;
        TextView product_name;
        TextView product_price;

        public RandomAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
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
        viewHolder.product_iv.setImageResource(R.drawable.iscreen_no_image);
        viewHolder.product_name.setText(randomList.get(i).getLabel());
        viewHolder.product_price.setText(randomList.get(i).getPrice());
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }


}
