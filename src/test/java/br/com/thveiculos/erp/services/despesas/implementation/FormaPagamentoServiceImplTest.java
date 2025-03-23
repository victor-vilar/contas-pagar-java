package br.com.thveiculos.erp.services.despesas.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.repositories.despesas.FormaPagamentoRepository;

@ExtendWith(MockitoExtension.class)
class FormaPagamentoServiceImplTest {

	@InjectMocks
	private FormaPagamentoServiceImpl service;
	
	@Mock
	private FormaPagamentoRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	
	@Test
	void DeveSalvarOObjetoComSucesso() {
		
		FormaPagamento forma = new FormaPagamento();
		forma.setId(1L);
		forma.setForma("teste");
		when(repository.save(any(FormaPagamento.class))).thenReturn(forma);
		
		FormaPagamento f = service.save(forma);
		
		Assertions.assertEquals(f.getId(), forma.getId());
		Assertions.assertEquals(f.getId(), forma.getId());
		

	}

	@Test
	void deveLançarExceptionSeViolarOConstraitDoBanco() {
		
		when(repository.save(any(FormaPagamento.class))).thenThrow(new DataIntegrityViolationException("Já existe uma forma de pagamento com esse nome;"));
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> service.save(new FormaPagamento()));

		
	}
	
	@Test
	void deveRetornarNullSeNaoEncontrarUmObjetoQuandoForAtualizar() {
		
		when(repository.findById(null)).thenReturn(Optional.empty());
		FormaPagamento f = service.update(new FormaPagamento());
		
		Assertions.assertNull(f);
		
	}
		
	@Test
	void deveAtualizarOObjetoDOBanco() {
		FormaPagamento formaSalva = new FormaPagamento();
		FormaPagamento formaAtualizada = new FormaPagamento();
		formaSalva.setId(1l);
		formaAtualizada.setId(1l);
		formaSalva.setForma("teste1");
		formaAtualizada.setForma("teste2");
		
		when(repository.findById(1L)).thenReturn(Optional.of(formaSalva));
		
		FormaPagamento f3 = service.update(formaAtualizada);
		
		Assertions.assertEquals(f3.getId(),formaAtualizada.getId());
		Assertions.assertEquals(f3.getForma(),formaAtualizada.getForma());
		
	}

	
	
}
