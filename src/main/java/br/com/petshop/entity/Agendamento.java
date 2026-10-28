package br.com.petshop.entity;

import jakarta.persistence.*; import java.time.*;

@Entity @Table(name="agendamentos", uniqueConstraints=@UniqueConstraint(columnNames={"data_servico","horario"}))
public class Agendamento {
 public enum Servico { BANHO, TOSA, BANHO_E_TOSA } public enum Status { AGENDADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO }
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(name="data_servico",nullable=false) private LocalDate data; @Column(nullable=false) private LocalTime horario; @Enumerated(EnumType.STRING) @Column(nullable=false) private Servico servico; @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status=Status.AGENDADO; @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="pet_id") private Pet pet;
 protected Agendamento(){} public Agendamento(LocalDate d,LocalTime h,Servico s,Status st,Pet p){data=d;horario=h;servico=s;status=st;pet=p;}
 public Long getId(){return id;} public LocalDate getData(){return data;} public LocalTime getHorario(){return horario;} public Servico getServico(){return servico;} public Status getStatus(){return status;} public Pet getPet(){return pet;}
 public void atualizar(LocalDate d,LocalTime h,Servico s,Status st,Pet p){data=d;horario=h;servico=s;status=st;pet=p;}
}
