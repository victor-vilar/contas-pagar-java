/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.entities.despesas.DespesaRecorrente;
import br.com.thveiculos.erp.enums.despesas.Periodo;
import br.com.thveiculos.erp.exceptions.despesas.DiaVencimentoInvalidoException;
import br.com.thveiculos.erp.exceptions.despesas.MesVencimentoInvalidoException;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.util.ConversorMoeda;
import br.com.thveiculos.erp.views.despesas.DespesaRecorrenteViewImpl;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
public class DespesaRecorrenteController extends DespesaAbstractController<DespesaRecorrenteViewImpl>{

    @Autowired
    public DespesaRecorrenteController(DespesaService service, CategoriaDespesaService categoriaDespesaService, FormaPagamentoService formaPagamentoService) {
        super(service, categoriaDespesaService, formaPagamentoService);
    }
    
    @Override
    public void salvar() {
        DespesaRecorrente despesa = new DespesaRecorrente();

        if(checarErrosAoSalvar()){
            throw new RuntimeException("Não foram preenchidos todos os campos");
        }
        
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
        
        
        
    }
    
    
    @Override
    public boolean checarErrosAoSalvar(){
        
 
        List<String> exclude = List.of("fieldMesVencimento","fieldCodFornecedor","fieldId", "comboFormaPagamentoTabela");
        Optional<JTextComponent> fields = view
                        .getTextFields()
                        .stream()
                        .filter(c -> c.getText().trim().isEmpty() && !exclude.contains(c.getName())).findFirst();

        if(fields.isPresent()){
             return true;
        }
        
        
        Optional<JComboBox<String>> combos = view
                .getComboBoxes()
                .stream()
                .filter(c -> c.getSelectedIndex() == -1 && !exclude.contains(c.getName())).findFirst();
        
        if(combos.isPresent()){
             return true;
        }

        
        String parcelamento = (String)view.getComboParcelamento().getSelectedItem();
        if((parcelamento.equals("ANUAL")) && (view.getFieldMesVencimento().getText().trim().isEmpty())){
            return true;
        }
        
        return false;
        
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


    
}
