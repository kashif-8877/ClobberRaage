package com.example.clobberrage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clobberrage.Model.Products;
import com.example.clobberrage.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SelectedProductCategoryActivity extends AppCompatActivity {

    private TextView categoryTv;
    private RecyclerView productCategory;
    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selcted_product_category);

        Bundle bundle = getIntent().getExtras();
        categoryName = bundle.getString("category");
        categoryTv = findViewById(R.id.tv_category);
        productCategory = findViewById(R.id.searched_category_list);

        categoryTv.setText(categoryName);

        productCategory.setLayoutManager(new LinearLayoutManager(SelectedProductCategoryActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("category").startAt(categoryName).endAt(categoryName),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
            {
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductBrand.setText(model.getBrand());

                Picasso.get().load(model.getPimage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SelectedProductCategoryActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        intent.putExtra("image",model.pimage);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        productCategory.setAdapter(adapter);
        adapter.startListening();
    }
}
