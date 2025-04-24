/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
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
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controlador para view que manipulem objetos do tipo {@link DespesaRecorrenteView}.
 * @author victor
 */
@Controller
public class DespesaRecorrenteController extends DespesaAbstractController<DespesaRecorrenteViewImpl>{

    @Autowired
    public DespesaRecorrenteController(DespesaService service, CategoriaDespesaService categoriaDespesaService, FormaPagamentoService formaPagamentoService,MovimentoPagamentoService movimentoService) {
        super(service, categoriaDespesaService, formaPagamentoService,movimentoService);
    }
    
    @Override
    public void salvar() {
        checarErrosAoSalvar();
        DespesaRecorrente despesa = new DespesaRecorrente();

        String id = view.getFieldId().getText();

        if (!id.equals("")) {
            despesa.setId(Long.valueOf(id));
        }

        String nome = view.getFieldDescricao().getText().toUpperCase();
        String descricao = view.getAreaDescricao().getText().toUpperCase();
        String categoria = (String) view.getComboCategoria().getSelectedItem();
        String formaPagamento = (String) view.getComboFormaPagamento().getSelectedItem();
        String dataInicio = view.getFieldDataInicio().getText();
        String dataFim = view.getFieldDataFim().getText();
        Periodo periodo = Periodo.valueOf((String)view.getComboParcelamento().getSelectedItem());
        String valor = view.getFieldValor().getText();
        Integer dia = Integer.valueOf(view.getFieldDiaVencimento().getText());
        
        
        
        despesa.setNomeFornecedor(nome);
        despesa.setDescricao(descricao);
        despesa.setCategoria(this.categoriaDespesaService.getByCategoria(categoria));
        despesa.setFormaPagamentoPadrao(this.formaPagamentoService.getByForma(formaPagamento));
        despesa.setDataInicio(ConversorData.paraData(dataInicio));
        despesa.setDataFim(ConversorData.paraData(dataFim));
        despesa.setPeriocidade(periodo);
        despesa.setValorTotal(ConversorMoeda.paraBigDecimal(valor));
        despesa.setDiaPagamento(dia);
        
        
        if(!view.getFieldMesVencimento().getText().trim().isEmpty()){
            Integer mes = Integer.valueOf(view.getFieldMesVencimento().getText());
            
            despesa.setMesPagamento(mes);
        }
        
        service.save(despesa);
        limparCampos();
        enableDisableComponents(false);
        
        

    }
    
    
    @Override
    public void checarErrosAoSalvar()throws FieldsEmBrancoException{
          
        List<String> exclude = List.of(
                "fieldMesVencimento",
                "fieldCodFornecedor",
                "fieldId",
                "comboFormaPagamentoTabela"
        );

        checarFieldsEmBranco(exclude);
        checarCombosEmBranco(exclude);
        
        String parcelamento = (String)view.getComboParcelamento().getSelectedItem();
        if((parcelamento.equals("ANUAL")) && (view.getFieldMesVencimento().getText().trim().isEmpty())){
            throw new FieldsEmBrancoException("Se a despesa for 'ANUAL' ela deve possuir um mês");
        }
        
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
    
    public void diaVencimentoAoPerderFoco() throws DiaVencimentoInvalidoException, NumberFormatException{
 
        Integer dia;
        Integer mes;

        if (view.getFieldDiaVencimento().getText().trim().isEmpty()) {
            return;
        }

        dia = Integer.valueOf(view.getFieldDiaVencimento().getText());
        if (dia <= 0 || dia > 30) {
            throw new DiaVencimentoInvalidoException("O dia informado deve estar entre 1 e 30");
        }

        if (view.getFieldMesVencimento().getText().trim().isEmpty()) {
            return;
        }

        mes = Integer.valueOf(view.getFieldMesVencimento().getText());

        if (dia > 28 && mes == 2) {
            throw new DiaVencimentoInvalidoException("Se o mês for fevereiro o dia não pode ser maior que 28 !");
        }
        
    }
    
    
    public void mesVencimentoAoPerderFoco() throws MesVencimentoInvalidoException, NumberFormatException {
        
        Integer dia;
        Integer mes;
        
        if (view.getFieldMesVencimento().getText().trim().isEmpty()) {
            return;
        }
        
        mes = Integer.valueOf(view.getFieldMesVencimento().getText());
        
        if (mes <= 0 || mes > 12) {
            throw new MesVencimentoInvalidoException("O mês informado não é válido !");
        }
        
        if (view.getFieldDiaVencimento().getText().trim().isEmpty()) {
            return;
        }

        dia = Integer.valueOf(view.getFieldDiaVencimento().getText());
        
        if (dia > 28 && mes == 2) {
            throw new MesVencimentoInvalidoException("Se o mês for fevereiro o dia não pode ser maior que 28 !");
        }
        
    }

    @Override
    public void preencherView(DespesaAbstrata despesa) {
        DespesaRecorrente despesaRecorrente = (DespesaRecorrente) despesa;
        this.movimentos = despesa.getParcelas();
        preencherFields(despesaRecorrente);
        preencherTabela(movimentos);
    }
    
    public void preencherFields(DespesaRecorrente despesa){
    
        view.getFieldId().setText(String.valueOf(despesa.getId()));      
        view.getFieldDescricao().setText(despesa.getNomeFornecedor());
        view.getAreaDescricao().setText(despesa.getDescricao());
        view.getComboCategoria().getModel().setSelectedItem(despesa.getCategoria().getName());
        view.getComboFormaPagamento().getModel().setSelectedItem(despesa.getFormaPagamentoPadrao().getName());
        view.getFieldDataInicio().setText(ConversorData.paraString(despesa.getDataInicio()));
        view.getFieldDataFim().setText(ConversorData.paraString(despesa.getDataFim()));
        view.getComboParcelamento().setSelectedItem(despesa.getPeriocidade().toString());
        view.getFieldValor().setText(ConversorMoeda.paraString(despesa.getValorTotal()));
        view.getFieldDiaVencimento().setText(String.valueOf(despesa.getDiaPagamento()));
        
        String mes = String.valueOf(despesa.getMesPagamento());
        
        if(!mes.equals("")){
        view.getFieldMesVencimento().setText(mes);
        }
        
    }



    
}
