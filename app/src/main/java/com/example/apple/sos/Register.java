package com.example.apple.sos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {


    private Button register;
    private EditText editTextusername;
    private EditText editTextpassword;
    private EditText editName;
    private EditText editTextuid;
    private TextView textViewlogin;
    private FirebaseAuth firebaseauth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog =new ProgressDialog(this);
        firebaseauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        if(firebaseauth.getCurrentUser()!=null)
        {  finish();
            startActivity(new Intent(getApplicationContext(),Account.class));
        }

        register = (Button)findViewById(R.id.register);
        editTextusername = (EditText)findViewById(R.id.edittextusername);
        editTextpassword = (EditText)findViewById(R.id.edittextpassword);
        editName =(EditText)findViewById(R.id.editName);
        editTextuid = (EditText) findViewById(R.id.edittextuid);
        textViewlogin=(TextView)findViewById(R.id.textviewLogin);

        register.setOnClickListener(this);
        textViewlogin.setOnClickListener(this);
    }

    private void registerUser()
    {
        final String email = editTextusername.getText().toString().trim();
        final String password = editTextpassword.getText().toString().trim();
        final String name = editName.getText().toString().trim();
        final String uid = editTextuid.getText().toString().trim();


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

        progressDialog.setMessage("Registering User..");
        progressDialog.show();


        firebaseauth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {

                            //User is registered successfully
                           UserInfo userInfo =new UserInfo(name,email,uid);
                            FirebaseUser firebaseUser =firebaseauth.getCurrentUser();
                            databaseReference.child(firebaseUser.getUid()).setValue(userInfo); //Saving Info



                            Toast.makeText(Register.this, "User registered successfully!!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),Account.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Failed to register!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }


    @Override
    public void onClick(View view) {
     if(view==register)
     {
         registerUser();
     }

      if(view==textViewlogin)
     {
         finish();
         startActivity(new Intent(Register.this,Login.class));
     }
    }




}
