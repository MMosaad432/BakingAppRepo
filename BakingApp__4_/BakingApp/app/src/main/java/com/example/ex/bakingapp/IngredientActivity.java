package com.example.ex.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientActivity extends AppCompatActivity {

    @BindView(R.id.recipeIngredients_rv)
    RecyclerView recipeIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipeIngredients.setLayoutManager(layoutManager);
        recipeIngredients.setHasFixedSize(true);
        RecipeIngredientsRecyclerViewAdapter recipeIngredientsRecyclerViewAdapter = new RecipeIngredientsRecyclerViewAdapter();
        recipeIngredients.setAdapter(recipeIngredientsRecyclerViewAdapter);

    }
}
