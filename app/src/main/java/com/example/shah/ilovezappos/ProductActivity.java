package com.example.shah.ilovezappos;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shah.ilovezappos.databinding.ActivityProductBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.realm.Realm;

public class ProductActivity extends AppCompatActivity {
    private boolean isInCart = false;
    private Realm realm;
    private Product product;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetContentView with data binding
        ActivityProductBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        final CollapsingToolbarLayout collapseToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //Initialize realm for adding product to cart
        Realm.init(ProductActivity.this);
        realm = Realm.getDefaultInstance();

        //Get searched product data from Intent extra
        product = (Product) getIntent().getSerializableExtra("product");
        binding.setProduct(product);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInCart) {
                    Animation myAnim = AnimationUtils.loadAnimation(ProductActivity.this, R.anim.bubble);

                    BubbleInterpolator interpolator = new BubbleInterpolator(0.4, 20);
                    myAnim.setInterpolator(interpolator);

                    myAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fab.clearAnimation();
                            fab.setImageDrawable(ContextCompat.getDrawable(ProductActivity.this, R.drawable.ic_shopping_cart_black_24dp));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fab.startAnimation(myAnim);

                    //Add product to cart (Realm)
                    realm.beginTransaction();
                    realm.copyToRealm(product);
                    realm.commitTransaction();
                    isInCart = true;
                } else {
                    Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Share product
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this product on Zappos. " + product.getProductUrl());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share with friend!"));
            }
        });

        final ImageView ivThumb = (ImageView) findViewById(R.id.imageViewProduct);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        ivThumb.setImageBitmap(bitmap);
                        int defaultColor = ContextCompat.getColor(ProductActivity.this, R.color.colorAccent);

                        //set collapse toolbar and scrim color based on image
                        collapseToolbar.setBackgroundColor(palette.getLightVibrantColor(defaultColor));
                        collapseToolbar.setContentScrimColor(palette.getLightVibrantColor(defaultColor));
                        collapseToolbar.setStatusBarScrimColor(palette.getLightMutedColor(defaultColor));
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this).load(product.getThumbnailImageUrl()).into(target);

        TextView tvOriginalPrice = (TextView) findViewById(R.id.textViewOriginalPrice);
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tvPercentOff = (TextView) findViewById(R.id.textViewDisc);

        //Hide percent off and original price if there is no discount
        if (product.getPercentOff().equals("0%")) {
            tvPercentOff.setVisibility(View.GONE);
            tvOriginalPrice.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check if product is in cart, set flag and image accordingly
        if (realm.where(Product.class).equalTo("styleId", product.getStyleId()).count() > 0){
            fab.setImageDrawable(ContextCompat.getDrawable(ProductActivity.this, R.drawable.ic_shopping_cart_black_24dp));
            isInCart = true;
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(ProductActivity.this, R.drawable.ic_add_shopping_cart_black_24dp));
            isInCart = false;
        }
    }
}
