package com.example.mybazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mybazaar.HomeActivity.showCart;
import static com.example.mybazaar.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_cart_query =false;

    public static String productID;
    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView productPrice;
    private TextView cutPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewPagerIndicator;

    private Dialog loadingDialog;
    private DocumentSnapshot documentSnapshot;

    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    public static boolean ALREADY_ADDED_TO_CART=false;

    private FirebaseFirestore firebaseFirestore;
    private Dialog signInDialog;
    private FirebaseUser firebaseAuthCurrentUser;
    private TextView badgeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        productImagesViewPager=findViewById(R.id.product_images_viewpager);
        viewPagerIndicator=findViewById(R.id.viewpager_indicator);
        productDetailsViewPager=findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout=findViewById(R.id.product_details_tablayout);
        buyNowBtn=findViewById(R.id.buy_now_btn);
        productTitle=findViewById(R.id.product_title);
        productPrice=findViewById(R.id.product_price);
        cutPrice=findViewById(R.id.cut_price);
        codIndicator=findViewById(R.id.cod_indicator_imageview);
        tvCodIndicator=findViewById(R.id.tv_cod_indicator);
        productDetailsTabsContainer=findViewById(R.id.product_details_tabs_container);
        addToCartBtn=findViewById(R.id.add_to_cart_btn);

        loadingDialog=new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firebaseFirestore=FirebaseFirestore.getInstance();

        final List<String> productImages =new ArrayList<>();
        productID=getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                     if (task.isSuccessful()) {
                                                         documentSnapshot = task.getResult();
                                                         for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                                             productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                                         }
                                                         ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                                         productImagesViewPager.setAdapter(productImagesAdapter);

                                                         productTitle.setText(documentSnapshot.get("product_title").toString());
                                                         productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                                                         cutPrice.setText("Rs." + documentSnapshot.get("cut_price").toString() + "/-");
                                                         if ((boolean) documentSnapshot.get("COD")) {
                                                             codIndicator.setVisibility(View.VISIBLE);
                                                             tvCodIndicator.setVisibility(View.VISIBLE);
                                                         } else {
                                                             codIndicator.setVisibility(View.INVISIBLE);
                                                             tvCodIndicator.setVisibility(View.INVISIBLE);
                                                         }

                                                         if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                                             productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                                             ProductDetailsFragment.productDetails = documentSnapshot.get("product_details").toString();
                                                         } else {
                                                             productDetailsTabsContainer.setVisibility(View.GONE);
                                                         }

                                                         productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount()));
                                                         if (firebaseAuthCurrentUser != null) {
                                                             if (DBqueries.cartList.size() == 0) {
                                                                 DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount);
                                                             } else {
                                                                 loadingDialog.dismiss();
                                                             }
                                                         } else {
                                                             loadingDialog.dismiss();
                                                         }

                                                         if (DBqueries.cartList.contains(productID)) {
                                                             ALREADY_ADDED_TO_CART = true;
                                                         } else {
                                                             ALREADY_ADDED_TO_CART = false;
                                                         }
                                                     } else {
                                                         loadingDialog.dismiss();
                                                         String error = task.getException().getMessage();
                                                         Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });


        viewPagerIndicator.setupWithViewPager(productImagesViewPager,true);


         productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
         productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 productDetailsViewPager.setCurrentItem(tab.getPosition());
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });

         buyNowBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (firebaseAuthCurrentUser == null){
                     signInDialog.show();
                 }else {
                     Intent deliveryIntent=new Intent(ProductDetailsActivity.this,DeliveryActivity.class);
                     startActivity(deliveryIntent);
                 }
             }
         });

         addToCartBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (firebaseAuthCurrentUser == null){
                     signInDialog.show();
                 }else {
                     if (!running_cart_query){
                         running_cart_query=true;
                         if (ALREADY_ADDED_TO_CART){
                             running_cart_query=false;
                             Toast.makeText(ProductDetailsActivity.this,"Already added to cart!",Toast.LENGTH_SHORT).show();
                         }else{
                             Map<String,Object> addProduct=new HashMap<>();
                             addProduct.put("product_ID_"+String.valueOf(DBqueries.cartList.size()),productID);
                             addProduct.put("list_size",(long)(DBqueries.cartList.size() +1));

                             firebaseFirestore.collection("USERS").document(firebaseAuthCurrentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                     .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         if (DBqueries.cartItemModelList.size() !=0){
                                             DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,productID,documentSnapshot.get("product_image_1").toString()
                                                     ,documentSnapshot.get("product_title").toString()
                                                     ,documentSnapshot.get("product_price").toString()
                                                     ,documentSnapshot.get("cut_price").toString()
                                                     ,(long)1
                                                     ,(long)0));

                                         }
                                         ALREADY_ADDED_TO_CART=true;
                                         DBqueries.cartList.add(productID);
                                         Toast.makeText(ProductDetailsActivity.this,"Added to cart successfully",Toast.LENGTH_SHORT).show();
                                         invalidateOptionsMenu();
                                         running_cart_query=false;
                                     }else {
                                         running_cart_query=false;
                                         String error=task.getException().getMessage();
                                         Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });
                         }
                     }
                 }
             }
         });

         ///// sign in dialog
        signInDialog=new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent =new Intent(ProductDetailsActivity.this,RegisterActivity.class);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn=true;
                SignUpFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setSignUpFragment=false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn=true;
                SignUpFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setSignUpFragment=true;
                startActivity(registerIntent);
            }
        });
        ////sign in dialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuthCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuthCurrentUser!=null) {

        }else{
            loadingDialog.dismiss();
        }


        if (DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CART=true;
        }else{
            ALREADY_ADDED_TO_CART=false;
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem=menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart);
            badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);

            if (firebaseAuthCurrentUser!=null){
                   if (DBqueries.cartList.size() == 0) {
                         DBqueries.loadCartList(ProductDetailsActivity.this,loadingDialog , false,badgeCount);
                    }
                   else{
                       badgeCount.setVisibility(View.VISIBLE);
                       if (DBqueries.cartList.size()<99) {
                           badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                       }else{
                           badgeCount.setText("99");
                       }
                   }
             }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseAuthCurrentUser==null){
                        signInDialog.show();
                    }else {
                        Intent cartIntent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id==android.R.id.home){
            finish();
            return true;
        }else if (id==R.id.main_search_icon){
            return true;
        }else if (id==R.id.main_cart_icon){
            if (firebaseAuthCurrentUser==null){
                signInDialog.show();
            }else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}