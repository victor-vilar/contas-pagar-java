/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.EnderecoFornecedor;
import br.com.victorvilar.contaspagar.entities.Fornecedor;
import br.com.victorvilar.contaspagar.exceptions.FornecedorNotFoundException;
import br.com.victorvilar.contaspagar.repositories.FornecedorRepository;
import br.com.victorvilar.contaspagar.services.interfaces.FornecedorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author victor
 */
@Service
public class FornecedorServiceImpl implements FornecedorService{

    private final FornecedorRepository repository;
    
    @Autowired
    public FornecedorServiceImpl(FornecedorRepository repository){
        this.repository = repository;
    }
    
    @Override
    public List<Fornecedor> getTodos() {
        return repository.findAll();
    }

    @Override
    public Fornecedor getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new FornecedorNotFoundException("Fornecedor Não encontrado !"));
    }

    @Override
    public Fornecedor save(Fornecedor obj) {
        return this.repository.save(obj);
    }

    @Override
    public List<Fornecedor> saveAll(List<Fornecedor> objs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Fornecedor update(Fornecedor obj) {
        Fornecedor fornecedorParaAtualizar = getById(obj.getId());
        fornecedorParaAtualizar.setCpfCnpj(obj.getCpfCnpj());
        fornecedorParaAtualizar.setRazaoSocial(obj.getRazaoSocial());
        fornecedorParaAtualizar.setNomeFantasia(obj.getNomeFantasia());
        fornecedorParaAtualizar.setObservacao(obj.getObservacao());
        atualizarEndereco(fornecedorParaAtualizar.getEndereco(),obj.getEndereco());
        return save(fornecedorParaAtualizar);
    }
    
    public void atualizarEndereco(EnderecoFornecedor end1, EnderecoFornecedor end2){
        end1.setBairro(end2.getBairro());
        end1.setCep(end2.getCep());
        end1.setCidade(end2.getCidade());
        end1.setLogradouro(end2.getLogradouro());
        end1.setNumero(end2.getNumero());
        end1.setObservacao(end2.getObservacao());
        end1.setUf(end2.getUf());
    }
    

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll(List<Fornecedor> objs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
