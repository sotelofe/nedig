package mx.org.inai.repository;
import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Dia;
@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface DiaRepository extends JpaRepository<Dia, Integer>{
	@Query("SELECT d FROM Dia d WHERE d.fecha=:fecha AND d.activo='A'")
    Optional<Dia> findByFecha(String fecha);
	
	@Query (value = "select d from Dia d WHERE d.fecha = :fecha and activo = :activo")
	Dia findByFechaActivo(String fecha, String activo);
}
