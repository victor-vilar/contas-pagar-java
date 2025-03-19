package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.repositories.despesas.DespesaRepository;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;

@Service
public class DespesaServiceImpl implements DespesaService{

	private DespesaRepository repository;
	
	@Autowired
	public DespesaServiceImpl(DespesaRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Despesa> getTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Despesa getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Despesa save(Despesa obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Despesa saveAll(List<Despesa> objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(List<Despesa> objs) {
		// TODO Auto-generated method stub
		
	}

}
