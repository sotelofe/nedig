package mx.org.inai.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Menu;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface MenuRepository extends JpaRepository<Menu, Integer>{

}
