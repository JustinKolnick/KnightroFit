package com.justinkolnick.fitnessapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Workout {
    public String uid;
    public String name;
    public String target;
    public Map<String, Exercise> exercises;
    public Date dateCompleted;

    public void Workout(String uid, String name, String target, Map<String, Exercise> exercises) {
        this.uid = uid;
        this.name = name;
        this.target = target;
        this.exercises = exercises;
    }
    public void Workout(String name, String target, Map<String, Exercise> exercises, Date dateCompleted) {
        this.uid = uid;
        this.name = name;
        this.target = target;
        this.exercises = exercises;
        this.dateCompleted = dateCompleted;
    }
}
