/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.entities.despesas.DespesaAvulsa;
import br.com.thveiculos.erp.entities.despesas.NotaFiscal;
import br.com.thveiculos.erp.exceptions.despesas.FieldsEmBrancoException;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.services.despesas.interfaces.MovimentoPagamentoService;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.views.despesas.DespesaAvulsaViewImpl;
import br.com.thveiculos.erp.views.interfaces.DespesaAvulsaView;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 * Controlador para view que manipulem objetos do tipo {@link DespesaAvulsaView}.
 * 
 * @author victor
 */
@Controller
@Lazy
public class DespesaAvulsaController extends DespesaAbstractController<DespesaAvulsaViewImpl> {

    @Autowired
    public DespesaAvulsaController(DespesaService service, CategoriaDespesaService categoriaDespesaService, FormaPagamentoService formaPagamentoService,MovimentoPagamentoService movimentoService) {
        super(service, categoriaDespesaService, formaPagamentoService,movimentoService);
    }

    @Override
    public void salvar() {
        DespesaAvulsa despesa = new DespesaAvulsa();

        String numeroNota = view.getFieldNota().getText().trim();
        String dataNota = view.getFieldNotaEmissao().getText().trim();
        String id = view.getFieldId().getText().trim();

        if (!id.equals("")) {
            despesa.setId(Long.valueOf(id));
        }

        if (!numeroNota.equals("") && !dataNota.equals("")) {
            despesa.setNotaFiscal(buildNota(numeroNota, dataNota));
        }

        String nome = view.getFieldDescricao().getText().trim().toUpperCase();
        String descricao = view.getAreaDescricao().getText().trim().toUpperCase();
        String categoria = (String) view.getComboCategoria().getSelectedItem();

        despesa.setNomeFornecedor(nome);
        despesa.setDescricao(descricao);
        despesa.setCategoria(this.categoriaDespesaService.getByCategoria(categoria));
        despesa.setParcelas(this.movimentos);

        this.service.save(despesa);
        limparCampos();

    }

    public NotaFiscal buildNota(String numero, String data) {

        NotaFiscal nota = new NotaFiscal();
        nota.setDataEmissao(ConversorData.paraData(data));
        nota.setNumero(numero);
        return nota;
    }

    public void gerarParcelas() {
        criarMovimentos();
        preencherTabela(movimentos);
        limparCamposParcelamento();
    }

    public void criarMovimentos() {

        movimentos = movimentoService.gerarMovimentos(
                (String) view.getComboParcelamento().getSelectedItem(),
                (int) view.getSpinnerQuantidadeParcelas().getValue(),
                view.getFieldVencimento().getText(),
                view.getFieldValor().getText(),
                formaPagamentoService.getByForma((String) view.getComboFormaPagamento().getSelectedItem()));

    }

    public void limparCamposParcelamento() {
        view.getComboParcelamento().setSelectedIndex(-1);
        view.getFieldValor().setText("");
        view.getComboFormaPagamento().setSelectedIndex(-1);
        view.getSpinnerQuantidadeParcelas().getModel().setValue(1);
        view.getFieldVencimento().setText("");
    }

    @Override
    public void checarErrosAoSalvar() {
        List<String> exclude = List.of(
                "fieldCodFornecedor",
                "fieldId",
                "comboFormaPagamentoTabela",
                "comboParcelamento",
                "fieldNotaFiscal",
                "fieldNotaFiscalEmissao",
                "fieldValor",
                "fieldVencimento");


         checarFieldsEmBranco(exclude);
         checarCombosEmBranco(exclude);
 
    
    }
    
    private void checarFieldsEmBranco(List<String> exclude) {
        Optional<JTextComponent> fields = view
                .getTextFields()
                .stream()
                .filter(c -> c.getText().trim().isEmpty() && !exclude.contains(c.getName())).findFirst();

        if (fields.isPresent()) {
            throw new FieldsEmBrancoException("Todos os campos devem ser preenchidos.");
        }
    }

    private void checarCombosEmBranco(List<String> exclude) {
        Optional<JComboBox<String>> combos = view
                .getComboBoxes()
                .stream()
                .filter(c -> c.getSelectedIndex() == -1 && !exclude.contains(c.getName())).findFirst();

        if (combos.isPresent()) {
            throw new FieldsEmBrancoException("Todos os campos devem ser preenchidos.");
        }
    }

}
