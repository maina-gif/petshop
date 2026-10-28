package br.com.petshop.repository; import br.com.petshop.entity.Pet; import org.springframework.data.jpa.repository.EntityGraph; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List; import java.util.Optional; public interface PetRepository extends JpaRepository<Pet,Long>{List<Pet> findByNomeContainingIgnoreCase(String nome);
 // usados pelas telas: já trazem o tutor junto (evita LazyInitializationException no Thymeleaf)
 @EntityGraph(attributePaths="tutor") List<Pet> findAllByOrderByNomeAsc();
 @EntityGraph(attributePaths="tutor") List<Pet> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
 @EntityGraph(attributePaths="tutor") Optional<Pet> findById(Long id);
 boolean existsByTutorId(Long tutorId);}
