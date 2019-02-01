package br.ufsc.barcodescanner.view.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;

public class GroupListFilter extends ListFilter<Group> {


    public GroupListFilter(LocalDatabaseRepository repository) {
        super(repository);
    }

    @Override
    protected List<Group> getValues(String filter) throws ExecutionException, InterruptedException {
        return this.repository.loadGroups(filter);
    }

}
