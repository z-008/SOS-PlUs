package com.example.apple.sos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Account extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button SendSOS;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      if(item.getItemId()==R.id.logout);
      {
          firebaseAuth.signOut();
          finish();
          startActivity(new Intent(getApplicationContext(),Register.class));
      }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        SendSOS = (Button)findViewById(R.id.SendSOS);
        firebaseAuth = FirebaseAuth.getInstance();

        SendSOS.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==SendSOS)
        {
            finish();
            startActivity(new Intent(Account.this, MapsActivity.class));
        }
    }
}
