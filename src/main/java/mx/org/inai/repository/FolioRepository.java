package mx.org.inai.repository;
import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Folio;
@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface FolioRepository extends JpaRepository<Folio, Integer>{
	@Query("SELECT max(f.folio) + 1 FROM Folio f WHERE f.idFolio = 3 AND f.activo='A'")
    Optional<Integer> obtenerSiguienteFolio();

    @Query("SELECT f FROM Folio f WHERE f.folio=:folio AND f.idFolio = 3 AND f.activo='A'")
    Optional<Folio> findByFolio(Integer folio);
    
    Folio findByIdFolioAndActivo(Integer idFolio, String activo);
    
    
}
