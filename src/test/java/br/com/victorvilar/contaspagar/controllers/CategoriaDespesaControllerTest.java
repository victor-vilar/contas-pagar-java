package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.CategoriaDespesaController;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.util.ControllerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;
import br.com.victorvilar.contaspagar.views.SimpleView;
import br.com.victorvilar.contaspagar.views.CategoriaDespesaView;

@ExtendWith(MockitoExtension.class)
class CategoriaDespesaControllerTest {

	@Spy
	@InjectMocks
	public CategoriaDespesaController controller;
	
	@Mock
	public CategoriaDespesaService service;

	@Mock
	public FormaPagamentoService formaPagamentoService;
	
	public SimpleView view;
	
	@BeforeEach
	void setUp() {
		view = new CategoriaDespesaView(controller);
	}
	
	
	@Test
	void metodoNovoDeveFazerCampoDeFormaPagamentoSerEditavel() {
        
        JTextField teste = view.getFieldNome();
        assertFalse(teste.isEditable());
        controller.novo();
        assertTrue(view.getFieldNome().isEditable());
	}
	
	@Test
	void metodoNaoDeveChamarMetodosSalvarOUAtualizarDoServiceSeOCampoEstiverVazio() {
		
		view.getFieldNome().setEditable(true);
		view.getFieldNome().setText("");
		
		controller.salvar();
		
		
		verify(service,times(0)).save(any(CategoriaDespesa.class));
		verify(service,times(0)).update(any(CategoriaDespesa.class));
		
	}
	
	@Test
	void metodoDeveChamarOmetodoSaveQuandoCampoDeNomeTiverPreenchidoMasNaoHouverId() {

		view.getFieldNome().setEditable(true);
		view.getFieldNome().setText("teste");
		
		controller.salvar();
		
		
		verify(service,times(1)).save(any(CategoriaDespesa.class));
		verify(controller,times(1)).updateView();
	}
	
	
	@Test
	void metodoDeletarNaoDeveChamarMetodoDeDeleteSeNenhumaLinhaForSelecionadaNaView() {

		view.getTable().clearSelection();
		
		controller.deletar();
		verify(service,times(0)).deleteById(any(Long.class));
	}
	
	@Test
	void metodoDeveChamarMetodoDeleteByIdDoServicePassandoOValorDaPrimeiraColunaDaLinhaSelecionada() {

		
		addRows();
		view.getTable().setRowSelectionInterval(0, 0);
		controller.deletar();
		verify(service,times(1)).deleteById(1L);
		
		addRows();
		view.getTable().setRowSelectionInterval(0,1);
		controller.deletar();
		verify(service,times(1)).deleteById(2L);
		
		addRows();
		view.getTable().setRowSelectionInterval(0, 2);
		controller.deletar();
		verify(service,times(1)).deleteById(3L);
	}
	
	//Metodo necessário pois apos o teste ser executado o numero de linhas é alterado.
	private void addRows() {
		DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
		ControllerHelper.limparTabela(model);
		model.addRow(new Object[] {1L,"Teste"});
		model.addRow(new Object[] {2L,"Teste"});
		model.addRow(new Object[] {3L,"Teste"});
		
	}
	
	@Test
	void metodoUpdateDeveChamarOsMetodosParaAtualizarAView() {

		view.getFieldNome().setEditable(true);
		
		controller.updateView();
		
		verify(controller,times(1)).atualizarTabela();
		verify(controller,times(1)).limparCampos();
		assertFalse(view.getFieldNome().isEditable());
	}
	
	@Test
	void metodoLimparCamposDeveRemoverTextoDosCamposDaView() {

		view.getFieldNome().setText("Text");
		view.getFieldId().setText("2");
		
		controller.limparCampos();
		
		assertTrue(view.getFieldNome().getText().equals(""));
		assertTrue(view.getFieldNome().getText().equals(""));
	}
	
	@Test
	void metodoAtualizarTabelaDeveAdiciionarOsItensDoBancoNaTabela() {
		List<CategoriaDespesa> formas = List.of(new CategoriaDespesa(),new CategoriaDespesa());
		formas.get(0).setId(1L);
		formas.get(0).setCategoria("Teste1");
		formas.get(0).setId(2L);
		formas.get(0).setCategoria("Teste2");
		
		when(service.getTodos()).thenReturn(formas);
		controller.atualizarTabela();
		
		Assertions.assertEquals(view.getTable().getRowCount(),2);
		
		
	}
	
}
