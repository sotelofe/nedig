package mx.org.inai.repository;

import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Periodo;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface PeriodoRepository extends JpaRepository<Periodo, Integer>{
	@Query("SELECT p FROM Periodo p WHERE p.folio=:folio AND p.activo='A'")
    Optional<Periodo> findByFolio(String folio);
	
	Periodo findByFolioAndActivo(String folio, String activo);
}
