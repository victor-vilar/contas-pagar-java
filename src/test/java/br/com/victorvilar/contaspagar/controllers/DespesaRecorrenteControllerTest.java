/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.exceptions.DiaVencimentoInvalidoException;
import br.com.victorvilar.contaspagar.exceptions.FieldsEmBrancoException;
import br.com.victorvilar.contaspagar.exceptions.MesVencimentoInvalidoException;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.DespesaRecorrenteViewImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
public class DespesaRecorrenteControllerTest {

    @Spy
    @InjectMocks
    private DespesaRecorrenteController controller;

    @Mock
    private CategoriaDespesaService categoriaDespesaService;

    @Mock
    private FormaPagamentoService formaPagamentoService;

    @Mock
    private MovimentoPagamentoService movimentoService;

    @Mock
    private DespesaService despesaService;

    @Mock
    public DespesaControllerHelper controllerHelper;

    private DespesaRecorrenteViewImpl view;


    List<MovimentoPagamento> d1Movimentos = new ArrayList<>();
    CategoriaDespesa cd1;
    CategoriaDespesa cd2;

    FormaPagamento fp1;
    FormaPagamento fp2;

    DespesaRecorrente d1;

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

        view = new DespesaRecorrenteViewImpl(controller, null);
        view.configurarComponent();

        d1 = new DespesaRecorrente();


        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        m1.setFormaPagamento(fp1);

        MovimentoPagamento m2 = new MovimentoPagamento();
        m2.setId(2L);
        m2.setReferenteParcela("2/2");
        m2.setDataVencimento(LocalDate.of(2025, Month.APRIL, 1));
        m2.setValorPagamento(new BigDecimal("1000"));
        m2.setFormaPagamento(fp1);

        d1Movimentos.add(m1);
        d1Movimentos.add(m2);

