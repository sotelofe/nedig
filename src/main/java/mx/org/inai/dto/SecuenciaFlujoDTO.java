package mx.org.inai.dto;

public class SecuenciaFlujoDTO {

	
    private Integer idSecuencia;
	
	
	private String folio;
	
	
	private String etapa;
	
	
	private String fecha;
	
	
	private String rutaArchivo;
	
	
	private String activo;

	public Integer getIdSecuencia() {
		return idSecuencia;
	}

	public void setIdSecuencia(Integer idSecuencia) {
		this.idSecuencia = idSecuencia;
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

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
