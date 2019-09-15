package com.example.clobberrage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.clobberrage.Admin.AdminAddNewProductActivity;
import com.example.clobberrage.Admin.AdminAddProductsActivity;

public class ProductsCategoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_category_list);
    }

    public void addProdducts(View view)
    {
        switch (view.getId())
        {
            case R.id.category_mens_polo:
            {
                Intent intent = new Intent(ProductsCategoryListActivity.this, SelectedProductCategoryActivity.class);
                intent.putExtra("category","Men_Polo");
                startActivity(intent);
                break;
            }
            case R.id.category_mens_t_shirts:
            {
                Intent intent = new Intent(ProductsCategoryListActivity.this,SelectedProductCategoryActivity.class);
                intent.putExtra("category","Men_T_Shirts");
                startActivity(intent);
                break;
            }

            case R.id.category_mens_casual_shirts:
            {
                Intent intent = new Intent(ProductsCategoryListActivity.this,SelectedProductCategoryActivity.class);
                intent.putExtra("category","Men_Casual_shirts");
                startActivity(intent);
                break;
            }

            case R.id.category_mens_jeans:
            {
                Intent intent = new Intent(ProductsCategoryListActivity.this,SelectedProductCategoryActivity.class);
                intent.putExtra("category","Men_Jeans");
                startActivity(intent);
                break;
            }
            case R.id.category_mens_sweaters:
            {
                Intent intent = new Intent(ProductsCategoryListActivity.this,SelectedProductCategoryActivity.class);
                intent.putExtra("category","Men_Sweaters");
                startActivity(intent);
                break;
            }

        }
    }
}
