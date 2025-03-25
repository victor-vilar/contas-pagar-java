package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.repositories.despesas.MovimentoPagamentoRepository;
import br.com.thveiculos.erp.services.despesas.interfaces.MovimentoPagamentoService;

@Service
public class MovimentoPagamentoServiceImpl implements MovimentoPagamentoService{

	private MovimentoPagamentoRepository repository;
	
	@Autowired
	public MovimentoPagamentoServiceImpl(MovimentoPagamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<MovimentoPagamento> getTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovimentoPagamento getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovimentoPagamento save(MovimentoPagamento obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovimentoPagamento saveAll(List<MovimentoPagamento> objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(List<MovimentoPagamento> objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MovimentoPagamento update(MovimentoPagamento obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
