package mx.org.inai.repository;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Manuales;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface ManualesRepository extends JpaRepository<Manuales, Integer>{
	List<Manuales> findByActivoOrderByOrdenAsc(String activo);
}
