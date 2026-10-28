package br.com.petshop.repository; import br.com.petshop.entity.Agendamento; import org.springframework.data.jpa.repository.EntityGraph; import org.springframework.data.jpa.repository.JpaRepository; import java.time.*; import java.util.*; public interface AgendamentoRepository extends JpaRepository<Agendamento,Long>{boolean existsByDataAndHorario(LocalDate d,LocalTime h); List<Agendamento> findByData(LocalDate d); long countByData(LocalDate d);
 // usados pelas telas: já trazem pet e tutor junto
 @EntityGraph(attributePaths={"pet","pet.tutor"}) List<Agendamento> findAllByOrderByDataAscHorarioAsc();
 @EntityGraph(attributePaths={"pet","pet.tutor"}) List<Agendamento> findByDataOrderByHorarioAsc(LocalDate d);
 @EntityGraph(attributePaths={"pet","pet.tutor"}) Optional<Agendamento> findById(Long id);
 boolean existsByPetId(Long petId);}
