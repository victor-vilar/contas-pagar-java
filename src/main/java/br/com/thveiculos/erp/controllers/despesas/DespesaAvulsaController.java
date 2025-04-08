/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.controllers.AppViewController;
import br.com.thveiculos.erp.entities.despesas.DespesaAvulsa;
import br.com.thveiculos.erp.entities.despesas.NotaFiscal;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.views.despesas.DespesaAvulsaViewImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
@Lazy
public class DespesaAvulsaController extends DespesaAbstractController<DespesaAvulsaViewImpl>{

    @Autowired
    public DespesaAvulsaController(DespesaService service, CategoriaDespesaService categoriaDespesaService, FormaPagamentoService formaPagamentoService) {
        super(service, categoriaDespesaService, formaPagamentoService);
    }


    @Override
    public void salvar() {
        DespesaAvulsa despesa = new DespesaAvulsa();

        String numeroNota = view.getFieldNota().getText();
        String dataNota = view.getFieldNotaEmissao().getText();
        String id = view.getFieldId().getText();

        if (!id.equals("")) {
            despesa.setId(Long.valueOf(id));
        }

        if (!numeroNota.equals("") && !dataNota.equals("")) {
            despesa.setNotaFiscal(buildNota(numeroNota, dataNota));
        }

        String nome = view.getFieldDescricao().getText().toUpperCase();
        String descricao = view.getAreaDescricao().getText().toUpperCase();
        String categoria = (String) view.getComboCategoria().getSelectedItem();

        despesa.setNomeFornecedor(nome);
        despesa.setDescricao(descricao);
        despesa.setCategoria(this.categoriaDespesaService.getByCategoria(categoria));
        despesa.setParcelas(this.movimentos);

        this.service.save(despesa);
        limparCampos();

    }

    private NotaFiscal buildNota(String numero, String data) {

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

    private void criarMovimentos() {
       
        movimentos = service.gerarMovimentos(
                (String) view.getComboParcelamento().getSelectedItem(),
                (int) view.getSpinnerQuantidadeParcelas().getValue(),
                view.getFieldVencimento().getText(),
                view.getFieldValor().getText(),
                formaPagamentoService.getByForma((String) view.getComboFormaPagamento().getSelectedItem()));

    }

    protected void limparCamposParcelamento() {
        view.getComboParcelamento().setSelectedIndex(-1);
        view.getFieldValor().setText("");
        view.getComboFormaPagamento().setSelectedIndex(-1);
        view.getSpinnerQuantidadeParcelas().getModel().setValue(1);
        view.getFieldVencimento().setText("");
    }
}
