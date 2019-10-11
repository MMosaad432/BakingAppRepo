package com.example.ex.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StepsActivity extends AppCompatActivity {

    private static int stepId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (findViewById(R.id.tabletStepsView)!=null){
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment(stepId);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.stepDetails_container, stepDetailsFragment)
                    .commit();
        }
    }
    public  void setClickedId(int id){
        stepId = id;
        fragmentUpdate();
    }
    private void fragmentUpdate(){
        if (findViewById(R.id.tabletStepsView)!=null){
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment(stepId);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.stepDetails_container, stepDetailsFragment)
                    .commit();
        }
    }
}
