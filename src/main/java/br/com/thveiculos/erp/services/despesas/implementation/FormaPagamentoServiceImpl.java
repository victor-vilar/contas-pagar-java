package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public FormaPagamento save(FormaPagamento obj) throws ConstraintViolationException, DataIntegrityViolationException {
			
			if(obj.getId()!= null) {
				return update(obj);
			}else {
				return repository.save(obj);
			}
	}
	
	@Override
	public FormaPagamento update(FormaPagamento obj) {
		
		FormaPagamento toUpdate;
		Optional<FormaPagamento> saved = this.repository.findById(obj.getId());
		
		if(saved.isPresent()) {
			toUpdate = saved.get();
			toUpdate.setForma(obj.getForma());
			return repository.save(toUpdate);
		}

		
		return null;
	}
	@Override
	public FormaPagamento saveAll(List<FormaPagamento> objs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void deleteById(Long id) {
		this.repository.deleteById(id);
		
	}

	@Override
	public void deleteAll(List<FormaPagamento> objs) {
		throw new UnsupportedOperationException("Not yet implemented");
		
	}

}
