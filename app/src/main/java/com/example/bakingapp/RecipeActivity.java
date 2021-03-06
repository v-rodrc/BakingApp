package com.example.bakingapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.bakingapp.databinding.ActivityRecipeBinding;
import com.example.bakingapp.ui.stepdetailfragment.StepDetailFragment;
import com.example.bakingapp.ui.steplistfragment.StepListFragment;

public class RecipeActivity extends AppCompatActivity implements StepListFragment.OnStepClickListener {

    private static final String IS_PHONE_KEY = "isphone";
    private static final String IS_MASTER_FRONT_KEY = "ismasterfront";
    private Boolean isPhone;
    private Boolean isMasterFront;
    private ActivityRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up view binding
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        isPhone = true; // TODO figure out if the device is a phone or tablet

        // Set up the fragments
        StepListFragment stepListFragment = new StepListFragment();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.master_list_container, stepListFragment).commit();
        fragmentManager.beginTransaction().add(R.id.step_detail_container, stepDetailFragment).commit();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(IS_MASTER_FRONT_KEY)) {
                isMasterFront = savedInstanceState.getBoolean(IS_MASTER_FRONT_KEY);
            }
        }
        else {
            // we check if there is information from the MainActivity, and if so we assign the recipe index selected
            // we do this within this if-else statement because this should only be done the first time the activity is created.
            Intent intent = getIntent();
            if (intent.hasExtra("index")) {
                stepListFragment.chooseRecipeIndex(intent.getIntExtra("index", 0));
            }
            else {
                // TODO handle error situation
            }
        }

        // We set up a OnBackPressedCallback in order to switch back to the master list while still
        // remaining in the same activity
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // TODO add in code for handling back pressed event
                if (isPhone) {
                    // we only give a back pressed event special treatment on a phone
                    if (!isMasterFront) {
                        displayMasterList();
                    }
                }
                else {
                    // we defer to the normal back pressed event if the device is a tablet
                    setEnabled(false);
                    onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);

        // This activity will be set up differently for a phone and for a tablet
        if (isPhone) {
            // TODO set up the activity for a phone screen
            displayMasterList();
        }
        else {
            // TODO set up the activity for a tablet screen
        }
    }

    @Override
    public void onStepClicked() {
        // When a step in the recipe is clicked, the resulting action will vary whether the device is a phone or tablet
        if (isPhone) {
            displayDetails();
        }
    }

    void displayMasterList() {
        binding.masterListContainer.setVisibility(View.VISIBLE);
        binding.stepDetailContainer.setVisibility(View.INVISIBLE);
        isMasterFront = true;
    }

    void displayDetails() {
        binding.masterListContainer.setVisibility(View.INVISIBLE);
        binding.stepDetailContainer.setVisibility(View.VISIBLE);
        isMasterFront = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_MASTER_FRONT_KEY, isMasterFront);
    }
}