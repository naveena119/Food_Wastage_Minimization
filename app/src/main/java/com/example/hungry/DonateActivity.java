package com.example.hungry;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DonateActivity extends AppCompatActivity {


    private EditText uname, fname, quantity,expdate, number;
    private DatabaseReference reff;
    private donateDetails member;
    private long maxid = 0;
    private Double lat,lng;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        uname = (EditText) findViewById(R.id.dname);
        fname = (EditText) findViewById(R.id.dfoodname);
        quantity = (EditText) findViewById(R.id.dquantity);
        expdate = (EditText) findViewById(R.id.dexp_date);
        number = (EditText) findViewById(R.id.dphnum);

        Button btn = (Button) findViewById(R.id.dsubmit);

        member = new donateDetails();

        reff = FirebaseDatabase.getInstance().getReference().child("Child");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String un, fn, qt,ed, nm;

                un = uname.getText().toString().trim();
                fn = fname.getText().toString().trim();
                qt = quantity.getText().toString().trim();
                ed = expdate.getText().toString().trim();
                nm = number.getText().toString().trim();

                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(fn) || TextUtils.isEmpty(qt) || TextUtils.isEmpty(nm)) {
                    Toast.makeText(DonateActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                }
                else {


                    FoodLocation foodLocation = new FoodLocation(getApplicationContext());

                    Location l = foodLocation.getLocation();

                    //if (l != null) {
                        lat = l.getLatitude();
                        lng = l.getLongitude();



                        member.setUserName(un);
                        member.setFoodName(fn);
                        member.setQuantity(qt);
                        member.setExpDate(ed);
                        member.setNumber(nm);
                        member.setX(lat);
                        member.setY(lng);

                        reff.child(String.valueOf(maxid + 1)).push().setValue(member);

                        Toast.makeText(DonateActivity.this, "Donated", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DonateActivity.this, SecondActivity.class);
                        startActivity(intent);
                        finish();
                   // }
                }
            }
        });
    }

}

