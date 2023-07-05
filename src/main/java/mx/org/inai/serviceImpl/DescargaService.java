package mx.org.inai.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Base64;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseArchivo;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.service.IDescargaService;

@ConfigurationProperties("eipdp")
@Service
public class DescargaService implements IDescargaService{

	@NotNull
    private String dirflujo1;
	
	@NotNull
    private String dirflujo2;
	
	@NotNull
    private String dirflujo3;
	
	@NotNull
    private String manual;
	
	@Autowired
	CuestionarioRepository cuestionarioRepositorio;
	
	private String pSfolio;
	
	@Override
	public ResponseEntity<InputStreamResource> descargarCuestionarioF1(String folio) {
		folio = folio.replace("/", "");
		String nombreFichero = folio+".docx";
		
		File file = new File(dirflujo1+nombreFichero);
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargarArchivoPorRuta(String nombreArchivo, Integer idFlujo, String carpeta, String pregunta)  {
		String ruta="";
		if(idFlujo==1) {
			ruta = dirflujo1+carpeta+Constantes.SEPARADORD+pregunta+Constantes.SEPARADORD+nombreArchivo;
		}else if(idFlujo==2) {
			ruta = dirflujo2+carpeta+Constantes.SEPARADORD+pregunta+Constantes.SEPARADORD+nombreArchivo;
		}else if(idFlujo==3) {
			ruta = dirflujo3+carpeta+Constantes.SEPARADORD+pregunta+Constantes.SEPARADORD+nombreArchivo;
		}
		
		File file = new File(ruta);
		
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreArchivo)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1(String pfolio)  {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,20)+"/"+pfolio.substring(20,23)+"/"+pfolio.substring(23,27);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 5, 1, Constantes.ACTIVO);
	
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}

	public String getDirflujo1() {
		return dirflujo1;
	}

	public void setDirflujo1(String dirflujo1) {
		this.dirflujo1 = dirflujo1;
	}
	
	public String getDirflujo2() {
		return dirflujo2;
	}

	public void setDirflujo2(String dirflujo2) {
		this.dirflujo2 = dirflujo2;
	}

	public String getDirflujo3() {
		return dirflujo3;
	}

	public void setDirflujo3(String dirflujo3) {
		this.dirflujo3 = dirflujo3;
	}

	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada(String pfolio) {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,20)+"/"+pfolio.substring(20,23)+"/"+pfolio.substring(23,27);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 7, 1, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada(String pfolio)  {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,20)+"/"+pfolio.substring(20,23)+"/"+pfolio.substring(23,27);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 8, 1, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargarCuestionarioF2(String folio)  {
		folio = folio.replace("/", "");
		String nombreFichero = folio+".docx";
		
		File file = new File(dirflujo2+nombreFichero);
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF2(String pfolio)  {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,17)+"/"+pfolio.substring(17,20)+"/"+pfolio.substring(20,24);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 15, 1, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoRiesgosF2(String pfolio) {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,17)+"/"+pfolio.substring(17,20)+"/"+pfolio.substring(20,24);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 17, 1, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada2(String pfolio) {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,17)+"/"+pfolio.substring(17,20)+"/"+pfolio.substring(20,24);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 20, 1, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada2(String pfolio)  {
		
		pSfolio = pfolio.substring(0,4)+"/"+pfolio.substring(4,8)+"/"+pfolio.substring(8,12)+"/"+pfolio.substring(12,17)+"/"+pfolio.substring(17,20)+"/"+pfolio.substring(20,24);
		
		Cuestionario cue = new Cuestionario();
		
		cue = cuestionarioRepositorio.findByFolioAndPreguntaAndSubPreguntaAndActivo(pSfolio, 20, 2, Constantes.ACTIVO);
		Integer pos = cue.getRutaArchivo().lastIndexOf("/");
		String nombreFichero = cue.getRutaArchivo().substring(pos+1);
		
		File file = new File(cue.getRutaArchivo());
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}
	
	@Override
	public ResponseEntity<InputStreamResource> descargarFormatos(String nombre) {
		
		String nombreFichero = nombre+".docx";
		
		File file = new File(manual+nombreFichero);
	    InputStreamResource resource;
		
	    try {
			resource = new InputStreamResource(new FileInputStream(file));
			 return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+nombreFichero)
			        .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
			        .body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	     return null;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}
	
	@Override
	public ResponseArchivo descargaArchivoRuta(RequestSecuencia request)  {	
		ResponseArchivo res = new ResponseArchivo();
		res.setBase64("");
		try {
			File pdfFile = new File(request.getFolio());					
			byte [] bytes = Files.readAllBytes(pdfFile.toPath());
            String b64 = Base64.getEncoder().encodeToString(bytes);			
			System.out.println(b64);
			res.setBase64(b64);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	
}
