package br.com.victorvilar.contaspagar.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.repositories.CategoriaDespesaRepository;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;

@Service
@Lazy
public class CategoriaDespesaServiceImpl implements CategoriaDespesaService {

    private CategoriaDespesaRepository repository;

    @Autowired
    public CategoriaDespesaServiceImpl(CategoriaDespesaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoriaDespesa> getTodos() {
        return repository.findAll();
    }

    @Override
    public CategoriaDespesa getById(Long id) {
        return null;
    }

    @Override
    public CategoriaDespesa save(CategoriaDespesa obj) {

        if (obj.getId() != null) {
            return update(obj);
        } else {
            return repository.save(obj);
        }
    }

    @Override
    public CategoriaDespesa update(CategoriaDespesa obj) {

        CategoriaDespesa toUpdate;
        Optional<CategoriaDespesa> saved = this.repository.findById(obj.getId());

        if (saved.isPresent()) {
            toUpdate = saved.get();
            toUpdate.setCategoria(obj.getCategoria());
            return repository.save(toUpdate);
        }

        return null;
    }

    @Override
    public List<CategoriaDespesa> saveAll(List<CategoriaDespesa> objs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);

    }

    @Override
    public void deleteAll(List<CategoriaDespesa> objs) {
        // TODO Auto-generated method stub

    }

    @Override
    public CategoriaDespesa getByCategoria(String categoria) {
        return repository.getByCategoria(categoria);
    }

}
