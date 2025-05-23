package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.services.implementation.FormaPagamentoServiceImpl;
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

import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.repositories.FormaPagamentoRepository;

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
	void DeveSalvarOObjetoComSucessoQuandoNaoPossuiId() {

		FormaPagamento forma = new FormaPagamento();

		forma.setForma("teste");
		when(repository.save(any(FormaPagamento.class))).thenReturn(forma);
		FormaPagamento f = service.save(forma);
		Assertions.assertEquals(f.getForma(), forma.getForma());


	}
	
	@Test
	void DeveSalvarOObjetoComSucessoQuandoPossuiId() {
		
		FormaPagamento forma = new FormaPagamento();
		forma.setId(1L);
		forma.setForma("teste");
		when(repository.save(any(FormaPagamento.class))).thenReturn(forma);
		when(repository.findById(forma.getId())).thenReturn(Optional.of(forma));
		
		FormaPagamento f = service.save(forma);
		
		Assertions.assertEquals(f.getId(), forma.getId());
		Assertions.assertEquals(f.getForma(), forma.getForma());
		

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
		when(repository.save(any(FormaPagamento.class))).thenAnswer(c -> c.getArgument(0));
		
		FormaPagamento f3 = service.update(formaAtualizada);
		
		Assertions.assertEquals(f3.getId(),formaAtualizada.getId());
		Assertions.assertEquals(f3.getForma(),formaAtualizada.getForma());
		
	}

	
	
}
