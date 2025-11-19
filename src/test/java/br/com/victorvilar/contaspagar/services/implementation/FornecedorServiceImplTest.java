/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.EnderecoFornecedor;
import br.com.victorvilar.contaspagar.entities.Fornecedor;
import br.com.victorvilar.contaspagar.enums.UF;
import br.com.victorvilar.contaspagar.repositories.FornecedorRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
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
public class FornecedorServiceImplTest {
    
    @InjectMocks
    @Spy
    public FornecedorServiceImpl service;
    
    @Mock
    public FornecedorRepository repository;
    
    private Fornecedor forn1;
    private EnderecoFornecedor end1;
    
    
    @BeforeEach
    public void setUp(){
        
        forn1 = new Fornecedor();
        forn1.setId(1l);
        forn1.setCpfCnpj("111");
        forn1.setRazaoSocial("teste1");
        forn1.setNomeFantasia("teste1");
        forn1.setObservacao("teste1");
        
        end1 = new EnderecoFornecedor();
        end1.setBairro("end1");
        end1.setCep("end1");
        end1.setCidade("end1");
        end1.setId(1l);
        end1.setLogradouro("end1");
        end1.setNumero("end1");
        end1.setObservacao("end1");
        end1.setUf(UF.DF);
        
        forn1.setEndereco(end1);
        
        
        
        
    }
    
    @Test
    @DisplayName("metodo update")
    public void deveAtualizarOsDadosPassados(){
    
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(forn1));
        
        Fornecedor forn2 = new Fornecedor();
        forn2.setId(1l);
        forn2.setCpfCnpj("222");
        forn2.setRazaoSocial("teste1Atualizado");
        forn2.setNomeFantasia("teste1Atualizado");
        forn2.setObservacao("teste1Atualizado");
        
        EnderecoFornecedor end2 = new EnderecoFornecedor();
        end2.setBairro("end1Atualizado");
        end2.setCep("end1Atualizado");
        end2.setCidade("end1Atualizado");
        end2.setId(1l);
        end2.setLogradouro("end1Atualizado");
        end2.setNumero("end1Atualizado");
        end2.setObservacao("end1Atualizado");
        end2.setUf(UF.RJ);
        
        forn2.setEndereco(end2);
        
        service.update(forn2);
        assertEquals(forn1.getCpfCnpj(),forn2.getCpfCnpj());
        assertEquals(forn1.getRazaoSocial(),forn2.getRazaoSocial());
        assertEquals(forn1.getNomeFantasia(),forn2.getNomeFantasia());
        assertEquals(forn1.getObservacao(),forn2.getObservacao());
        assertEquals(forn1.getEndereco().getBairro(),forn2.getEndereco().getBairro());
        assertEquals(forn1.getEndereco().getCep(),forn2.getEndereco().getCep());
        assertEquals(forn1.getEndereco().getCidade(),forn2.getEndereco().getCidade());
        assertEquals(forn1.getEndereco().getLogradouro(),forn2.getEndereco().getLogradouro());
        assertEquals(forn1.getEndereco().getNumero(),forn2.getEndereco().getNumero());
        assertEquals(forn1.getEndereco().getObservacao(),forn2.getEndereco().getObservacao());
        assertEquals(forn1.getEndereco().getUf(),forn2.getEndereco().getUf());
    }
    
    @Test
    @DisplayName("metodo update")
    public void metodoDeveChamarOMetodoSalvarAposTerAtualizadoAsPropriedades(){
        
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(forn1));
        Fornecedor forn2 = new Fornecedor();
        forn2.setId(1l);
        forn2.setCpfCnpj("222");
        forn2.setRazaoSocial("teste1Atualizado");
        forn2.setNomeFantasia("teste1Atualizado");
        forn2.setObservacao("teste1Atualizado");
        
        EnderecoFornecedor end2 = new EnderecoFornecedor();
        end2.setBairro("end1Atualizado");
        end2.setCep("end1Atualizado");
        end2.setCidade("end1Atualizado");
        end2.setId(1l);
        end2.setLogradouro("end1Atualizado");
        end2.setNumero("end1Atualizado");
        end2.setObservacao("end1Atualizado");
        end2.setUf(UF.RJ);
        
        forn2.setEndereco(end2);
        
        service.update(forn2);
        verify(service,times(1)).save(any());
        
    }
    
}
