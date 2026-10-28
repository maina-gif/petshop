package br.com.petshop.repository; import br.com.petshop.entity.Pet; import org.springframework.data.jpa.repository.EntityGraph; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List; public interface PetRepository extends JpaRepository<Pet,Long>{List<Pet> findByNomeContainingIgnoreCase(String nome);
 // usados pelas telas: já trazem o tutor junto (evita LazyInitializationException no Thymeleaf)
 @EntityGraph(attributePaths="tutor") List<Pet> findAllByOrderByNomeAsc();
 @EntityGraph(attributePaths="tutor") List<Pet> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
 @EntityGraph(attributePaths="tutor") java.util.Optional<Pet> findComTutorById(Long id);
 boolean existsByTutorId(Long tutorId);}
