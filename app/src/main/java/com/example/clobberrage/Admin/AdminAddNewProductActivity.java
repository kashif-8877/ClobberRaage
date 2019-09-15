package com.example.clobberrage.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.clobberrage.HomeActivity;
import com.example.clobberrage.MainActivity;
import com.example.clobberrage.R;

import io.paperdb.Paper;

public class AdminAddNewProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Clobber Rage");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_add_new_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.check_orders_btn)
        {
            startActivity(new Intent(AdminAddNewProductActivity.this, AdminNewOrdersActivity.class));
            // Handle the camera action
        }  else if (id == R.id.admin_logout_btn)
        {
            Paper.book().destroy();
            Intent intent = new Intent(AdminAddNewProductActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        else if (id ==  R.id.admin_maintain_products)
        {
            Intent intent = new Intent(AdminAddNewProductActivity.this, HomeActivity.class);
            intent.putExtra("Admin","Admin");
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addProdducts(View view)
    {
        switch (view.getId())
        {
            case R.id.mens_polo:
            {
                Intent intent = new Intent(AdminAddNewProductActivity.this, AdminAddProductsActivity.class);
                intent.putExtra("category","Men_Polo");
                startActivity(intent);
                break;
            }
            case R.id.mens_t_shirts:
            {
                Intent intent = new Intent(AdminAddNewProductActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Men_T_Shirts");
                startActivity(intent);
                break;
            }

            case R.id.mens_casual_shirts:
            {
                Intent intent = new Intent(AdminAddNewProductActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Men_Casual_shirts");
                startActivity(intent);
                break;
            }

            case R.id.mens_jeans:
            {
                Intent intent = new Intent(AdminAddNewProductActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Men_Jeans");
                startActivity(intent);
                break;
            }
            case R.id.mens_sweaters:
            {
                Intent intent = new Intent(AdminAddNewProductActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Men_Sweaters");
                startActivity(intent);
                break;
            }

        }
    }
}
