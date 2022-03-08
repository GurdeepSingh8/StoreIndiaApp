package com.example.otpverificationusingfirebase;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class SavedStateViewModel extends Product {

    private SavedStateHandle state;

    public SavedStateViewModel(SavedStateHandle savedStateHandle) {
        state = savedStateHandle;
    }


}