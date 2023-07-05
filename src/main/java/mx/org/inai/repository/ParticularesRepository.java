package mx.org.inai.repository;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.TrataMientoParticulares;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface ParticularesRepository extends JpaRepository<TrataMientoParticulares, Integer>{
	
	List<TrataMientoParticulares> findByActivo(String activo);

}
