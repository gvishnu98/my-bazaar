package com.example.mybazaar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.mybazaar.RegisterActivity.setSignUpFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FrameLayout frameLayout;
    private ImageView actionBarLogo;
    private static final int HOME_FRAGMENT=0;
    private static final int ORDERS_FRAGMENT=1;
    private static final int CART_FRAGMENT=2;
    private static final int ACCOUNT_FRAGMENT=3;
    public static Boolean showCart=false;

    private int currentFragment=-1;
    private NavigationView navigationView;
    private TextView badgeCount;

    private Dialog signInDialog;
    private FirebaseUser firebaseAuthCurrentUser;
    public static DrawerLayout drawer;

    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionBarLogo =findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);

        params=(AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags=params.getScrollFlags();
        drawer = findViewById(R.id.drawer_layout);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout=findViewById(R.id.main_framelayout);

            if (showCart) {
                drawer.setDrawerLockMode(1);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
                goToFragment("My Cart", new MyCartFragment(), -2);
            } else {
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            }

         signInDialog=new Dialog(HomeActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent =new Intent(HomeActivity.this,RegisterActivity.class);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setSignUpFragment=false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setSignUpFragment=true;
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuthCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuthCurrentUser==null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(false);
        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if (currentFragment==HOME_FRAGMENT){
                currentFragment=-1;
                super.onBackPressed();
            }else {
                if (showCart){
                    showCart=false;
                    finish();
                }else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment==HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.home, menu);

            MenuItem cartItem=menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.cart);
                badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);
                if (firebaseAuthCurrentUser!=null){
                    if (DBqueries.cartList.size() == 0) {
                        DBqueries.loadCartList(HomeActivity.this, new Dialog(HomeActivity.this), false,badgeCount);
                    }else{
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
                        if (firebaseAuthCurrentUser == null) {
                            signInDialog.show();
                        }else {
                            goToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                        }
                    }
                });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.main_search_icon){

            return true;
        }else if (id==R.id.main_notification_icon){
            return true;
        }else if (id==R.id.main_cart_icon){
            if (firebaseAuthCurrentUser == null) {
                signInDialog.show();
            }else {
                goToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        }else if (id==android.R.id.home){
            if (showCart){
                showCart=false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToFragment(String title,Fragment fragment,int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().getThemedContext();
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(79,121,66));
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if (fragmentNo==CART_FRAGMENT){
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{
            params.setScrollFlags(scrollFlags);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        if (firebaseAuthCurrentUser!=null) {
            int id = item.getItemId();
            if (id == R.id.nav_my_bazaar) {
                actionBarLogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            } else if (id == R.id.nav_my_orders) {
                goToFragment("My Orders", new MyOrderFragment(), ORDERS_FRAGMENT);
            } else if (id == R.id.nav_my_cart) {
                goToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            } else if (id == R.id.nav_my_account) {
                goToFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
            } else if (id == R.id.nav_sign_out) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerIntent=new Intent(HomeActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else {
            drawer.closeDrawer(GravityCompat.START);
             signInDialog.show();
             return false;
        }

    }

    private void setFragment(Fragment fragment,int fragmentNo){
        if (fragmentNo !=currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
}