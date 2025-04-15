package br.com.victorvilar.contaspagar.veiculos.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


/**
 * Classe para representar o movimento de entrada e saida de veículos
 *  
 *  */



@Entity
public class Entrada implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private VeiculoAbstrato veiculo;
	private LocalDate dataDeEntrada;
	private LocalDate dataDeSaida;
	private BigInteger valorCompra;
	private BigInteger valorVenda;
	

	
}
