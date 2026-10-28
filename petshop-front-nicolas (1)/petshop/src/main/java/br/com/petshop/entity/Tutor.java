package br.com.petshop.entity;

import jakarta.persistence.*;
import br.com.petshop.security.AesAttributeConverter;

@Entity @Table(name="tutores")
public class Tutor {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String nome;
  @Convert(converter=AesAttributeConverter.class) @Column(nullable=false, columnDefinition="TEXT") private String telefone;
  @Column(nullable=false) private String email;
  @Convert(converter=AesAttributeConverter.class) @Column(nullable=false, columnDefinition="TEXT") private String cpf;
  private String endereco;
  protected Tutor() {}
  public Tutor(String nome,String telefone,String email,String cpf,String endereco){this.nome=nome;this.telefone=telefone;this.email=email;this.cpf=cpf;this.endereco=endereco;}
  public Long getId(){return id;} public String getNome(){return nome;} public String getTelefone(){return telefone;} public String getEmail(){return email;} public String getCpf(){return cpf;} public String getEndereco(){return endereco;}
  public void atualizar(String n,String t,String e,String c,String a){nome=n;telefone=t;email=e;cpf=c;endereco=a;}
}
