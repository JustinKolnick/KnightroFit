package com.justinkolnick.fitnessapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


import android.widget.Toast;
import com.squareup.timessquare.CalendarPickerView;
import java.util.Calendar;
import java.util.Date;


public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Adds back the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Data");
        graph.getLegendRenderer().setVisible(false);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter());

        // Get a reference to the current user logged in
        String userId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            userId = user.getUid();
        }
        // Get an instance of the current firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get a reference for the user that is logged into the database
        DatabaseReference ref = database.getReference("Users/"+userId+"/measurements/weight");

        // Attach a listener to read the data at our database reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop to go through the children and get their values from that database
                // so we can populate the graph with them
                Integer i = 1;
                BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    series.appendData(new DataPoint(i, Integer.parseInt(ds.getValue().toString())), false, i);
                    i++;
                }
                graph.addSeries(series);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        // set a calendar day to be used as the first day that will displayed on our calendar view
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 7, 01);
        Date min = cal.getTime();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView datePicker = findViewById(R.id.calendar);
        datePicker.init(min, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(min);


        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);

                Toast.makeText(GraphActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    // Adds back the back button
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}