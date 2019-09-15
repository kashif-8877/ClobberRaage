package com.example.clobberrage.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clobberrage.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductsActivity extends AppCompatActivity {

   private ImageView newProductImage;
   private TextView selectImageTview;
   private EditText newProductName,newProductPrice,newProductDescription,newProductBrand;
   private Button addProductBtn;
   private String categoryName,P_name,P_price,P_description,P_brand,saveCurrantDate,saveCurrantTime;
   private String productRandomKey,downloadImageUrl;
   private static final int galleryPickCode = 1;
   private Uri imageUri;
   private StorageReference productImageRef;
   private DatabaseReference productDataBaseRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_products);
        progressDialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        categoryName = bundle.getString("category");
        productDataBaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        newProductImage = findViewById(R.id.select_product_picture);
        selectImageTview = findViewById(R.id.tv_select_image);
        newProductName = findViewById(R.id.product_name);
        newProductPrice = findViewById(R.id.product_price);
        newProductDescription = findViewById(R.id.product_description);
        newProductBrand = findViewById(R.id.product_brand);
        addProductBtn = findViewById(R.id.add_new_product);

        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                selectImageTview.setVisibility(View.INVISIBLE);

            }

        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }

    private void validateProductData()
    {
        P_name = newProductName.getText().toString().trim();
        P_description = newProductDescription.getText().toString().trim();
        P_brand = newProductBrand.getText().toString().trim();
        P_price = newProductPrice.getText().toString().trim();

        if (imageUri ==null)
        {
            Toast.makeText(this, "Please Enter Product Image..", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(P_name))
        {
            newProductName.setError("Empty");
            return;
        }
        else if (TextUtils.isEmpty(P_description))
        {
            newProductDescription.setError("Empty");
            return;
        }
        else if (TextUtils.isEmpty(P_price))
        {
            newProductPrice.setError("Empty");
            return;
        }
        else if (TextUtils.isEmpty(P_brand))
        {
            newProductBrand.setError("Empty");
            return;
        }
        else
        {
            storeProductInformation();
        }
    }

    private void storeProductInformation()
    {
        progressDialog.setTitle("Adding new Product");
        progressDialog.setMessage("Please wait, while we adding new product.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currantDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrantDate = currantDate.format(calendar.getTime());

        SimpleDateFormat currantTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrantTime = currantTime.format(calendar.getTime());

        productRandomKey = saveCurrantDate+saveCurrantTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey+".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
               String message = e.toString();
                Toast.makeText(AdminAddProductsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductsActivity.this, "Product image uploaded successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddProductsActivity.this, "Getting product image url successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDataBase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDataBase()
    {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("pid",productRandomKey);
        hashMap.put("date",saveCurrantDate);
        hashMap.put("time",P_description);
        hashMap.put("pimage",downloadImageUrl);
        hashMap.put("category",categoryName);
        hashMap.put("pname",P_name);
        hashMap.put("price",P_price);
        hashMap.put("brand",P_brand);

        productDataBaseRef.child(productRandomKey).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(AdminAddProductsActivity.this, "Product added Successfully...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminAddProductsActivity.this, AdminAddNewProductActivity.class ));
                }
                else
                {
                    progressDialog.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddProductsActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openGallery()
    {
        Intent getPicture = new Intent(Intent.ACTION_GET_CONTENT);
        getPicture.setType("image/*");
        startActivityForResult(getPicture,galleryPickCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPickCode && resultCode == RESULT_OK && data != null)
        {
            imageUri = data.getData();
            newProductImage.setImageURI(imageUri);
        }
    }
}