        d1.setId(1l);
        d1.setNomeFornecedor("LOREM TEST");
        d1.setDescricao("LOREM IPSUM");
        d1.setCategoria(cd1);
        d1.setParcelas(d1Movimentos);
        d1.setQuitado(false);
        d1.setValorTotal(new BigDecimal("0"));
        d1.setPeriocidade(Periodo.MENSAL);
        d1.setDataInicio(LocalDate.now());
        d1.setDataFim(LocalDate.of(9999, Month.MARCH, 1));
        d1.setDiaPagamento(Integer.MIN_VALUE);
        d1.setFormaPagamentoPadrao(fp1);

    }

    @Test
    @DisplayName("Metodo novo, deve destravar e limpar os campos da view")
    public void metodoNovoDeveChamarOsMetodosDeLimparEDestravarComponentes() {

        controller.novo();

        verify(controller, times(1)).enableDisableComponents(true);
        verify(controller, times(1)).limparCampos();

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
    public void metodoDeletarDeveChamaODeleteDoServiço() {

        view.getFieldId().setText("1");
        controller.deletar();
        verify(despesaService, times(1)).deleteById(1l);
        verify(controller, times(1)).limparCampos();
    }

    @Test
    public void metodoLimparCamposDeveDeletarTextosDosComponentes() {

        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("10");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);

        controller.limparCampos();

        assertEquals(view.getFieldId().getText(), "");
        assertEquals(view.getFieldDescricao().getText(), "");
        assertEquals(view.getFieldDiaVencimento().getText(), "");
        assertEquals(view.getFieldMesVencimento().getText(), "");
        assertEquals(view.getFieldCodFornecedor().getText(), "");
        assertEquals(view.getAreaDescricao().getText(), "");
        assertEquals(view.getComboCategoria().getSelectedIndex(), -1);

    }

    @Test
    public void metodoLimparCamposDeveLimparListaDeMovimentos() {
        List<MovimentoPagamento> movimentos = controller.getMovimentos();
        movimentos.add(new MovimentoPagamento());
        movimentos.add(new MovimentoPagamento());

        assertFalse(controller.getMovimentos().isEmpty());
        assertEquals(controller.getMovimentos().size(), 2);

        controller.limparCampos();
        assertTrue(controller.getMovimentos().isEmpty());
    }

    @Test
    public void metodoInicializarComboBoxDeveChamarOutrosMetodosComSucesso() {

        verify(controller, times(1)).resetarCombos();
        verify(controller, times(1)).inicializarComboCategoria();
        verify(controller, times(1)).inicializarComboFormaPagamento();
        verify(controller, times(1)).inicializarComboParcelamento();
    }

    @Test
    public void metodoResetarCombosDeveLimparComboBoxes() {

        view.getComboCategoria().addItem("categoria 1");
        view.getComboCategoria().addItem("categoria 2");
        view.getComboFormaPagamento().addItem("forma 1");
        view.getComboFormaPagamento().addItem("forma 2");
        view.getComboFormaPagamentoTabela().addItem("forma 1");
        view.getComboFormaPagamentoTabela().addItem("forma 2");
        view.getComboParcelamento().addItem("mensal");

        controller.resetarCombos();

        assertEquals(view.getComboCategoria().getModel().getSize(), 0);
        assertEquals(view.getComboFormaPagamento().getModel().getSize(), 0);
        assertEquals(view.getComboFormaPagamentoTabela().getModel().getSize(), 0);
        assertEquals(view.getComboParcelamento().getModel().getSize(), 0);

    }

    @Test
    public void metodoInicializarComboCategoriaDeveInicializarComboBox() {
        when(categoriaDespesaService.getTodos()).thenReturn(List.of(cd1, cd2));
        controller.resetarCombos();
        controller.inicializarComboCategoria();
        assertEquals(view.getComboCategoria().getModel().getSize(), 2);
        assertEquals((String) view.getComboCategoria().getModel().getSelectedItem(), null);
    }

    @Test
    public void metodoInicializarComboFormaPagamentoDeveInicializarComboBox() {
        when(formaPagamentoService.getTodos()).thenReturn(List.of(fp1, fp2));
        controller.resetarCombos();
        controller.inicializarComboFormaPagamento();
        assertEquals(view.getComboFormaPagamento().getModel().getSize(), 2);
        assertEquals(view.getComboFormaPagamentoTabela().getModel().getSize(), 2);
        assertEquals((String) view.getComboFormaPagamento().getModel().getSelectedItem(), null);
    }

    @Test
    public void metodoInicializarComboParcelamentoDeveInicializarComboBox() {
        controller.resetarCombos();
        controller.inicializarComboParcelamento();
        assertFalse(view.getComboParcelamento().getModel().getSize() == 0);
        assertEquals((String) view.getComboParcelamento().getModel().getSelectedItem(), null);
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
    public void metodoPreencherTabelaDeveAdicionarDadosVindoDoMovimento() {
        List<MovimentoPagamento> movimentos = new ArrayList<>();
        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        m1.setFormaPagamento(fp1);

        MovimentoPagamento m2 = new MovimentoPagamento();
        m2.setId(2L);
        m2.setReferenteParcela("2/2");
        m2.setDataVencimento(LocalDate.of(2025, Month.APRIL, 1));
        m2.setValorPagamento(new BigDecimal("1000"));
        m2.setFormaPagamento(fp1);

        movimentos.add(m1);
        movimentos.add(m2);

        controller.preencherTabela(movimentos);

        assertEquals(view.getTableParcelas().getValueAt(0, 0), m1.getId());
        assertEquals(view.getTableParcelas().getValueAt(0, 1), m1.getReferenteParcela());
        assertEquals(view.getTableParcelas().getValueAt(0, 2), ConversorData.paraString(m1.getDataVencimento()));
        assertEquals(view.getTableParcelas().getValueAt(0, 3), ConversorMoeda.paraString(m1.getValorPagamento()));
        assertEquals(view.getTableParcelas().getValueAt(0, 4), ConversorData.paraString(m1.getDataPagamento()));
        assertEquals(view.getTableParcelas().getValueAt(0, 5), m1.getFormaPagamento().getName());

        assertEquals(view.getTableParcelas().getValueAt(1, 0), m2.getId());
        assertEquals(view.getTableParcelas().getValueAt(1, 1), m2.getReferenteParcela());
        assertEquals(view.getTableParcelas().getValueAt(1, 2), ConversorData.paraString(m2.getDataVencimento()));
        assertEquals(view.getTableParcelas().getValueAt(1, 3), ConversorMoeda.paraString(m2.getValorPagamento()));
        assertEquals(view.getTableParcelas().getValueAt(1, 4), ConversorData.paraString(m2.getDataPagamento()));
        assertEquals(view.getTableParcelas().getValueAt(1, 5), m2.getFormaPagamento().getName());

    }

    @Test
    public void metodoAtualizarLinhaAlteradaDeveChamarMetodos() {

        controller.atualizarMovimento(1);
        verify(controller, times(1)).editarMovimento(eq(1));
        verify(controller, times(1)).preencherTabela(anyList());
    }

    @Test
    public void metodoAoSubscreverDeveChamarMetodoSubscriptionCategoriaDespesa() {

        controller.aoSusbscrever("teste", "Categoria Despesas");
        verify(controller, times(1)).subscriptionCategoriaDespesa("teste");

    }

    @Test
    public void metodoAoSubscreverDeveChamarMetodoSubscriptionFormaPagamento() {

        controller.aoSusbscrever("teste", "Formas Pagamento");
        verify(controller, times(1)).subscriptionFormaPagamento("teste");
    }

    @Test
    public void metodoSubscriptionCategoriaDespesaDeveAtualizarNovosValoresESelecionarOValorInserido() {
        controller.subscriptionCategoriaDespesa("Teste");
        verify(controller, times(2)).inicializarComboCategoria();
        assertEquals((String) view.getComboCategoria().getSelectedItem(), "Teste");
    }

    @Test
    public void metodoSubscriptionFormaPagamentoDeveAtualizarNovosValoresESelecionarOValorInserido() {
        controller.subscriptionFormaPagamento("Teste");
        verify(controller, times(2)).inicializarComboFormaPagamento();
        assertEquals((String) view.getComboFormaPagamento().getSelectedItem(), "Teste");
    }

    @Test
    public void metodoSalvarDeveChamarMetodoChecarErrosAoSalvar() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(1);
        view.getFieldValor().setText("100");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("12");
        controller.salvar();
        verify(controller, times(1)).checarErrosAoSalvar();
    }

    @Test
    public void metodoSalvarChamaMetodoSaveDoService() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(1);
        view.getFieldValor().setText("100");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("12");
        controller.salvar();
        verify(despesaService, times(1)).save(any(DespesaRecorrente.class));
    }

    @Test
    public void metodoSalvarChamaMetodoLimparCampos() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(1);
        view.getFieldValor().setText("100");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("12");
        controller.salvar();
        verify(controller, times(1)).limparCampos();
    }

    @Test
    public void metodoSalvarChamaMetodoEnableComponentsComFalse() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(1);
        view.getFieldValor().setText("100");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("12");
        controller.salvar();
        verify(controller, times(1)).enableDisableComponents(false);
    }

    @Test
    public void metodoChecarErrosAoSalvarDeveLançarExceptionSeAlgumCampoEstiverEmBranco() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(1);
        view.getFieldValor().setText(" ");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("");

        Assertions.assertThrows(FieldsEmBrancoException.class, ()
                -> controller.checarErrosAoSalvar());

    }

    @Test
    public void metodoChecarErrosAoSalvarDeveLançarExceptionSeAlgumComboEstiverEmBranco() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(-1);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(0);
        view.getFieldValor().setText("R$1000,00 ");
        view.getFieldDiaVencimento().setText("10 ");
        view.getFieldMesVencimento().setText("10");

        Assertions.assertThrows(FieldsEmBrancoException.class, ()
                -> controller.checarErrosAoSalvar());
    }

    @Test
    public void metodoChecarErrosAoSalvarDeveLançarExceptionSeParcelamentoForAnualEMesVEncimentoNulo() {
        view.getFieldId().setText("2");
        view.getFieldDescricao().setText("Teste");
        view.getFieldCodFornecedor().setText("22");
        view.getAreaDescricao().setText("teste");
        view.getComboCategoria().setSelectedIndex(0);
        view.getComboFormaPagamento().setSelectedIndex(0);
        view.getFieldDataInicio().setText("01/02/2025");
        view.getFieldDataFim().setText("01/02/2025");
        view.getComboParcelamento().setSelectedIndex(0);
        view.getFieldValor().setText("R$1000,00 ");
        view.getFieldDiaVencimento().setText("12");
        view.getFieldMesVencimento().setText("");

        Assertions.assertThrows(FieldsEmBrancoException.class, ()
                -> controller.checarErrosAoSalvar());
    }

    @Test
    public void metodoDiaVencimentoAoPerderFocoLancaDiaVencimentoExceptionSeDiaForMenorQueZero() {

        view.getFieldDiaVencimento().setText("-1");
        Assertions.assertThrows(DiaVencimentoInvalidoException.class, ()
                -> controller.diaVencimentoAoPerderFoco());
    }

    @Test
    public void metodoDiaVencimentoAoPerderFocoLancaDiaVencimentoExceptionSeDiaForMaiorQueTrinta() {

        view.getFieldDiaVencimento().setText("35");
        Assertions.assertThrows(DiaVencimentoInvalidoException.class, ()
                -> controller.diaVencimentoAoPerderFoco());
    }

    @Test
    public void metodoDiaVencimentoAoPerderFocoLancaDiaVencimentoExceptionSeDiaForMaiorQueVinteOitoEMesDois() {

        view.getFieldDiaVencimento().setText("29");
        view.getFieldMesVencimento().setText("2");
        Assertions.assertThrows(DiaVencimentoInvalidoException.class, ()
                -> controller.diaVencimentoAoPerderFoco());
    }

    @Test
    public void metodoMesVencimentoAoPerderFocoLancaMesVencimentoExceptionSeMesForMenorOuIgualAZero() {

        view.getFieldMesVencimento().setText("-1");
        Assertions.assertThrows(MesVencimentoInvalidoException.class, ()
                -> controller.mesVencimentoAoPerderFoco());
    }

    @Test
    public void metodoMesVencimentoAoPerderFocoLancaMesVencimentoExceptionSeMesForMaiorQueDoze() {

        view.getFieldMesVencimento().setText("35");
        Assertions.assertThrows(MesVencimentoInvalidoException.class, ()
                -> controller.mesVencimentoAoPerderFoco());
    }

    @Test
    public void metodoMesVencimentoAoPerderFocoLancaMesVencimentoExceptionSeDiaForMaiorQueVinteOitoEMesDois() {

        view.getFieldDiaVencimento().setText("29");
        view.getFieldMesVencimento().setText("2");
        Assertions.assertThrows(MesVencimentoInvalidoException.class, ()
                -> controller.mesVencimentoAoPerderFoco());
    }

    @Test
    public void metodoPreencherViewDevePegarListaDeMovimentosDoObjetoPassado() {
        when(movimentoService.getAllByDespesaId(d1.getId())).thenReturn(d1Movimentos);
        controller.preencherView(d1);
        assertNotNull(controller.getMovimentos());
        assertEquals(controller.getMovimentos().get(0), d1.getParcelas().get(0));
        assertEquals(controller.getMovimentos().get(1), d1.getParcelas().get(1));
        assertEquals(controller.getMovimentos().size(), 2);
    }

    @Test
    public void metodoPreencherViewDeveChamarMetodoPreencherFields() {
        controller.preencherView(d1);
        verify(controller, times(1)).preencherFields(any(DespesaRecorrente.class));
    }

    @Test
    public void metodoPreencherViewDeveChamarMetodoPreencherTabela() {
        controller.preencherView(d1);
        verify(controller, times(1)).preencherTabela(anyList());
    }

    @Test
    public void metodoPreencherFieldsDevePreencherTodosOsCampos() {
        controller.preencherFields(d1);
        assertEquals(view.getFieldId().getText(), String.valueOf(d1.getId()));
        assertEquals(view.getFieldDescricao().getText(), d1.getNomeFornecedor());
        assertEquals(view.getAreaDescricao().getText(), d1.getDescricao());
        assertEquals(view.getComboCategoria().getModel().getSelectedItem(), d1.getCategoria().getCategoria());
        assertEquals(view.getComboFormaPagamento().getModel().getSelectedItem(), d1.getFormaPagamentoPadrao().getForma());
        assertEquals(view.getFieldDataInicio().getText(), ConversorData.paraString(d1.getDataInicio()));
        assertEquals(view.getFieldDataFim().getText(), ConversorData.paraString(d1.getDataFim()));
        assertEquals(view.getComboParcelamento().getModel().getSelectedItem(), d1.getPeriocidade().toString());
        assertEquals(view.getFieldValor().getText(), ConversorMoeda.paraString(d1.getValorTotal()));
        assertEquals(view.getFieldDiaVencimento().getText(), String.valueOf(d1.getDiaPagamento()));

    }

}
