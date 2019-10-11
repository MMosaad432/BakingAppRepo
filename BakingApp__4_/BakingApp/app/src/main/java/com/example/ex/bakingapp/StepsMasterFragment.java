package com.example.ex.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Mosaad on 6/3/2018.
 */

public class StepsMasterFragment extends Fragment {
    View v;
    Unbinder unbinder;


    public StepsMasterFragment() {}

    @BindView(R.id.recipeSteps_rv)
    RecyclerView recipeStepsRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.recipes_steps_master_fragment,container,false);
        unbinder = ButterKnife.bind(this,v);
        recipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipeStepsRecyclerView.setHasFixedSize(true);
        RecipeStepsDetailsAdapter recipesRecyclerViewAdapter =new RecipeStepsDetailsAdapter((StepsActivity)getContext());
        recipeStepsRecyclerView.setAdapter(recipesRecyclerViewAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
