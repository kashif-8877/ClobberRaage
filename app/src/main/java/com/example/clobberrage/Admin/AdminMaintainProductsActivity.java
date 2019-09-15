package com.example.clobberrage.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clobberrage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn,deleteBtn;
    private EditText name,price,brand;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        brand = findViewById(R.id.product_brand_maintain);
        deleteBtn=findViewById(R.id.delete_product_btn);

        imageView = findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminMaintainProductsActivity.this, AdminAddNewProductActivity.class));
                    finish();
                    Toast.makeText(AdminMaintainProductsActivity.this, "Product is deleted Successfull.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void applyChanges()
    {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pBrand = brand.getText().toString();

        if (pName.equals(""))
        {
            name.setError("Empty");
            return;
        }
        if (pPrice.equals(""))
        {
            price.setError("Empty");
            return;
        }
        if (pBrand.equals(""))
        {
            brand.setError("Empty");
            return;
        }

        else
        {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("pid",productID);
            hashMap.put("pname",pName);
            hashMap.put("price",pPrice);
            hashMap.put("brand",pBrand);

            productsRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applist Successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminMaintainProductsActivity.this,AdminAddNewProductActivity.class));
                    }

                }
            });
        }
    }

    private void displaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String Pname = dataSnapshot.child("pname").getValue().toString();
                    String Pprice = dataSnapshot.child("price").getValue().toString();
                    String Pbrand = dataSnapshot.child("brand").getValue().toString();
                    String Piamge = dataSnapshot.child("pimage").getValue().toString();

                    name.setText(Pname);
                    price.setText(Pprice);
                    brand.setText(Pbrand);

                    Picasso.get().load(Piamge).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
