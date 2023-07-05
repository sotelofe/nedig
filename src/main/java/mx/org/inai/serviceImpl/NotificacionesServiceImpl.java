package mx.org.inai.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.NotificacionesDTO;
import mx.org.inai.dto.ResponseNotificaciones;
import mx.org.inai.dto.UploadF;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.Notificaciones;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.NotificacionesRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.service.INotificacionesService;

@ConfigurationProperties("notificacion")
@Service
public class NotificacionesServiceImpl implements INotificacionesService{
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private NotificacionesRepository repository;
	
	@NotNull
    private String dirflujo1;
	
	@NotNull
    private String dirflujo2;
	
	@NotNull
    private String dirflujo3;
	
	private List<NotificacionesDTO> lista = null;
	
	@Autowired
	private UsuarioRepository repoUsuario;
	
	@Autowired
	private FlujoRepository repositoryFlujo;
	
	@Override
	public ResponseNotificaciones alta(NotificacionesDTO notificaciones) {
		ResponseNotificaciones response = new ResponseNotificaciones();
		Date objDate = new Date();
		SimpleDateFormat sfecha = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat shora = new SimpleDateFormat("hh: mm: ss");
		String fecha = sfecha.format(objDate);
		String hora = shora.format(objDate);
		String para = "";
		
		Flujo flujo = repositoryFlujo.findByFolio(notificaciones.getFolio());
        
		try {
        	if(flujo !=null ) {
        		
        		Usuario u = repoUsuario.findByIdUsuario(flujo.getIdUsuario());
        		
        		if(notificaciones.getIdPerfil()==1) {
        			para= "Sujeto obligado";
        			notificaciones.setIdUsuarioPara(u.getIdUsuario());
        		}else {
        			para= "Administrador";          			
        			notificaciones.setIdUsuarioPara(notificaciones.getIdUsuarioPara());
        		}
        		
        		
        	}else {
        		
        		Usuario u = repoUsuario.findByIdUsuario(notificaciones.getIdUsuarioPara());
        		
        		if(u.getIdPerfil()==1 && flujo==null) {
        			para= "Administrador";
        		}else {        			
        			para= "Sujeto obligado";
        		}
        		
        		notificaciones.setIdUsuarioPara(notificaciones.getIdUsuarioPara());
        	}
        	
        	notificaciones.setFecha(fecha);
        	notificaciones.setHora(hora);
        	notificaciones.setActivo(Constantes.ACTIVO);
        	notificaciones.setPara(para);
        	
        	if(notificaciones.getUpload() != null && !notificaciones.getUpload().getFilename().equals("")) {
        		notificaciones.setRutaSolicitud(getRutaSolicitud(notificaciones,"1"));
        	}
			
        	NotificacionesDTO noti = new NotificacionesDTO();
        	repository.save(noti.getEntidad(notificaciones));
        	
        	response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.NOTIFICACION_CORRECTA);
			
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			
		}
		
		
		return response;
	}
	
	private String getRutaSolicitud(NotificacionesDTO notificaciones, String tipo) {
		String ruta="";
		
		String folioDir = notificaciones.getFolio().replace('/', '_');
		folioDir = folioDir.replace('-', '_');
		ruta = dirflujo1 + folioDir + Constantes.SEPARADORD + tipo + Constantes.SEPARADORD;
		validaRutaGeneral(ruta);
		generaArchivo(ruta, notificaciones.getUpload());
		
		return ruta + notificaciones.getUpload().getFilename();
	}
	
	private void  generaArchivo(String ruta, UploadF upload) {
		
		try (FileOutputStream stream = new FileOutputStream(ruta+upload.getFilename())) {
			byte[] decodedBytes = Base64.getDecoder().decode(upload.getValue().getBytes("UTF-8"));
		    stream.write(decodedBytes);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResponseNotificaciones recuperarNotificaciones(NotificacionesDTO notificaciones) {
		ResponseNotificaciones response = new ResponseNotificaciones();
		lista = new ArrayList<NotificacionesDTO>();
		
		List<Notificaciones> listaEntity = new ArrayList<Notificaciones>();
		
		if(notificaciones.getIdPerfil()==1) {			
			listaEntity = repository.findByActivo(Constantes.ACTIVO);
		}else {			
			listaEntity = repository.findByIdUsuarioOrIdUsuarioParaAndActivo(notificaciones.getIdUsuario(),notificaciones.getIdUsuario(), Constantes.ACTIVO);
		}
		
		listaEntity.forEach(l->{
			NotificacionesDTO noti = new NotificacionesDTO(l);		
			Usuario u = repoUsuario.findById(l.getIdUsuarioPara()).get();
			Usuario ude = repoUsuario.findById(l.getIdUsuario()).get();
			noti.setPara(u.getUsuario());
			noti.setTipoMensaje(getTipoMensaje(notificaciones.getIdUsuario(), u.getIdUsuario()));
			noti.setDe(ude.getUsuario());
			lista.add(noti);
		});
		
		Comparator<NotificacionesDTO> compare = Comparator
                .comparing(NotificacionesDTO::getFecha)
                .thenComparing(NotificacionesDTO::getHora);
		
		lista = lista.stream().sorted(compare.reversed()).collect(Collectors.toList());
		
		response.setEstatus(Constantes.OK);		
		response.setMensaje("Exito");
		response.setNotificaciones(lista);
		
		return response;
	}
	
	private String getTipoMensaje(Integer idUusario, Integer idUsuarioPara) {
		String respuesta = "";
		if(idUusario == idUsuarioPara) {
			respuesta="Recibido";
		}else {
			respuesta="Enviado";
		}
		return respuesta;
	}
	
	@Override
	public ResponseNotificaciones bajaNotificaciones(NotificacionesDTO notificaciones) {
		ResponseNotificaciones response = new ResponseNotificaciones();
		try {
			//Notificaciones notifi = repository.findAll().stream().filter(f-> f.getFolio().equals(notificaciones.getFolio()) && f.getIdNotificacion() == notificaciones.getIdNotificacion()).findAny().orElse(null);
			Notificaciones notifi = repository.findByFolioAndIdNotificacion(notificaciones.getFolio(), notificaciones.getIdNotificacion());
			if(notifi!=null) {
				notifi.setActivo(Constantes.BAJA);
				repository.save(notifi);
				response.setEstatus(Constantes.OK);
				response.setMensaje(Constantes.BAJA_CORRECTA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

	private void validaRutaGeneral(String ruta) {
		 File directorio = new File(ruta);
	        if (!directorio.exists()) {
	            if (directorio.mkdirs()) {
	               //logger.info("Directorio creado");
	            } else {
	               // logger.info("Error al crear directorio");
	            }
	        }
	}

	@Override
	public ResponseEntity<InputStreamResource> descargaNotificaciones(String ruta) {
		String nuevaRuta = ruta.replaceAll("-", "/");
		String nombre = nuevaRuta.substring(nuevaRuta.lastIndexOf("/")+1);
		File file = new File(nuevaRuta);
		
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombre)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}

	@Override
	public ResponseNotificaciones respondeNotificaciones(NotificacionesDTO notificaciones) {
		ResponseNotificaciones response = new ResponseNotificaciones();
		
		try {
			if(notificaciones.getUpload() != null && !notificaciones.getUpload().getFilename().equals("")) {
        		notificaciones.setRutaRespuesta(getRutaSolicitud(notificaciones,"2"));
        	}
			
			//Notificaciones noti = repository.findAll().stream().filter(f-> f.getIdNotificacion() == notificaciones.getIdNotificacion()  && f.getFolio().equals(notificaciones.getFolio())).findAny().orElse(null);
			Notificaciones noti = repository.findByFolioAndIdNotificacion(notificaciones.getFolio(), notificaciones.getIdNotificacion());
			noti.setIdEstatus(2);
        	noti.setRutaRespuesta(notificaciones.getRutaRespuesta());
        	noti.setRespuesta(notificaciones.getRespuesta());
        	
        	repository.save(noti);
        	
        	response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.NOTIFICACION_ACTUALIZACION_CORRECTA);
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		return null;
	}
	
	
	public String getDirflujo1() {
		return dirflujo1;
	}

	public void setDirflujo1(String dirflujo1) {
		this.dirflujo1 = dirflujo1;
	}
	
	@Override
	public ResponseNotificaciones getAdministradores() {
		ResponseNotificaciones response = new ResponseNotificaciones();
		List<NotificacionesDTO> notificaciones = new ArrayList<NotificacionesDTO>();
		try {
			List<Usuario> listaAdmin = repoUsuario.findByIdPerfil(1);
			if(listaAdmin.size()>0) {
				listaAdmin.forEach(f->{
					NotificacionesDTO no = new NotificacionesDTO();
					no.setIdUsuario(f.getIdUsuario());
					no.setPara(f.getUsuario());
					notificaciones.add(no);
				});
			}
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
		} catch (Exception e) {
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}
	
	@Override
	public ResponseNotificaciones getUsuarios() {
		ResponseNotificaciones response = new ResponseNotificaciones();
		List<NotificacionesDTO> notificaciones = new ArrayList<NotificacionesDTO>();
		try {
			List<Usuario> listaAdmin = repoUsuario.findByIdPerfil(2);
			if(listaAdmin.size()>0) {
				listaAdmin.forEach(f->{
					NotificacionesDTO no = new NotificacionesDTO();
					no.setIdUsuario(f.getIdUsuario());
					no.setPara(f.getUsuario());
					notificaciones.add(no);
				});
			}
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
		} catch (Exception e) {
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}
	
	@Override
	public ResponseNotificaciones getUsuarioPorFolio(NotificacionesDTO noti) {
		ResponseNotificaciones response = new ResponseNotificaciones();
		List<NotificacionesDTO> notificaciones = new ArrayList<NotificacionesDTO>();
		try {
			Flujo flujo = repositoryFlujo.findByFolio(noti.getFolio());
			
			Usuario usuario = repoUsuario.findByIdUsuarioAndActivo(flujo.getIdUsuario(), Constantes.ACTIVO);
			
			if(usuario !=null) {				
					NotificacionesDTO no = new NotificacionesDTO();
					no.setIdUsuario(usuario.getIdUsuario());
					no.setPara(usuario.getUsuario());					
					notificaciones.add(no);				
			}
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
		} catch (Exception e) {
			response.setNotificaciones(notificaciones);
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}
}

	
	

