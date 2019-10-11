package com.example.ex.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ex.bakingapp.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler{

    public static int id = 1;
    @BindView(R.id.recipe_rv)
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (findViewById(R.id.tabletView)!=null){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

        }
        RecipesRecyclerViewAdapter recipesRecyclerViewAdapter;
        recyclerView.setHasFixedSize(true);
        if (isNetworkAvailable()) {
            recipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(this);
        }else {
            recipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(null);
        }
        recyclerView.setAdapter(recipesRecyclerViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        RecipesRecyclerViewAdapter recipesRecyclerViewAdapter;
        recyclerView.setHasFixedSize(true);
        if (isNetworkAvailable()) {
            recipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(this);
        }else {
            recipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(null);
        }
        recyclerView.setAdapter(recipesRecyclerViewAdapter);

    }

    @Override
    public void onClickListener(int var1) {
        Recipe recipe = RecipesRecyclerViewAdapter.recipes.get(var1);
        Intent intent = new Intent(this,RecipeDetailsActivity.class);
        intent.putExtra("selectedRecipe",recipe);
        startActivity(intent);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
