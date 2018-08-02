package com.justinkolnick.fitnessapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Exercise {
    public String name;
    public String target;
    public Map<String, Benchmark> benchmarks;

    public void Workout(String name, String target, Map<String, Benchmark> benchmarks) {
        this.name = name;
        this.target = target;
        this.benchmarks = benchmarks;
    }
}
