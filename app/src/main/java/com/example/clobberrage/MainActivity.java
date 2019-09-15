package com.example.clobberrage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.clobberrage.Admin.AdminAddNewProductActivity;
import com.example.clobberrage.Model.Users;
import com.example.clobberrage.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton, loginButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        progressDialog = new ProgressDialog(this);

        joinNowButton = findViewById(R.id.main_join_now__btn);
        loginButton = findViewById(R.id.main_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegesterActivity.class));
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPnoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        String userParentKey = Paper.book().read(Prevalent.userParentDbNameKey);

        if (userPhoneKey != "" && userPasswordKey != "" && userParentKey != "")
        {
            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey) && !TextUtils.isEmpty(userParentKey))
            {
                if (userParentKey.equals("Users"))
                {
                    allowAccessToUser(userPhoneKey,userPasswordKey);
                    progressDialog.setTitle("Already Logged in");
                    progressDialog.setMessage("Please wait, while we are checking the credentials");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
                else
                {
                    allowAccessToAdmins(userPhoneKey,userPasswordKey);
                    progressDialog.setTitle("Already Logged in");
                    progressDialog.setMessage("Please wait, while we are checking the credentials");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }

            }
        }

    }

    private void allowAccessToAdmins(final String userPhoneKey,final String userPasswordKey)
    {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Admins").child(userPhoneKey).exists())
                {
                    Users usersData = dataSnapshot.child("Admins").child(userPhoneKey).getValue(Users.class);
                    if (usersData.getPhone().equals(userPhoneKey))
                    {
                        if (usersData.getPassword().equals(userPasswordKey))
                        {
                            String userName = usersData.getName();
                            Toast.makeText(MainActivity.this, "Welcome "+userName, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, AdminAddNewProductActivity.class));
                            Prevalent.currentLogedInUser= usersData;
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Kindly provide right phone number and password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User don't exists!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Please try again or creat new account.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void allowAccessToUser(final String userPhoneKey,final String userPasswordKey)
    {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(userPhoneKey).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(userPhoneKey).getValue(Users.class);
                    if (usersData.getPhone().equals(userPhoneKey))
                    {
                        if (usersData.getPassword().equals(userPasswordKey))
                        {
                            String userName = usersData.getName();
                            Toast.makeText(MainActivity.this, "Welcome "+userName, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            Prevalent.currentLogedInUser = usersData;
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Kindly provide right phone number and password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User don't exists!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Please try again or creat new account.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
