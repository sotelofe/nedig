package mx.org.inai.repository;
import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import mx.org.inai.model.Cuestionario;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Integer>{
	@Query("SELECT c FROM Cuestionario c WHERE c.folio = :folio AND c.activo='A'")
    List<Cuestionario> findByFolio(String folio);

    @Query("SELECT c FROM Cuestionario c WHERE c.folio = :folio AND c.pregunta= :pregunta AND c.activo='A'")
    List<Cuestionario> findByFolioAndPregunta(String folio, Integer pregunta);
    
    Cuestionario findByFolioAndPreguntaAndSubPreguntaAndActivo(String folio,Integer pregunta, Integer subPregunta, String estatus);
    
    Cuestionario findByFolioAndPreguntaAndActivo(String folio,Integer pregunta, String estatus);
    
    List<Cuestionario> findByFolioAndActivo(String folio, String estatus);
    
    Cuestionario findByFolioAndPreguntaAndSubPregunta(String folio, Integer pregunta, Integer subPregunta);
       
}
