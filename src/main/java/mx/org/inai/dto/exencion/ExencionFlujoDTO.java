package mx.org.inai.dto.exencion;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Class ExencionFlujoDTO.
 *
 * @author A. Juarez
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExencionFlujoDTO extends ExencionAbstractBaseDTO {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id flujo. */
	private Integer idFlujo;
    
    /** The id usuario. */
    private Integer idUsuario;
    
    /** The flujo. */
    private Integer flujo;
    
    /** The folio. */
    private String folio;
    
    /** The etapa. */
    private String etapa;
    
    /** The fecha. */
    private String fecha;
    
    /** The activo. */
    private String activo;
    
    /** The usuario. */
    private String usuario;
    
    /** The id color. */
    private Integer idColor;
    
    /** The tuvo ria. */
    private boolean tuvoRia;
    
    /** The tuvo ria no presentada. */
    private boolean tuvoRiaNoPresentada;
    
    private String tramite;
    
    private Integer permiteAcuerdo;
    
	private Integer idColorRia;

	public Integer getIdFlujo() {
		return idFlujo;
	}

	public void setIdFlujo(Integer idFlujo) {
		this.idFlujo = idFlujo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getFlujo() {
		return flujo;
	}

	public void setFlujo(Integer flujo) {
		this.flujo = flujo;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getIdColor() {
		return idColor;
	}

	public void setIdColor(Integer idColor) {
		this.idColor = idColor;
	}

	public boolean isTuvoRia() {
		return tuvoRia;
	}

	public void setTuvoRia(boolean tuvoRia) {
		this.tuvoRia = tuvoRia;
	}

	public boolean isTuvoRiaNoPresentada() {
		return tuvoRiaNoPresentada;
	}

	public void setTuvoRiaNoPresentada(boolean tuvoRiaNoPresentada) {
		this.tuvoRiaNoPresentada = tuvoRiaNoPresentada;
	}

	public Integer getPermiteAcuerdo() {
		return permiteAcuerdo;
	}

	public void setPermiteAcuerdo(Integer permiteAcuerdo) {
		this.permiteAcuerdo = permiteAcuerdo;
	}

	public Integer getIdColorRia() {
		return idColorRia;
	}

	public void setIdColorRia(Integer idColorRia) {
		this.idColorRia = idColorRia;
	}	
}
