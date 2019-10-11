package com.example.ex.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        if(savedInstanceState == null){
        int stepId = getIntent().getIntExtra("stepId",-1);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment(stepId);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.stepDetails_container, stepDetailsFragment)
                .commit();
        }else {

        }
    }
}
