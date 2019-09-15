package com.example.clobberrage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber,InputPassword;
    private TextView txtAdmin,txtNotAdmin,forgetPasswordLink;
    private Button logInBtn;
    private CheckBox rememberMeChk;
    private ProgressDialog progressDialog;
    private String parentDbName ="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputPhoneNumber = findViewById(R.id.login_phone_number);
        InputPassword = findViewById(R.id.login_Password);
        logInBtn = findViewById(R.id.login_btn);
        rememberMeChk = findViewById(R.id.remember_me_chk);
        txtAdmin= findViewById(R.id.admin_panel_link);
        txtNotAdmin=findViewById(R.id.not_admin_panel_link);
        forgetPasswordLink = findViewById(R.id.forget_password_link);

        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });

        Paper.init(this);
        progressDialog = new ProgressDialog(this);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInBtn.setText("Login Admin");
                txtNotAdmin.setVisibility(View.VISIBLE);
                txtAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
                Toast.makeText(LoginActivity.this, "You're now logging in with admin Account.", Toast.LENGTH_SHORT).show();
            }
        });
        txtNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInBtn.setText("Log in");
                txtAdmin.setVisibility(View.VISIBLE);
                txtNotAdmin.setVisibility(View.INVISIBLE);
                parentDbName ="Users";
            }
        });


    }

    private void loginUser()
    {
        String phone_number = InputPhoneNumber.getText().toString().trim();
        String Password = InputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone_number))
        {
            InputPhoneNumber.setError("empty!");
            return;
        }
        else if (TextUtils.isEmpty(Password))
        {
            InputPassword.setError("empty!");
            return;
        }
        else
        {
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait, while we are checking the Account Info");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            allowAccessToAccount(phone_number,Password);
        }
    }

    private void allowAccessToAccount(final String phone_number, final String password)
    {
        if (rememberMeChk.isChecked())
        {
            Paper.book().write(Prevalent.userPnoneKey,phone_number);
            Paper.book().write(Prevalent.userPasswordKey,password);
            Paper.book().write(Prevalent.userParentDbNameKey,parentDbName);
        }

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone_number).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone_number).getValue(Users.class);
                    if (usersData.getPhone().equals(phone_number))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                           if (parentDbName.equals("Admins"))
                           {
                               String userName = usersData.getName();
                               Toast.makeText(LoginActivity.this, "Welcome "+userName, Toast.LENGTH_SHORT).show();
                               progressDialog.dismiss();
                               startActivity(new Intent(LoginActivity.this, AdminAddNewProductActivity.class));
                               finish();
                           }
                           else if (parentDbName.equals("Users"))
                           {
                               String userName = usersData.getName();
                               Toast.makeText(LoginActivity.this, "Welcome "+userName, Toast.LENGTH_SHORT).show();
                               progressDialog.dismiss();
                               startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                               Prevalent.currentLogedInUser = usersData;
                               finish();
                           }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Kindly provide right phone number and password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User don't exists!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Please try again or creat new account.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
