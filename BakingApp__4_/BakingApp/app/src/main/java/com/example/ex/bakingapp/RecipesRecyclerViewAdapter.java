package com.example.ex.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ex.bakingapp.models.Recipe;
import com.example.ex.bakingapp.utils.JsonDownload;
import com.example.ex.bakingapp.utils.JsonUtils;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesRecyclerViewAdapter extends Adapter<RecipesRecyclerViewAdapter.RecipeRecyclerViewAdapterViewHolder>   {
    private final String downloadURL= "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private  JsonDownload jsonDownload;
    private JSONArray jsonArray;
    public static List<Recipe> recipes;
    private final RecipesRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler mRecyclerViewAdapterOnClickHandler;

    public RecipesRecyclerViewAdapter(RecipesRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler recyclerViewAdapterOnClickHandler) {
        this.mRecyclerViewAdapterOnClickHandler = recyclerViewAdapterOnClickHandler;



        this.jsonDownload = new JsonDownload();
        if (this.mRecyclerViewAdapterOnClickHandler==null){
            recipes = null;
        }else {
            try {
                recipes = JsonUtils.getAllRecipes(this.jsonDownload.execute(downloadURL).get());
            } catch (ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public RecipeRecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.recipes;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new RecipeRecyclerViewAdapterViewHolder(view);
    }

    public void onBindViewHolder(final RecipeRecyclerViewAdapterViewHolder holder, int position) {
        if (getItemCount()==1 && (recipes == null || recipes.isEmpty())){
            holder.recipeIngredientNumberTextView.setVisibility(View.GONE);
            holder.recipeStepsNumberTextView.setVisibility(View.GONE);
            holder.recipeServingNumberTextView.setVisibility(View.GONE);
            holder.recipeNameTextView.setText("No recipes found.");
        }else {
            Recipe recipe = recipes.get(position);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.recipeIngredientNumberTextView.setVisibility(View.VISIBLE);
            holder.recipeStepsNumberTextView.setVisibility(View.VISIBLE);
            holder.recipeServingNumberTextView.setVisibility(View.VISIBLE);
            holder.recipeNameTextView.setText(recipe.getName());
            holder.recipeIngredientNumberTextView.setText(String.valueOf(recipe.getIngredients().size()));
            holder.recipeStepsNumberTextView.setText(String.valueOf(recipe.getSteps().size()));
            holder.recipeServingNumberTextView.setText(String.valueOf(recipe.getServings()));
        }
    }

    public int getItemCount() {
        if (recipes==null||recipes.isEmpty()){
            return 1;
        }else return recipes.size();

    }


    public class RecipeRecyclerViewAdapterViewHolder extends ViewHolder implements OnClickListener {

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.recipeName_tv) TextView recipeNameTextView;
        @BindView(R.id.recipeIngredientsNumber_tv) TextView recipeIngredientNumberTextView;
        @BindView(R.id.recipeStepsNumber_tv) TextView recipeStepsNumberTextView;
        @BindView(R.id.recipeServingNumber_tv) TextView recipeServingNumberTextView;
        public RecipeRecyclerViewAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);

        }

        public void onClick(View view) {
            if (recipes!=null){
            int clickedAdapter = this.getAdapterPosition();
            mRecyclerViewAdapterOnClickHandler.onClickListener(clickedAdapter);
        }
        }
    }

    public interface RecyclerViewAdapterOnClickHandler {
        void onClickListener(int var1);
    }
}
