package com.example.ex.bakingapp.utils;


import com.example.ex.bakingapp.R;
import com.example.ex.bakingapp.models.Ingredient;
import com.example.ex.bakingapp.models.Recipe;
import com.example.ex.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JsonUtils {

    public JsonUtils() {
    }
    public static List<Recipe> getAllRecipes(JSONArray result){
        List<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps=new ArrayList<>();
        for (int i = 0; i < result.length();i++){
            try {

                JSONObject recipe = result.getJSONObject(i);
                Recipe recipeModel = new Recipe();
                recipeModel.setId(recipe.getInt("id"));
                recipeModel.setName(recipe.getString("name"));
                JSONArray ingredientsJsonArray = recipe.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsJsonArray.length();j++){
                    JSONObject ingredient = ingredientsJsonArray.getJSONObject(j);
                    Ingredient ingredientModel = new Ingredient();
                    ingredientModel.setQuantity(ingredient.getDouble("quantity"));
                    ingredientModel.setMeasure(ingredient.getString("measure"));
                    ingredientModel.setIngredient(ingredient.getString("ingredient"));
                    ingredients.add(ingredientModel);
                }
                recipeModel.setIngredients(ingredients);
                JSONArray stepsJsonArray = recipe.getJSONArray("steps");
                for (int k = 0; k < stepsJsonArray.length();k++){
                    JSONObject step = stepsJsonArray.getJSONObject(k);
                    Step stepModel = new Step();
                    stepModel.setId(step.getInt("id"));
                    stepModel.setShortDescription(step.getString("shortDescription"));
                    stepModel.setDescription(step.getString("description"));
                    stepModel.setVideoURL(step.getString("videoURL"));
                    stepModel.setThumbnailURL(step.getString("thumbnailURL"));
                    steps.add(stepModel);
                }
                recipeModel.setSteps(steps);
                recipeModel.setServings(recipe.getInt("servings"));
                switch (i){
                    case 0:
                        recipeModel.setImage(R.drawable.nutellapie);
                        break;
                    case 1:
                        recipeModel.setImage(R.drawable.brownies);
                        break;
                    case 2:
                        recipeModel.setImage(R.drawable.yellowcake);
                        break;
                    default:
                        recipeModel.setImage(R.drawable.cheesecake);
                }
                recipes.add(recipeModel);
                steps = new ArrayList<>();
                ingredients=new ArrayList<>();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }

}
