package mx.org.inai.dto;

import java.io.Serializable;

import mx.org.inai.model.Flujo;

public class FlujoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer idFlujo;
	private Integer idUsuario;
	private Integer flujo;
	private String folio;
	private String etapa;
	private String fecha;
	private String activo;
	private String usuario;
	private Integer idColor;
	private boolean tuvoRia;
	private boolean tieneRiesgos;
	private boolean tieneRecomendaciones;
	private Integer idEtapa;
	private boolean tieneRiaNoPresentada;
	private String tramite;
	private Integer permiteAcuerdo;
	private Integer idColorRia;
	
	public FlujoDTO(Flujo flujo) {
		    super();
		    this.idFlujo= flujo.getIdFlujo();
		    this.idUsuario=flujo.getIdUsuario();
		    this.flujo=flujo.getFlujo();
		    this.folio=flujo.getFolio();
		    this.etapa=flujo.getEtapa();
		    this.fecha=flujo.getFecha();
		    this.activo=flujo.getActivo();
		    this.permiteAcuerdo = flujo.getPermiteAcuerdo();
		  }
	
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

	public boolean isTieneRiesgos() {
		return tieneRiesgos;
	}

	public void setTieneRiesgos(boolean tieneRiesgos) {
		this.tieneRiesgos = tieneRiesgos;
	}

	public boolean isTieneRecomendaciones() {
		return tieneRecomendaciones;
	}

	public void setTieneRecomendaciones(boolean tieneRecomendaciones) {
		this.tieneRecomendaciones = tieneRecomendaciones;
	}

	public Integer getIdEtapa() {
		return idEtapa;
	}

	public void setIdEtapa(Integer idEtapa) {
		this.idEtapa = idEtapa;
	}

	public boolean isTieneRiaNoPresentada() {
		return tieneRiaNoPresentada;
	}

	public void setTieneRiaNoPresentada(boolean tieneRiaNoPresentada) {
		this.tieneRiaNoPresentada = tieneRiaNoPresentada;
	}

	public String getTramite() {
		return tramite;
	}

	public void setTramite(String tramite) {
		this.tramite = tramite;
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
