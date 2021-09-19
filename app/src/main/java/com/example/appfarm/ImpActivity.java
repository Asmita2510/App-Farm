 package com.example.appfarm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.appfarm.DBqueries.currentUsar;

public class  ImpActivity<onNavigationItemSelected> extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;

    private static final int HOME_FRAGMENT =0;
    private static final int ACCOUNT_FRAGMENT =1;

    private FrameLayout frameLayout;
    private ImageView actionBarLogo;
    private NavigationView navigationView;
    private ImageView actionbarLogo;
    private static int currentFragment=-1;

    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;

    private CircleImageView profileView;
    private TextView fullname,email;
    private ImageView addProfileIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,0,0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);

        profileView = findViewById(R.id.imp_profile_image);
        fullname = findViewById(R.id.imp_fullname);
        email = findViewById(R.id.imp_email);
        addProfileIcon = findViewById(R.id.add_profile_icon);

        if (currentUsar!=null){
            FirebaseFirestore.getInstance().collection("USERS").document(currentUsar.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DBqueries.fullname = task.getResult().getString("FullName");
                        DBqueries.email = task.getResult().getString("Email");
                        DBqueries.profile = task.getResult().getString("Profile");


                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ImpActivity.this,error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }

        setFragment(new HomeFragment(),HOME_FRAGMENT);

        if (currentUsar == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        }else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
/////////////
         /*signInDialog = new Dialog(ImpActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(ImpActivity.this,RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        signInDialog.show();*/
/////////////
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if (currentFragment == HOME_FRAGMENT){
                currentFragment =-1;
                super.onBackPressed();
            }else {
                actionBarLogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(),HOME_FRAGMENT);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getMenuInflater().inflate(R.menu.imp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();

        if (id == R.id.imp_search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title,Fragment fragment,int fragmentNo)
    {
        invalidateOptionsMenu();
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        setFragment(fragment,fragmentNo);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.nav_app_farm){
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            setFragment(new HomeFragment(),HOME_FRAGMENT);
         }
        else if (id == R.id.nav_my_account){
            gotoFragment("My Account",new MyAccountFragment(),ACCOUNT_FRAGMENT);
        }
        else if (id == R.id.nav_sign_out){
            FirebaseAuth.getInstance().signOut();
            Intent registerIntent  = new Intent(ImpActivity.this,RegisterActivity.class);
            startActivity(registerIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void setFragment(Fragment fragment,int fragmentNo){
        if (fragmentNo!=currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
}