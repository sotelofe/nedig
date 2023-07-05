package mx.org.inai.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Catalogo;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer>{
	@Query("SELECT c FROM Catalogo c WHERE c.clave = :clave AND c.activo='A'")
    Optional<Catalogo> findByClave(String clave);

    @Query("SELECT c FROM Catalogo c WHERE c.idSubCatalogo = (SELECT a.idCatalogo FROM Catalogo a WHERE a.clave=:clave) ")
    List<Catalogo> findBySubCatalogo(String clave);
    
    Catalogo findByClaveAndActivo(String clave, String activo);
    
    List<Catalogo> findByIdSubCatalogoAndActivo(Integer idSubcatalogo, String activo);
    
    Catalogo findByValorAndActivo(String valor, String activo);
        
}
