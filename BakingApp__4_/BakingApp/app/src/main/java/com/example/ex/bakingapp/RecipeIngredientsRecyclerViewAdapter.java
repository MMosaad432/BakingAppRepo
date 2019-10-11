package com.example.ex.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ex.bakingapp.models.Ingredient;
import com.example.ex.bakingapp.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeIngredientsRecyclerViewAdapter.RecipeIngredientsRecyclerViewViewHolder> {


    public RecipeIngredientsRecyclerViewAdapter() {
    }

    @Override
    public RecipeIngredientsRecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view;
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.ingredients, parent, false);
        return new RecipeIngredientsRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeIngredientsRecyclerViewViewHolder holder, final int position) {
        Ingredient ingredient = RecipeDetailsActivity.ingredients.get(position);
        holder.recipeIngredient.setText(String.valueOf(ingredient.getQuantity())+" "+ingredient.getMeasure()+"\n"+ingredient.getIngredient());

    }


    @Override
    public int getItemCount() {

        return RecipeDetailsActivity.ingredients.size();
    }

    class RecipeIngredientsRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredients_tv)
        TextView recipeIngredient;
        public RecipeIngredientsRecyclerViewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}