package com.example.shah.ilovezappos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shah on 2/10/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartRecyclerViewHolder> {

    Context mContext;
    List<Product> products;
    IActivity activity;

    public CartAdapter(Context mContext, List<Product> products) {
        this.mContext = mContext;
        this.products = products;
        this.activity = (IActivity) mContext;
    }

    @Override
    public CartRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.cart_row, parent, false);
        CartRecyclerViewHolder viewHolder = new CartRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartRecyclerViewHolder holder, int position) {
        final Product prod = products.get(position);
        holder.tvProductName.setText(prod.getProductName() + " - " + prod.getPrice());
        Picasso.with(mContext).load(prod.getThumbnailImageUrl()).into(holder.ivThumb);
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.DeleteProd(prod.getStyleId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;
        ImageView ivThumb, ivDel;

        public CartRecyclerViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.textViewCartRow);
            ivThumb = (ImageView) itemView.findViewById(R.id.imageViewCartRow);
            ivDel = (ImageView) itemView.findViewById(R.id.imageViewRemoveCart);
        }
    }

    public interface IActivity {
        public void DeleteProd(String styleId);
    }
}
