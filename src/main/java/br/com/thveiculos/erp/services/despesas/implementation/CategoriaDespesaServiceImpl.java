package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;
import br.com.thveiculos.erp.repositories.despesas.CategoriaDespesaRepository;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;

@Service
public class CategoriaDespesaServiceImpl implements CategoriaDespesaService {

	
	private CategoriaDespesaRepository repository;
	
	@Autowired
	public CategoriaDespesaServiceImpl (CategoriaDespesaRepository repositoy) {
		this.repository = repository; 
	}
	
	@Override
	public List<CategoriaDespesa> getTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoriaDespesa getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoriaDespesa save(CategoriaDespesa obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoriaDespesa saveAll(List<CategoriaDespesa> objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(List<CategoriaDespesa> objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CategoriaDespesa update(CategoriaDespesa obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
