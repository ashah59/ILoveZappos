package com.example.shah.ilovezappos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CartActivity extends AppCompatActivity implements CartAdapter.IActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        final RecyclerView rvCart = (RecyclerView) findViewById(R.id.rvCart);
        final TextView tvEmptyCart = (TextView) findViewById(R.id.textViewEmptyCart);

        //Get all products available in cart
        Realm.init(CartActivity.this);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Product> products = realm.where(Product.class).findAll();

        //Initialize adapter and set it to recycler view
        final CartAdapter adapter = new CartAdapter(this, products);
        rvCart.setAdapter(adapter);
        //Initialize layout manager and set it to recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(linearLayoutManager);

        //Monitor change in cart data on delete
        products.addChangeListener(new RealmChangeListener<RealmResults<Product>>() {
            @Override
            public void onChange(RealmResults<Product> element) {
                if (products.size() == 0) {
                    rvCart.setVisibility(View.GONE);
                    tvEmptyCart.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void DeleteProd(final String styleId) {
        Realm.init(CartActivity.this);
        Realm realm = Realm.getDefaultInstance();
        //Delete particular product from cart
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Product res = bgRealm.where(Product.class).equalTo("styleId", styleId).findFirst();
                res.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        });
    }
}
