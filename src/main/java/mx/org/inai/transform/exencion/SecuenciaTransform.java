package mx.org.inai.transform.exencion;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.model.SecuenciaFlujo;
import mx.org.inai.repository.SecuenciaFlujoRepository;
import mx.org.inai.util.exencion.TipoEnvio;

/**
 * The Class FlujoTransform.
 *
 * @author A. Juarez
 */
@Component
@ConfigurationProperties("eipdp")
@Qualifier("secuenciaConverter")
public class SecuenciaTransform implements Transformer<SecuenciaFlujo, ExencionRequestDTO> {   
    
    @NotNull
    private String dirflujo3;
    
    @Autowired
    private SecuenciaFlujoRepository secuenciaRepository;

    private String ruta;
    /**
     * Transform.
     *
     * @param form the form
     * @return the flujo
     */
    @Override
    public SecuenciaFlujo transform(ExencionRequestDTO form) {
    	
    	SecuenciaFlujo flu = new SecuenciaFlujo();
		flu.setFolio(form.getFolio());
		flu.setEtapa(form.getEtapa());
		flu.setFecha(obtenerFecha().toString());
		flu.setRutaArchivo(getRuta(form));
		flu.setActivo("A");		
		 
        return flu;

    }
    
    @Override
    public SecuenciaFlujo transform(ExencionRequestDTO form, String ruta) {
    	
    	SecuenciaFlujo flu = new SecuenciaFlujo();
		flu.setFolio(form.getFolio());
		flu.setEtapa(form.getEtapa());
		flu.setFecha(obtenerFecha().toString());
		flu.setRutaArchivo(ruta);
		flu.setActivo("A");		
		 
        return flu;

    }
    
    @Override
    public SecuenciaFlujo transform(ExencionRequestDTO form, boolean actualizacion) {
    	SecuenciaFlujo flu = secuenciaRepository.findByFolioAndEtapa(form.getFolio(), Constantes.ACUERDO);
    	
    	if(actualizacion) {    		
    		flu.setRutaArchivo(getRuta(form));		
    	}
    					
        return flu;

    }
    
    private LocalDate obtenerFecha() {
		LocalDate date = LocalDate.now();
		return date;
	}
    
    private String getRuta(ExencionRequestDTO form) {
    	ruta = "" ;
    	String directorio = form.getFolio().replaceAll("/", "");
    	
    	 if (form.getTipoEnvio() == TipoEnvio.REGISTRAR) {
    		 ruta =  dirflujo3 + directorio + ".docx";
    	 }else {
    		 form.getPreguntas().forEach(f->{
    			 if(f.getSubpregunta()==1 && f.getArchivo() !=null) {
    				 ruta = dirflujo3 + directorio + Constantes.SEPARADORD + f.getPregunta() + Constantes.SEPARADORD + f.getSubpregunta() + Constantes.SEPARADORD + f.getArchivo().getFilename();
    			 }
    		 });    		 
    	 }
    	 
    	
    	return ruta;
    }

	public String getDirflujo3() {
		return dirflujo3;
	}

	public void setDirflujo3(String dirflujo3) {
		this.dirflujo3 = dirflujo3;
	}
    
}
