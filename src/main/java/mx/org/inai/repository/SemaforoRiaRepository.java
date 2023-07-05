package mx.org.inai.repository;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.SemaforoRia;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface SemaforoRiaRepository extends JpaRepository<SemaforoRia, Integer>{
	
	List<SemaforoRia> findByActivo(String activo);
	SemaforoRia findByFolioAndActivo(String folio, String activo);
	
}
