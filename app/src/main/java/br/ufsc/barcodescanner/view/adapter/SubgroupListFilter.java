package br.ufsc.barcodescanner.view.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.ufsc.barcodescanner.service.model.Subgroup;
import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;

public class SubgroupListFilter extends ListFilter<Subgroup> {

    private int groupId;

    public SubgroupListFilter(LocalDatabaseRepository repository) {
        super(repository);
    }

    @Override
    protected List<Subgroup> getValues(String filter) throws ExecutionException, InterruptedException {
        return this.repository.loadSubgroups(filter, groupId);
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


}
