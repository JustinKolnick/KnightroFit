package com.justinkolnick.fitnessapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    String userId;
    private FirebaseDatabase mDb = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = mDb.getReference();

    private ToggleButton showWeightb;
    private ToggleButton showAgeb;
    private ToggleButton trackProgressb;
    private ToggleButton pushNotifyb;
    private ToggleButton userVisibilityb;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Adds back the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            userId = user.getUid();
        }

        showWeightb = (ToggleButton) findViewById(R.id.WeightDisplay);
        showWeightb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        mDatabase.child("Users").child(userId).child("Settings").child("showWeight").setValue(true);
                } else {
                        mDatabase.child("Users").child(userId).child("Settings").child("showWeight").setValue(false);
                }
            }
        });

        showAgeb = (ToggleButton) findViewById(R.id.AgeDisplay);
        showAgeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDatabase.child("Users").child(userId).child("Settings").child("showAge").setValue(true);
                } else {
                    mDatabase.child("Users").child(userId).child("Settings").child("showAge").setValue(false);
                }
            }
        });

        trackProgressb = (ToggleButton) findViewById(R.id.TrackProgress);
        trackProgressb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDatabase.child("Users").child(userId).child("Settings").child("trackProgress").setValue(true);
                } else {
                    mDatabase.child("Users").child(userId).child("Settings").child("trackProgress").setValue(false);
                }
            }
        });

        pushNotifyb = (ToggleButton) findViewById(R.id.pushNotifications);
        pushNotifyb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDatabase.child("Users").child(userId).child("Settings").child("pushNotify").setValue(true);
                } else {
                    mDatabase.child("Users").child(userId).child("Settings").child("pushNotify").setValue(false);
                }
            }
        });


        userVisibilityb = (ToggleButton) findViewById(R.id.UserVisible);
        userVisibilityb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDatabase.child("Users").child(userId).child("Settings").child("userVisibility").setValue(true);
                } else {
                    mDatabase.child("Users").child(userId).child("Settings").child("userVisibility").setValue(false);
                }
            }
        });

        DatabaseReference ref = mDb.getReference("Users/"+userId+"/Settings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saveData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void saveData(DataSnapshot dataSnapshot) {
        showWeightb.setChecked(Boolean.valueOf(dataSnapshot.child("showWeight").getValue().toString()));
        showAgeb.setChecked(Boolean.valueOf(dataSnapshot.child("showAge").getValue().toString()));
        trackProgressb.setChecked(Boolean.valueOf(dataSnapshot.child("trackProgress").getValue().toString()));
        pushNotifyb.setChecked(Boolean.valueOf(dataSnapshot.child("pushNotify").getValue().toString()));
        userVisibilityb.setChecked(Boolean.valueOf(dataSnapshot.child("userVisibility").getValue().toString()));
    }

    // Adds back the back button
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
