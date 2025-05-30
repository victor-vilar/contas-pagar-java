/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.NotaFiscal;
import br.com.victorvilar.contaspagar.exceptions.FieldsEmBrancoException;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.views.DespesaAvulsaViewImpl;
import br.com.victorvilar.contaspagar.views.interfaces.DespesaAvulsaView;
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
    public DespesaAvulsaController(DespesaService service,
                                   CategoriaDespesaService categoriaDespesaService,
                                   FormaPagamentoService formaPagamentoService,
                                   MovimentoPagamentoService movimentoService,
                                   DespesaControllerHelper controllerHelper) {
        super(service, categoriaDespesaService, formaPagamentoService,movimentoService,controllerHelper);
    }

    @Override
    public void salvar() {
        checarErrosAoSalvar();
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

        despesa.setNome(nome);
        despesa.setDescricao(descricao);
        despesa.setCategoria(this.categoriaDespesaService.getByCategoria(categoria));
        despesa.setParcelas(this.movimentos);

        this.service.save(despesa);
        limparCampos();
        enableDisableComponents(false);

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

        movimentos = controllerHelper.gerarMovimentos(
                (String) view.getComboParcelamento().getSelectedItem(),
                (int) view.getSpinnerQuantidadeParcelas().getValue(),
                view.getFieldVencimento().getText(),
                view.getFieldValor().getText(),
                formaPagamentoService.getByForma((String) view.getComboFormaPagamento().getSelectedItem()));

        movimentoService.adicionarOuAtualizarReferenteParcela(movimentos);

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
                "comboFormaPagamento",
                "comboFormaPagamentoTabela",
                "comboParcelamento",
                "fieldNota",
                "fieldNotaEmissao",
                "fieldValor",
                "fieldVencimento");


         checarFieldsEmBranco(exclude);
         checarCombosEmBranco(exclude);
         checarSeExistemParcelas();
 
    
    }
    
    private void checarFieldsEmBranco(List<String> exclude) {
        Optional<JTextComponent> fields = view
                .getTextFields()
                .stream()
                .filter(c -> c.getText().trim().isEmpty() && !exclude.contains(c.getName())).findFirst();

        if (fields.isPresent()) {
            System.out.println(fields.get().getName());
            throw new FieldsEmBrancoException("Todos os campos devem ser preenchidos.");
        }
    }

    private void checarCombosEmBranco(List<String> exclude) {
        Optional<JComboBox<String>> combos = view
                .getComboBoxes()
                .stream()
                .filter(c -> c.getSelectedIndex() == -1 && !exclude.contains(c.getName())).findFirst();

        if (combos.isPresent()) {
            System.out.println(combos.get().getName());
            throw new FieldsEmBrancoException("Todos os campos devem ser preenchidos.");
        }
    }

    private void checarSeExistemParcelas(){
        if(this.movimentos.isEmpty()){
            throw new QuantidadeDeParcelasException("Uma despesa deve possuir pelo menos uma parcela !");
        }
    }

    @Override
    public void preencherView(DespesaAbstrata despesa) {
        DespesaAvulsa despesaAvulsa = (DespesaAvulsa) despesa;
        this.movimentos = despesa.getParcelas();
        preencherFields(despesaAvulsa);
        preencherTabela(movimentos);
    }
    
    public void preencherFields(DespesaAvulsa despesa){
    
        view.getFieldId().setText(String.valueOf(despesa.getId()));
        
        if(despesa.getNotaFiscal() != null){
            view.getFieldNota().setText(despesa.getNotaFiscal().getNumero());
            view.getFieldNotaEmissao().setText(ConversorData.paraString(despesa.getNotaFiscal().getDataEmissao()));
        }
       
        view.getFieldDescricao().setText(despesa.getNome());
        view.getAreaDescricao().setText(despesa.getDescricao());
        view.getComboCategoria().getModel().setSelectedItem(despesa.getCategoria().getName());
        
    }
    
    
    @Override
    public void editar(){
        super.editar();
        if(!view.getFieldId().getText().trim().equals("")){
            view.getComboParcelamento().setEnabled(false);
            view.getSpinnerQuantidadeParcelas().setEnabled(false);
            view.getComboFormaPagamento().setEnabled(false);
            view.getFieldValor().setEnabled(false);
            view.getFieldVencimento().setEnabled(false);
            view.getBtnGerarParcelas().setEnabled(false);
            view.getBtnProcurarFormaPagamento().setEnabled(false);
        }
    }

}
