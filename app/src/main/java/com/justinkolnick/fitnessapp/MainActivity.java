package com.justinkolnick.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseListOptions.Builder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Firebase Auth Stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    public static final int RC_SIGN_IN = 1;

    // Database References
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
    private DatabaseReference mWorkoutsListReference = mFirebaseDatabase.getReference("/Workouts/");


    // Stuff on the page
    private ListView mListView;
    private ArrayList<String> workouts;

    // START HERE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize FirebaseAuth components
        mAuth = FirebaseAuth.getInstance();


        mListView = findViewById(R.id.workoutsList);


        // HANDLES AUTH
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // If user signed in ...
                if (user != null) {
                    onSignedInInitialize(user.getUid(), user.getDisplayName());
                    System.out.println("Signed In!");
                }

                // Otherwise, show the AuthUI
                else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        // Finds the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workouts = new ArrayList<String>();
        String uid = mAuth.getUid();
        System.out.println(uid);


        // Gets workout list
        mListView = (ListView) findViewById(R.id.workoutsList);
        ArrayList<String> w = new ArrayList<String>();
        w.add("Chest Day");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, w);
//
        mListView.setAdapter(itemsAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                DatabaseReference mD = itemsAdapter.getRef();
//                // When clicked perform some action...
                Intent i = new Intent(getApplicationContext(), StartWorkout.class);
//                Bundle b = new Bundle();
//                b.putLong("id", id);
//                i.putExtras(b);
                startActivity(i);
            }
        });


        // Gets reference and sets listener for floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddWorkout.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                return true;
            case R.id.action_graph:
                startActivity(new Intent(getApplicationContext(), GraphActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mListener);
    }

    private void onSignedInInitialize(String uid, String name) {
        DatabaseReference d = mDatabaseReference.child("Users").child(uid).child("Name");
        d.setValue(name);
    }

    private void onSignedOutCleanup() {
        ;
    }
}
