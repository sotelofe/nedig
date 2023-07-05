package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOLIO")
public class Folio {

	@Id
	@GeneratedValue(generator="folio_idfolio_seq")
	@Column(name="idfolio")
    private Integer idFolio;
	
	@Column(name="folio")
	private Integer folio;
	
	@Column(name="activo")
	private String activo;

	public Integer getIdFolio() {
		return idFolio;
	}

	public void setIdFolio(Integer idFolio) {
		this.idFolio = idFolio;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
