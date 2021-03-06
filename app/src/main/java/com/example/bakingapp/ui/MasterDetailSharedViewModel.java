package com.example.bakingapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.Recipe;
import com.example.bakingapp.managers.ContentManager;

public class MasterDetailSharedViewModel extends ViewModel {
    private int stepIndex = 0;
    private MutableLiveData<Recipe> currentRecipe = new MutableLiveData<>();
    private MutableLiveData<Recipe.Step> currentStep = new MutableLiveData<>();

    public void chooseCurrentRecipe(int index) {
        ContentManager contentManager = ContentManager.getInstance();
        contentManager.queryRecipeApi();
        currentRecipe.setValue(contentManager.getRecipes().getValue().get(index));
    }

    public void chooseCurrentStep(int index) {
        stepIndex = index;
        if (index == 0)
            currentStep.setValue(null);
        else {
            currentStep.setValue(currentRecipe.getValue().getSteps().get(index - 1));
        }
    }

    public void nextStep() {
        if (stepIndex < currentRecipe.getValue().getSteps().size() + 1) {
            stepIndex++;
            chooseCurrentStep(stepIndex);
        }
    }

    public void previousStep() {
        if (stepIndex > 0) {
            stepIndex--;
            chooseCurrentStep(stepIndex);
        }
    }

    public Boolean isLastStep() {
        if (stepIndex == currentRecipe.getValue().getSteps().size())
            return true;
        return false;
    }

    public MutableLiveData<Recipe> getCurrentRecipe() {
        return currentRecipe;
    }

    public MutableLiveData<Recipe.Step> getCurrentStep() {
        return currentStep;
    }
}
