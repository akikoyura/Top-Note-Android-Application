package com.e.topnoteapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnSignUp;
    private FirebaseAuth mAuth=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.login);
        btnSignUp  = findViewById(R.id.signUp);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }
    public void Login()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.reg_log_input_layout,null);
        mydialog.setView(myView);
        final EditText email=myView.findViewById(R.id.email_login);
        final EditText pass = myView.findViewById(R.id.password_login);
        Button btnLogin = myView.findViewById(R.id.login_btn);
        final AlertDialog dialog = mydialog.create();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = pass.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail))
                {
                    email.setError("Email Required...");
                }
                if(TextUtils.isEmpty(mPassword))
                {
                    pass.setError("Password Required...");
                }
                mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Login done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();

    }
    public void SignUp()
    {
        final AlertDialog.Builder mydialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.signup_layout,null);
        mydialog.setView(myView);
        final EditText email = myView.findViewById(R.id.email_signup);
        final EditText password = myView.findViewById(R.id.password_signup);
        Button btnSignUp = myView.findViewById(R.id.signup_btn);
        final AlertDialog dialog = mydialog.create();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail))
                {
                    email.setError("Email Required....");
                }
                if(TextUtils.isEmpty(mPassword))
                {
                    password.setError("Password Required...");
                }
                mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }


}
