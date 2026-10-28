package br.com.petshop.service;

import br.com.petshop.entity.*; import br.com.petshop.repository.*; import org.springframework.stereotype.Service; import java.time.*; import java.util.*; import java.math.BigDecimal;

@Service public class CrudServices {
 public final TutorRepository tutores; public final PetRepository pets; public final ProdutoRepository produtos; public final AgendamentoRepository agendamentos; public CrudServices(TutorRepository t,PetRepository p,ProdutoRepository pr,AgendamentoRepository a){tutores=t;pets=p;produtos=pr;agendamentos=a;}
 public Tutor tutor(Long id){return tutores.findById(id).orElseThrow(()->new NoSuchElementException("Tutor não encontrado"));} public Pet pet(Long id){return pets.findById(id).orElseThrow(()->new NoSuchElementException("Pet não encontrado"));}
 public Agendamento salvarAgendamento(LocalDate d,LocalTime h,Agendamento.Servico s,Agendamento.Status st,Long petId,Long id){if(d.isBefore(LocalDate.now()))throw new IllegalArgumentException("A data não pode estar no passado");if(agendamentos.existsByDataAndHorario(d,h)&&(id==null||agendamentos.findById(id).map(x->!x.getData().equals(d)||!x.getHorario().equals(h)).orElse(true)))throw new IllegalArgumentException("Já existe agendamento nesse horário");Pet p=pet(petId);Agendamento a=id==null?new Agendamento(d,h,s,st,p):agendamentos.findById(id).orElseThrow();if(id!=null)a.atualizar(d,h,s,st,p);return agendamentos.save(a);}
}
