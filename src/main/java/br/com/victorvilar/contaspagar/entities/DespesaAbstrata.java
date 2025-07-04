package br.com.victorvilar.contaspagar.entities;

import br.com.victorvilar.contaspagar.entities.interfaces.Despesa;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.victorvilar.contaspagar.enums.DespesaTipo;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "despesas")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DespesaAbstrata implements Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String descricao;
    private boolean quitado = false;
    protected BigDecimal valorTotal;
    @Enumerated(EnumType.STRING)
    private DespesaTipo tipo;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "despesa")
    @OrderBy("id ASC")
    private List<MovimentoPagamento> movimentos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaDespesa categoria;

    public Long getId() {
        return id;
    }

    @Override
    public CategoriaDespesa getCategoria() {
        return this.categoria;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao() {
        return this.descricao;
    }

    @Override
    public boolean isQuitado() {
        return this.quitado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setQuitado(boolean quitado) {
        this.quitado = quitado;
    }


    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public void setParcelas(List<MovimentoPagamento> movimentos) {
        this.movimentos.clear();
        movimentos.stream().forEach(m -> addParcela(m));

    }

    public List<MovimentoPagamento> getParcelas() {
        return this.movimentos;
    }

    public void addParcela(MovimentoPagamento parcela) {

        if (!this.movimentos.contains(parcela)) {
            movimentos.add(parcela);
            parcela.setDespesa(this);
        }

        getValorTotal();
    }

    public void removerParcela(MovimentoPagamento parcela) {
        if (this.movimentos.contains(parcela)) {
            movimentos.remove(parcela);
            parcela.setDespesa(null);
        }

        getValorTotal();
    }

    public void removerParcela(List<MovimentoPagamento> parcelas) {
        parcelas.stream().forEach(p -> removerParcela(p));
    }

    public int getQuantidadeParcelas() {
        return this.movimentos.size();
    }

    @Override
    public DespesaTipo getTipo() {
        return tipo;
    }

    public void setTipo(DespesaTipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (id == null) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DespesaAbstrata other = (DespesaAbstrata) obj;
        return Objects.equals(this.id, other.id);
    }

}
