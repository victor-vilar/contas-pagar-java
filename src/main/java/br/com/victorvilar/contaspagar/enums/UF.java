/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.enums;

/**
 *
 * @author victor
 */
public enum UF {
  AC("Acre", "Rio Branco"),
  AL("Alagoas", "Maceió"),
  AM("Amazonas", "Manaus"),
  AP("Amapá", "Macapá"),
  BA("Bahia", "Salvador"),
  CE("Ceará", "Fortaleza"),
  DF("Distrito Federal", "Brasília"),
  ES("Espírito Santo", "Vitória"),
  GO("Goiás", "Goiânia"),
  MA("Maranhão", "São Luís"),
  MG("Minas Gerais", "Belo Horizonte"),
  MS("Mato Grosso do Sul", "Campo Grande"),
  MT("Mato Grosso", "Cuiabá"),
  PA("Pará", "Belém"),
  PB("Paraíba", "João Pessoa"),
  PE("Pernambuco", "Recife"),
  PI("Piauí", "Teresina"),
  PR("Paraná", "Curitiba"),
  RJ("Rio de Janeiro", "Rio de Janeiro"),
  RN("Rio Grande do Norte", "Natal"),
  RO("Rondônia", "Porto Velho"),
  RR("Roraima", "Boa Vista"),
  RS("Rio Grande do Sul", "Porto Alegre"),
  SC("Santa Cataria", "Florianópolis"),
  SE("Sergipe", "Aracaju"),
  SP("São Paulo", "São Paulo"),
  TO("Tocantins", "Palmas");
  

  private final String nome;
  private final String capital;
  
  public String getNome(){
      return this.nome;
  }
  
  public String capital(){
      return this.capital;
  }
  
  UF(String nome, String capital){
      this.nome = nome;
      this.capital = capital;
  }
  

  /**
   * Converte, a partir da sigla da federação para o enum UF.
   *
   * @param nomeUf sigla da Unidade da Federação. Exemplo: "eS"
   * @return a Unidade da Federação ou nulo caso não encontrado
   */

  public static UF fromSigla(final String nomeUf) {
    for (final UF uf : UF.values()) {
      if (uf.name().equalsIgnoreCase(nomeUf)) {
        return uf;
      }
    }

    return null;
  }

  /**
   * Converte, a partir do nome da federação para o enum UF.
   *
   * @param nome capital da Unidade da Federação. Exemplo: "portO AlegrE"
   * @return a Unidade da Federacao ou nulo caso não encontrado
   */
  public static UF fromNome(final String nome) {
    for (final UF uf : UF.values()) {
      if (uf.nome.equalsIgnoreCase(nome)) {
        return uf;
      }
    }

    return null;
  }

  /**
   * Converte, a partir do nome da capital para o enum UF.
   *
   * @param capital A capital da Unidade da Federação. Exemplo: "portO AlegrE"
   * @return a Unidade da Federacao ou nulo caso não encontrado
   */
  public static UF fromCapital(final String capital) {
    for (final UF uf : UF.values()) {
      if (uf.capital.equalsIgnoreCase(capital)) {
        return uf;
      }
    }

    return null;
  }

  
}
