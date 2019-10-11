package com.example.ex.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex.bakingapp.models.Ingredient;
import com.example.ex.bakingapp.models.Recipe;
import com.example.ex.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static List<Step> steps;
    public static List<Ingredient> ingredients;
    public static Recipe recipe;

    @BindView(R.id.recipe_iv)ImageView recipeImageView;
    @BindView(R.id.recipeNameDetails_tv)TextView recipeName;
    @BindView(R.id.recipeIngredientsDetails_tv)TextView recipeIngredient;
    @BindView(R.id.recipeStepsDetails_tv)TextView recipeSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        recipe = getIntent().getParcelableExtra("selectedRecipe");
        MainActivity.id = recipe.getId();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,IngredientWidget.class));
        if (appWidgetIds.length>0) {
            IngredientWidget.updateAppWidget(this, appWidgetManager, appWidgetIds[0]);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        }
        steps = recipe.getSteps();
        ingredients = recipe.getIngredients();
        recipeName.setText(recipe.getName());
        recipeImageView.setImageResource(recipe.getImage());
        recipeImageView.setTag(recipe.getImage());
        recipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this,IngredientActivity.class);
                startActivity(intent);
            }
        });
        recipeSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this,StepsActivity.class);
                startActivity(intent);
            }
        });
    }
}
