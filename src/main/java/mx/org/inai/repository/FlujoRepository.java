package mx.org.inai.repository;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Flujo;
@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface FlujoRepository extends JpaRepository<Flujo, Integer>{
	@Query("SELECT f FROM Flujo f WHERE f.flujo = :flujo AND f.activo='A' ORDER BY f.folio desc")
    List<Flujo> findByFlujo(Integer flujo);

    @Query("SELECT f FROM Flujo f WHERE f.flujo = :flujo AND f.idUsuario=:idUsuario AND f.activo='A' ORDER BY f.folio desc")
    List<Flujo> findByFlujoAndUsuario(Integer flujo, Integer idUsuario);
    
    @Query("SELECT f FROM Flujo f WHERE f.folio = :folio AND f.activo='A'")
    Flujo findByFolio(String folio);
    
    List<Flujo> findByFlujoAndActivo(Integer flujo, String activo);
    
    List<Flujo> findByIdUsuarioAndFlujoAndActivo(Integer idUsuario, Integer flujo, String activo);
    
    Flujo findByFolioAndActivo(String folio, String activo);
    
    Flujo findByFolioAndFlujo(String folio, Integer flujo);
}
