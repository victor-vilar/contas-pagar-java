/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;
import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.views.despesas.DespesaAvulsaView;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class DespesaViewControllerTest {

    @Spy
    @InjectMocks
    private DespesaViewController controller;

    @Mock
    private CategoriaDespesaService categoriaDespesaService;

    @Mock
    private FormaPagamentoService formaPagamentoService;

    @Mock
    private DespesaService despesaService;

    private DespesaAvulsaView view;
    CategoriaDespesa cd1;
    CategoriaDespesa cd2;

    FormaPagamento fp1;
    FormaPagamento fp2;

    @BeforeEach
    public void setUp() {
        cd1 = new CategoriaDespesa();
        cd1.setId(1L);
        cd1.setCategoria("MANUTENÇÃO");

        cd2 = new CategoriaDespesa();
        cd2.setId(2L);
        cd2.setCategoria("LUZ");

        fp1 = new FormaPagamento();
        fp1.setId(1L);
        fp1.setForma("PIX");

        fp2 = new FormaPagamento();
        fp2.setId(2L);
        fp2.setForma("BOLETO");

        when(this.categoriaDespesaService.getTodos()).thenReturn(List.of(cd1, cd2));
        when(this.formaPagamentoService.getTodos()).thenReturn(List.of(fp1, fp2));

        view = new DespesaAvulsaView(controller, null);
        view.configurarComponent();
        controller.inicializarComboBox();

    }

    @Test
    @DisplayName("Metodo novo, deve destravar e limpar os campos da view")
    public void metodoNovoDeveChamarOsMetodosDeLimparEDestravarComponentes() {

        controller.novo();

        verify(controller, times(1)).enableDisableComponents(true);
        verify(controller, times(1)).limparCampos();

    }

    @Test
    public void metodoEnableComponentesDeveAtivarTodosOsComponentesQueNaoEstaoNaListaDeExclusao() {
        List<String> exludeComponents = List.of("btnNovo", "btnEditar",
                "btnSalvar", "btnDeletar", "fieldId");

        view.getAllComponentes().stream().forEach(c -> {

            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                c.setEnabled(false);
            }
        });

        controller.enableDisableComponents(true);

        view.getAllComponentes().stream().forEach(c -> {
            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                assertEquals(true, c.isEnabled());
            }

        });

    }

    @Test
    public void metodoEnableComponentesDeveDesativarTodosOsComponentesQueNaoEstaoNaListaDeExclusao() {
        List<String> exludeComponents = List.of("btnNovo", "btnEditar",
                "btnSalvar", "btnDeletar", "fieldId");

        view.getAllComponentes().stream().forEach(c -> {

            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                c.setEnabled(true);
            }
        });

        controller.enableDisableComponents(false);

        view.getAllComponentes().stream().forEach(c -> {
            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                assertEquals(false, c.isEnabled());
            }

        });

    }

    @Test
    public void metodoEditarNaoDeveAtivarComponentesSeFieldIdForNull() {
        controller.editar();
        verify(controller, times(0)).enableDisableComponents(true);
    }

    @Test
    public void metodoEditarDeveAtivarComponentesSeFieldIdForDiferenteDeNull() {

        view.getFieldId().setText("1");
        controller.editar();
        verify(controller, times(1)).enableDisableComponents(true);

    }

    @Test
    public void metodoDeletarDeveRetornarChamaODeleteDoServiço() {

        view.getFieldId().setText("1");
        controller.deletar();
        verify(despesaService, times(1)).deleteById(1l);
        verify(controller, times(1)).limparCampos();
    }

    @Test
    public void metodosLimparCamposDeveLimparCamposDeTextoEComboBoxELimparListaDeParcelasSalvas() {

        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldNota().setText("teste");
        view.getFieldNotaEmissao().setText("0102");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);

        view.getComboParcelamento().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldValor().setText("teste");
        view.getFieldVencimento().setText("0102");

        controller.limparCampos();

        view.getTextFields().stream().forEach(c -> {
            assertEquals(c.getText(), "");
        });

        assertEquals(view.getComboCategoria().getSelectedIndex(), -1);
        assertEquals(view.getComboParcelamento().getSelectedIndex(), -1);
        assertEquals(view.getComboFormaPagamento().getSelectedIndex(), -1);

    }


    
    



    
}
