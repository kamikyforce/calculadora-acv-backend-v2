package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bd_categorias_leite")
public class CategoriaLeite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "categoria", nullable = false)
    private String categoria;
    
    @Column(name = "idade")
    private String idade;
    
    // Constructors
    public CategoriaLeite() {}
    
    public CategoriaLeite(String categoria, String idade) {
        this.categoria = categoria;
        this.idade = idade;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getIdade() {
        return idade;
    }
    
    public void setIdade(String idade) {
        this.idade = idade;
    }
}