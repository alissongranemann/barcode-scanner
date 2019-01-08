package br.ufsc.barcodescanner.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private BarcodeRepository repository;
    private BarcodeViewModel viewModel;

    public ViewModelFactory(BarcodeRepository dataSource) {
        this.repository = dataSource;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null) {
            viewModel = new BarcodeViewModel(repository);
        }
        if (modelClass.isAssignableFrom(BarcodeViewModel.class)) {
            return (T) viewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}