package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.repositories.despesas.FormaPagamentoRepository;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;

@Service
public class FormaPagamentoServiceImpl implements FormaPagamentoService{

	private FormaPagamentoRepository repository;
	
	@Autowired
	public FormaPagamentoServiceImpl(FormaPagamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<FormaPagamento> getTodos() {
		return repository.findAll();
	}

	@Override
	public FormaPagamento getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormaPagamento save(FormaPagamento obj) {
			return repository.save(obj);
	}

	@Override
	public FormaPagamento saveAll(List<FormaPagamento> objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(List<FormaPagamento> objs) {
		// TODO Auto-generated method stub
		
	}

}
