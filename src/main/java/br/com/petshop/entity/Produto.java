package br.com.petshop.entity;

import jakarta.persistence.*; import java.math.BigDecimal;

@Entity @Table(name="produtos")
public class Produto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false) private String nome; @Column(nullable=false,precision=10,scale=2) private BigDecimal preco; @Column(nullable=false) private Integer estoque;
 protected Produto(){} public Produto(String n,BigDecimal p,Integer e){nome=n;preco=p;estoque=e;}
 public Long getId(){return id;} public String getNome(){return nome;} public BigDecimal getPreco(){return preco;} public Integer getEstoque(){return estoque;}
 public void atualizar(String n,BigDecimal p,Integer e){nome=n;preco=p;estoque=e;}
}
