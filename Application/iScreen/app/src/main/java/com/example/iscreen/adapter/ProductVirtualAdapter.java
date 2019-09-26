package com.example.iscreen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.ProductVirtualAdapterListener;
import com.example.iscreen.interfaces.ProduitsAdapterListener;
import com.example.iscreen.remote.model.ProductVirtual;
import com.example.iscreen.utility.IScreenUtility;

import java.io.File;
import java.util.List;

public class ProductVirtualAdapter extends RecyclerView.Adapter<ProductVirtualAdapter.ProduitsVirtualViewHolder> {
    private static final String TAG = ProductVirtualAdapter.class.getSimpleName();

    private Context mContext;
    private List<ProductVirtual> productVirtuals;
    private ProductVirtualAdapterListener mListener;

    //    ViewHolder de l'adapter
    public class ProduitsVirtualViewHolder extends RecyclerView.ViewHolder {
        TextView label, priceHT, priceTTC;
        ImageView poster;
        View itemView;
//        public ImageButton details;

        public ProduitsVirtualViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.view_productvirtual);
            label = view.findViewById(R.id.tv_productvirtual_label);
            priceHT = view.findViewById(R.id.tv_productvirtual_prix_ht);
            priceTTC = view.findViewById(R.id.tv_productvirtual_prix_ttc);

//            ecoute du clique sur le bouton de shopping(ajout dans le panier)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    mListener.onProductVirtualClicked(productVirtuals.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }


    public ProductVirtualAdapter(Context context, List<ProductVirtual> produitsList, ProductVirtualAdapterListener listener) {
        this.mContext = context;
        this.productVirtuals = produitsList;
        this.mListener = listener;
    }

    @Override
    public ProductVirtualAdapter.ProduitsVirtualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_productvirtual, parent, false);

        return new ProductVirtualAdapter.ProduitsVirtualViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductVirtualAdapter.ProduitsVirtualViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: categorieId="+productVirtuals.get(position).getFk_product_pere()+
                " label="+productVirtuals.get(position).getLabel()+
                " id="+productVirtuals.get(position).getRowid()+
                " price="+productVirtuals.get(position).getPrice()+
                " qty="+productVirtuals.get(position).getQty());

        String[] label = productVirtuals.get(position).getLabel().split(" ");

        holder.label.setText(label[label.length-1]+" x "+productVirtuals.get(position).getQty());
        holder.priceHT.setText(String.format("%s %s HT",
                IScreenUtility.amountFormat2(productVirtuals.get(position).getPrice()),
                IScreenUtility.CURRENCY));
        holder.priceTTC.setText(String.format("%s %s TTC",
                IScreenUtility.amountFormat2(productVirtuals.get(position).getPrice_ttc()),
                IScreenUtility.CURRENCY));
    }

    @Override
    public int getItemCount() {
        if (productVirtuals != null) {
            return productVirtuals.size();
        }
        return 0;
    }
}
