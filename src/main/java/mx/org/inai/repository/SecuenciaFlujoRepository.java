package mx.org.inai.repository;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.SecuenciaFlujo;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface SecuenciaFlujoRepository extends JpaRepository<SecuenciaFlujo, Integer>{
	
	SecuenciaFlujo findByFolioAndEtapa(String folio, String etapa);
	List<SecuenciaFlujo> findByFolioOrderByIdSecuencia(String folio);
        
}
