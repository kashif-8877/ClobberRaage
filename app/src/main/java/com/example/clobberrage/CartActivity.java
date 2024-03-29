package com.example.clobberrage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clobberrage.Model.Cart;
import com.example.clobberrage.Prevalent.Prevalent;
import com.example.clobberrage.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;

    private int overTotalPrice = 0;
    private String prQuantity,prPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.total_price);
        txtMsg1 = findViewById(R.id.msg1);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CartActivity.this,ConformFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentLogedInUser.getPhone())
                .child("Products"),Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Quantity = "+ model.getQuantity());
                holder.txtProductPrice.setText("Price = "+ model.getPrice());
                holder.txtProductName.setText(model.getCategory());

                prQuantity = holder.txtProductQuantity.getText().toString();
                prPrice = holder.txtProductPrice.getText().toString();
                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;
                txtTotalAmount.setText("Total Amount =  "+ String.valueOf(overTotalPrice));
                Picasso.get().load(model.getImage()).into(holder.imgProductImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "update",
                                        "Remove"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if (which == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this , ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }

                                if (which == 1)
                                {
                                    cartListRef.child("User View").child(Prevalent.currentLogedInUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item removed Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(CartActivity.this,HomeActivity.class));
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
               View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
               CartViewHolder holder = new CartViewHolder(view);
               return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderState()
{
    DatabaseReference oderRef = FirebaseDatabase.getInstance().getReference()
            .child("Orders")
            .child(Prevalent.currentLogedInUser.getPhone());
    oderRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            if (dataSnapshot.exists())
            {
                String shippingState = dataSnapshot.child("state").getValue().toString();
                String userName = dataSnapshot.child("name").getValue().toString();

                if (shippingState.equals("shipped"))
                {
                    recyclerView.setVisibility(View.GONE);
                    txtMsg1.setVisibility(View.VISIBLE);
                    txtMsg1.setText("Congratulations, your final order has been shipped successfully. Soon you will recive your order.");
                    nextProcessBtn.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "You can purchase more products once you recive your first order.", Toast.LENGTH_SHORT).show();
                }
                else if (shippingState.equals("not shipped"))
                {
                    txtTotalAmount.setText("your order is not shipped shipped yet.");
                    recyclerView.setVisibility(View.GONE);
                    txtMsg1.setVisibility(View.VISIBLE);
                    nextProcessBtn.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "You can purchase more products once you recive your first order.", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
}
