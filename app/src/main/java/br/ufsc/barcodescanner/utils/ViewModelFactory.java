package br.ufsc.barcodescanner.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private BarcodeViewModel viewModel;

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null) {
            viewModel = new BarcodeViewModel();
        }
        if (modelClass.isAssignableFrom(BarcodeViewModel.class)) {
            return (T) viewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}