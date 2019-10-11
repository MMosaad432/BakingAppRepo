package com.example.ex.bakingapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ex.bakingapp.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsDetailsAdapter extends RecyclerView.Adapter<RecipeStepsDetailsAdapter.RecipeStepsRecyclerViewViewHolder> {
    StepsActivity context;

    public RecipeStepsDetailsAdapter(StepsActivity context) {
        this.context = context;
    }

    @Override
    public RecipeStepsRecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view;
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.recipes_steps_master_layout, parent, false);
        return new RecipeStepsRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeStepsRecyclerViewViewHolder holder, final int position) {
        Step step = RecipeDetailsActivity.steps.get(position);
        holder.recipeStepShortDescriptionTextView.setText(step.getShortDescription());
        holder.recipeStepShortDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context.findViewById(R.id.tabletStepsView)!=null){
                    context.setClickedId(position);
                }else {
                Intent intent = new Intent(context,StepDetailsActivity.class);
                intent.putExtra("stepId",position);
                context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {

        return RecipeDetailsActivity.steps.size();
    }

    class RecipeStepsRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipeStepShortDescription_tv)
        TextView recipeStepShortDescriptionTextView;
        public RecipeStepsRecyclerViewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}