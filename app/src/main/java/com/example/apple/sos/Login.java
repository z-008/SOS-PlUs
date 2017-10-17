package com.example.apple.sos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button register;
    private Button login;
    private EditText editTextusername;
    private EditText editTextpassword;
    private TextView textViewforgotpassword;
    static FirebaseAuth firebaseauth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog =new ProgressDialog(this);
        firebaseauth = FirebaseAuth.getInstance();



        if(firebaseauth.getCurrentUser()!=null)
        {  //finish();
           startActivity(new Intent(getApplicationContext(),Account.class));
        }

        register = (Button)findViewById(R.id.register);
        login =(Button)findViewById(R.id.Login);
        editTextusername = (EditText)findViewById(R.id.edittextusername);
        editTextpassword = (EditText)findViewById(R.id.edittextpassword);
        textViewforgotpassword = (TextView)findViewById(R.id.textviewforgotpassword);
        login.setOnClickListener(this);
        textViewforgotpassword.setOnClickListener(this);


    }


    public void loginUser()
    { String email = editTextusername.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<8)
        {
            Toast.makeText(this, "Length should be atleast 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        firebaseauth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            //User is registered successfully
                            Toast.makeText(Login.this, "User logged in successfully!!", Toast.LENGTH_SHORT).show();
                           // finish();
                            startActivity(new Intent(Login.this, Account.class));
                        }

                        else {

                            new AlertDialog.Builder(Login.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Alert!")
                                    .setMessage("Incorrect Email/Password or both!!")
                                    .setPositiveButton("Ok",null)
                                    .setNegativeButton(null,null)
                                    .show();





                            //Toast.makeText(Login.this, "Incorrect Email/Password or both!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    @Override
    public void onClick(View view) {
        if(view==login)
        {
            loginUser();
        }

        else if(view==textViewforgotpassword)
        {
            Toast.makeText(this, "Reset Link has been sent to your mail id", Toast.LENGTH_SHORT).show();
        }

    }
}
