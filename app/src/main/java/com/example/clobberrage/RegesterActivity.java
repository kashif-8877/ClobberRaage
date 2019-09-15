package com.example.clobberrage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegesterActivity extends AppCompatActivity {

    private EditText userName, phoneNumber,userPassword;
    private Button creatAccountButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        userName = findViewById(R.id.register_username);
        phoneNumber = findViewById(R.id.register_phone_number);
        userPassword = findViewById(R.id.register_app_password);
        creatAccountButton = findViewById(R.id.register_btn);
        progressDialog = new ProgressDialog(this);
        creatAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatAccount();
            }
        });
    }

    public void CreatAccount()
    {
        String User_Name = userName.getText().toString().trim();
        String phone_Number = phoneNumber.getText().toString().trim();
        String User_password = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(User_Name))
        {
            userName.setError("empty!");
            return;
        }
        else if (TextUtils.isEmpty(phone_Number))
        {
            phoneNumber.setError("empty!");
            return;
        }
        else if (TextUtils.isEmpty(User_password))
        {
            userPassword.setError("empty!");
            return;
        }
        else
        {
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            validatePhoneNumber(User_Name, phone_Number ,User_password);

        }
    }

    private void validatePhoneNumber(final String user_name, final String phone_number, final String user_password)
    {
       final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
       rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (!(dataSnapshot.child("Users").child(phone_number).exists()))
               {
                   HashMap<String,Object> hashMap = new HashMap<>();
                   hashMap.put("name", user_name);
                   hashMap.put("password",user_password);
                   hashMap.put("phone",phone_number);

                   rootRef.child("Users").child(phone_number).updateChildren(hashMap)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task)
                               {
                                   if (task.isSuccessful())
                                   {
                                       Toast.makeText(RegesterActivity.this, "Your Account has been created.", Toast.LENGTH_SHORT).show();
                                       progressDialog.dismiss();
                                       startActivity(new Intent(RegesterActivity.this,LoginActivity.class));
                                       finish();
                                   }
                                   else
                                   {
                                       progressDialog.dismiss();
                                       Toast.makeText(RegesterActivity.this, "Network issues, Please check the network.", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

               }
               else
               {
                   Toast.makeText(RegesterActivity.this, "This "+phone_number +"Already exists.", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
                   Toast.makeText(RegesterActivity.this, " Please try again with another Phone number.", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(RegesterActivity.this,MainActivity.class));
                   finish();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

}
