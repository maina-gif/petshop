package br.com.petshop.entity;

import jakarta.persistence.*;

@Entity @Table(name="usuarios", uniqueConstraints=@UniqueConstraint(columnNames="username"))
public class Usuario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String username;
  @Column(nullable=false) private String password;
  @Column(nullable=false) private String role = "ADMIN";
  protected Usuario() {}
  public Usuario(String username,String password,String role){this.username=username;this.password=password;this.role=role;}
  public Long getId(){return id;} public String getUsername(){return username;} public String getPassword(){return password;} public String getRole(){return role;}
  public void setPassword(String p){password=p;}
}
