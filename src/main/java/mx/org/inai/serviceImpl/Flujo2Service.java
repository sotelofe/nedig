package mx.org.inai.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.AceptarFUnoDTO;
import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.CuestionarioDTO;
import mx.org.inai.dto.FlujoDTO;
import mx.org.inai.dto.FormPresentacionDTO;
import mx.org.inai.dto.ListaParticularDTO;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.Servidor;
import mx.org.inai.dto.SubeDocumento;
import mx.org.inai.dto.UploadF;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.model.Catalogo;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.model.Dia;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.Folio;
import mx.org.inai.model.Periodo;
import mx.org.inai.model.SecuenciaFlujo;
import mx.org.inai.model.SemaforoRia;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.repository.DiaRepository;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.FolioRepository;
import mx.org.inai.repository.PeriodoRepository;
import mx.org.inai.repository.SecuenciaFlujoRepository;
import mx.org.inai.repository.SemaforoRiaRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.service.ICatalogoService;
import mx.org.inai.service.ICorreoService;
import mx.org.inai.service.IFlujo2Service;

@ConfigurationProperties("eipdp")
@Service
public class Flujo2Service implements IFlujo2Service{
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FolioRepository folioRepositorio;
	
	@Autowired
	private FlujoRepository flujoRepositorio;
	
	@Autowired
	private CatalogoRepository catalogoRepositorio;
	
	@Autowired
	private CuestionarioRepository cuestionarioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Autowired
	ICorreoService iCorreoService;
	
	@NotNull
    private String dirflujo2;
	
	@Autowired
	ICatalogoService iCatalogoService;
	
	@Autowired
	PeriodoRepository periodoRepository;
	
	@Autowired
	DiaRepository diaRepository;
	
	@Autowired
	private SemaforoRiaRepository semaforoRepository;
	
	String sfechaFinal = null;
	
	private Usuario usuarios;
	
	String folioDir;
	
	@NotNull
    private String dirflujo3;
	
	@Autowired
	SecuenciaFlujoRepository secuenciaReposiroty;
	
	@Override
	public ResponseUsuario altaFlujo2(FormPresentacionDTO presentacion) {
		ResponseUsuario response = new ResponseUsuario();
		String folio = "";
		String etapa = "";
		Flujo flu = null;
		Cuestionario cue = null;
		
        try {
        	 folio  = generarFolio();
        	 generaFolioDirectorio(folio);
        	 etapa  = obtenerEtapa();
        	 flu    = altaFlujo(presentacion, folio, etapa);
        	 altaObligatorio(presentacion, folio);
        	 cue    = altaNoObligatorio(presentacion, folio);
        	 generaDocumento(folio, presentacion);
        	 enviarCorreoAltaFlujo2(presentacion, folio);
        	 actualizaPeriodoValido(folio);
        	 
        	 String fol = folio.replaceAll("/", "");
        	 altaSecuenciaFlujo(folio, etapa, dirflujo2+fol+".docx");
        	 
        	 if(flu.getIdFlujo() !=null) {
	        	response.setEstatus(Constantes.OK);
	 			response.setMensaje(folio);
        	 }else {
        		 response.setEstatus(Constantes.FAIL);
 	 			response.setMensaje("");
        	 }
        	 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private void altaSecuenciaFlujo(String folio, String etapa, String rutaArchivo) {
		SecuenciaFlujo flu = new SecuenciaFlujo();
		flu.setFolio(folio);
		flu.setEtapa(etapa);
		flu.setFecha(obtenerFecha().toString());
		flu.setRutaArchivo(rutaArchivo);
		flu.setActivo("A");		
		secuenciaReposiroty.save(flu);
		
	}
	
	private String generarFolio() {
		String sFolio = "";
		Integer numeroFolio = 0;
		String sNumeroFolio = "";
		String clave="";
		LocalDate fecha=null;
		
		clave = obtenerClave(Constantes.KEY_FOLIO, Constantes.KEY_FOLIO2 );
		numeroFolio = obtenerSecuenciaFlujo();
		sNumeroFolio = generaCadenaSecuenciaFolio(numeroFolio);
		fecha = obtenerFecha();
		
		sFolio = clave+Constantes.SEPARADORD+ sNumeroFolio +Constantes.SEPARADORD+fecha.getYear();
		
		return sFolio;
	}
	
	private void generaFolioDirectorio(String folio) {
		folioDir = "";
		folioDir = folio.replaceAll("/", "_");
		folioDir = folioDir.replace('-', '_');
	}
	
	private String obtenerClave(String llavePadre, String llaveHija) {
		String clave ="";
		
		//Catalogo catalogo = catalogoRepositorio.findAll().stream().filter(c -> c.getClave().equals(llavePadre) && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Catalogo catalogo = catalogoRepositorio.findByClaveAndActivo(llavePadre, Constantes.ACTIVO);
		//List<Catalogo> listCatalogo = catalogoRepositorio.findAll().stream().filter(ca ->  ca.getIdSubCatalogo() == catalogo.getIdCatalogo()).collect(Collectors.toList());
		List<Catalogo> listCatalogo = catalogoRepositorio.findByIdSubCatalogoAndActivo(catalogo.getIdCatalogo(), Constantes.ACTIVO);
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals(llaveHija)) {
				clave = listCatalogo.get(i).getValor();
				break;
			}
		}
		
				
		return clave;		
	}
	
	private Integer obtenerSecuenciaFlujo() {
		Integer iFolio = 1;
		Folio folio = null;
		
		//folio = folioRepositorio.findAll().stream().filter(fo -> fo.getIdFolio() == 2 && fo.getActivo().equals("A")).findAny().orElse(null);
		folio = folioRepositorio.findByIdFolioAndActivo(2, Constantes.ACTIVO);
		if(folio==null){
			folio = new Folio();
			iFolio = 1;
			folio.setFolio(iFolio);
         	folio.setActivo("A");
         	folioRepositorio.save(folio);
        	
        }else {
			iFolio = folio.getFolio();
        	iFolio = iFolio + 1;
        	folio.setFolio(iFolio);
         	folio.setActivo("A");
        	folioRepositorio.save(folio);
		}
		
		return iFolio;
	}
	
	private String generaCadenaSecuenciaFolio(Integer secuencia) {
		String cadena = "";
		String iCadena = String.valueOf(secuencia);
		
		/*
		if(iCadena.length()==1) {
			cadena = "000000000"+iCadena;
		}else if(iCadena.length()==2) {
			cadena = "00000000"+iCadena;
		}else if(iCadena.length()==3) {
			cadena = "0000000"+iCadena;
		}else if(iCadena.length()==4) {
			cadena = "000000"+iCadena;
		}else if(iCadena.length()==5) {
			cadena = "00000"+iCadena;
		}else if(iCadena.length()==6) {
			cadena = "0000"+iCadena;
		}else if(iCadena.length()==7) {
			cadena = "000"+iCadena;
		}else if(iCadena.length()==8) {
			cadena = "00"+iCadena;
		}else if(iCadena.length()==9) {
			cadena = "0"+iCadena;
		}else if(iCadena.length()==10) {
			cadena = iCadena;
		}else if(iCadena.length()>10) {
			cadena = iCadena;
		}
		*/
		
		if(iCadena.length()==1) {
			cadena = "00"+iCadena;
		}else if(iCadena.length()==2) {
			cadena = "0"+iCadena;
		}else if(iCadena.length()==3) {
			cadena = iCadena;
		}
		
		return cadena;
	}

	private LocalDate obtenerFecha() {
		LocalDate date = LocalDate.now();
		return date;
	}

	private String obtenerEtapa() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH1);
		
		return etapa;
	}

	private Flujo altaFlujo(FormPresentacionDTO presentacion, String folio, String etapa) {
		Flujo flu = new Flujo();
		flu.setIdUsuario(presentacion.getIdUsuario());
		flu.setFlujo(2);
		flu.setFolio(folio);
		flu.setEtapa(etapa);
		flu.setFecha(obtenerFecha().toString());
		flu.setActivo("A");
		flujoRepositorio.save(flu);
		
		return flu;
	}

	private Cuestionario altaNoObligatorio(FormPresentacionDTO presentacion, String folio) {
		Cuestionario preCue = new Cuestionario();
		Cuestionario preCue1 = new Cuestionario();
		Cuestionario preCue2 = new Cuestionario();
		Cuestionario preCue3 = new Cuestionario();
		
		
		if(presentacion.getRadioEvaluacionImpacto() !=null && !presentacion.getRadioEvaluacionImpacto().equals("")) {
			preCue.setFolio(folio);
			preCue.setPregunta(14);
			preCue.setRespuesta(presentacion.getRadioEvaluacionImpacto());
			preCue.setActivo("A");
			cuestionarioRepository.save(preCue);
		}
		
		if(presentacion.getRadioEvaluacionImpacto() !=null && presentacion.getRadioEvaluacionImpacto().equals("Si")) {
			
			if(!presentacion.getEvaUno().equals("")) {
				preCue1.setFolio(folio);
				preCue1.setPregunta(14);
				preCue1.setSubPregunta(1);
				preCue1.setRespuesta(presentacion.getEvaUno());
				preCue1.setActivo("A");
				cuestionarioRepository.save(preCue1);
			}
			
			if(!presentacion.getEvaDos().equals("")) {
				preCue2.setFolio(folio);
				preCue2.setPregunta(14);
				preCue2.setSubPregunta(2);
				preCue2.setRespuesta(presentacion.getEvaDos());
				preCue2.setActivo("A");
				cuestionarioRepository.save(preCue2);
			}
			
			if(!presentacion.getEvaTres().equals("")) {
				preCue3.setFolio(folio);
				preCue3.setPregunta(14);
				preCue3.setSubPregunta(3);
				preCue3.setRespuesta(presentacion.getEvaTres());
				preCue3.setActivo("A");
				cuestionarioRepository.save(preCue3);
			}
		}
		
		return preCue;
	}
	
	private void  generaArchivo(String ruta, UploadF upload) {
		
		try (FileOutputStream stream = new FileOutputStream(ruta)) {
			byte[] decodedBytes = Base64.getDecoder().decode(upload.getValue().getBytes("UTF-8"));
		    stream.write(decodedBytes);
		}catch (Exception e) {
			//logger.error(e.getMessage());
		}
	}
	
	private Cuestionario altaObligatorio(FormPresentacionDTO presentacion, String folio) {
		Cuestionario cue = new Cuestionario();
		Cuestionario cue1 = new Cuestionario();
		Cuestionario cue2 = new Cuestionario();
		Cuestionario cue3 = new Cuestionario();
		Cuestionario cue4 = new Cuestionario();
		Cuestionario cue5 = new Cuestionario();
		Cuestionario cue6 = new Cuestionario();
		Cuestionario cue7 = new Cuestionario();
		Cuestionario cue8 = new Cuestionario();
		Cuestionario cue9 = new Cuestionario();
		Cuestionario cue10 = new Cuestionario();
		Cuestionario cue11 = new Cuestionario();
		Cuestionario cue12 = new Cuestionario();
		Cuestionario cue13 = new Cuestionario();
		Cuestionario cue14 = new Cuestionario();
		Cuestionario cue15 = new Cuestionario();
		Cuestionario cue16 = new Cuestionario();
		Cuestionario cue17 = new Cuestionario();
		Cuestionario cue18 = new Cuestionario();
		Cuestionario cue19 = new Cuestionario();
		Cuestionario cue20 = new Cuestionario();
		Cuestionario cue21 = new Cuestionario();
		Cuestionario cue22 = new Cuestionario();
		Cuestionario cue23 = new Cuestionario();
		Cuestionario cue24 = new Cuestionario();
		Cuestionario cue25 = new Cuestionario();
		Cuestionario cue26 = new Cuestionario();
		Cuestionario cue27 = new Cuestionario();
		Cuestionario cue28 = new Cuestionario();
		Cuestionario cue29 = new Cuestionario();
		Cuestionario cue30 = new Cuestionario();
		Cuestionario cue31 = new Cuestionario();
		Cuestionario cue32 = new Cuestionario();
		Cuestionario cue33 = new Cuestionario();
		Cuestionario cue34 = new Cuestionario();
		Cuestionario cue35 = new Cuestionario();
		Cuestionario cue36 = new Cuestionario();
		Cuestionario cue37 = new Cuestionario();
		Cuestionario cue38 = new Cuestionario();
		Cuestionario cue39 = new Cuestionario();
		Cuestionario cue40 = new Cuestionario();
		Cuestionario cue41 = new Cuestionario();
		Cuestionario cue42 = new Cuestionario();
		Cuestionario cue43 = new Cuestionario();
		Cuestionario cue44 = new Cuestionario();
		Cuestionario cue45 = new Cuestionario();
		Cuestionario cue46 = new Cuestionario();
		Cuestionario cue47 = new Cuestionario();
		Cuestionario cue48 = new Cuestionario();
		Cuestionario cue49 = new Cuestionario();
		Cuestionario cue50 = new Cuestionario();
		Cuestionario cue51 = new Cuestionario();
		Cuestionario cue52 = new Cuestionario();
		Cuestionario cue53 = new Cuestionario();
		Cuestionario cue54 = new Cuestionario();
		Cuestionario cue55 = new Cuestionario();
		Cuestionario cue56 = new Cuestionario();
		Cuestionario cue57 = new Cuestionario();
		Cuestionario cue58 = new Cuestionario();
		Cuestionario cue59 = new Cuestionario();
		Cuestionario cue60 = new Cuestionario();
		Cuestionario cue61 = new Cuestionario();
		Cuestionario cue62 = new Cuestionario();
		Cuestionario cue63 = new Cuestionario();
		
		String ruta1 = dirflujo2+folioDir+Constantes.SEPARADORD+"1"+Constantes.SEPARADORD;
		String ruta2 = dirflujo2+folioDir+Constantes.SEPARADORD+"2"+Constantes.SEPARADORD;
		String ruta3 = dirflujo2+folioDir+Constantes.SEPARADORD+"3"+Constantes.SEPARADORD;
		String ruta4 = dirflujo2+folioDir+Constantes.SEPARADORD+"4"+Constantes.SEPARADORD;
		String ruta5 = dirflujo2+folioDir+Constantes.SEPARADORD+"5"+Constantes.SEPARADORD;
		String ruta6 = dirflujo2+folioDir+Constantes.SEPARADORD+"6"+Constantes.SEPARADORD;
		String ruta7 = dirflujo2+folioDir+Constantes.SEPARADORD+"7"+Constantes.SEPARADORD;
		String ruta8 = dirflujo2+folioDir+Constantes.SEPARADORD+"8"+Constantes.SEPARADORD;
		
		cue.setFolio(folio);
		cue.setPregunta(1);
		cue1.setSubPregunta(1);
		cue.setRespuesta(presentacion.getDenominacion());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
		cue1.setFolio(folio);
		cue1.setPregunta(2);
		cue1.setSubPregunta(1);
		cue1.setRespuesta(presentacion.getNomPolPub());
		cue1.setActivo("A");
		cuestionarioRepository.save(cue1);
		
		cue2.setFolio(folio);
		cue2.setPregunta(3);
		cue2.setSubPregunta(1);
		cue2.setRespuesta(presentacion.getObjGen());
		cue2.setActivo("A");
		cuestionarioRepository.save(cue2);
		
		cue3.setFolio(folio);
		cue3.setPregunta(4);
		cue3.setSubPregunta(1);
		cue3.setRespuesta(presentacion.getObjEsp());
		cue3.setActivo("A");
		cuestionarioRepository.save(cue3);
		
		
		
		if(presentacion.getListaServidor().size()>0) {
			int i=1;
			for(Servidor ser: presentacion.getListaServidor()) {
				
				Cuestionario cn = new Cuestionario();
				cn.setFolio(folio);
				cn.setPregunta(5);
				cn.setSubPregunta(i);
				cn.setRespuesta(ser.getNombre());
				cn.setRutaArchivo("nombre");
				cn.setActivo("A");
				cuestionarioRepository.save(cn);
				
				Cuestionario cn1 = new Cuestionario();
				cn1.setFolio(folio);
				cn1.setPregunta(5);
				cn1.setSubPregunta(i);
				cn1.setRespuesta(ser.getCargo());
				cn1.setRutaArchivo("cargo");
				cn1.setActivo("A");
				cuestionarioRepository.save(cn1);
				
				i++;
			}
			
		}
		
		
		
		
		cue4.setFolio(folio);
		cue4.setPregunta(6);
		cue4.setSubPregunta(1);
		cue4.setRespuesta(presentacion.getObjetivosGenerales());
		cue4.setActivo("A");
		cuestionarioRepository.save(cue4);
		
		cue5.setFolio(folio);
		cue5.setPregunta(6);
		cue5.setSubPregunta(2);
		cue5.setRespuesta(presentacion.getObjetivosEspecificos());
		cue5.setActivo("A");
		cuestionarioRepository.save(cue5);
		
		cue6.setFolio(folio);
		cue6.setPregunta(6);
		cue6.setSubPregunta(3);
		cue6.setRespuesta(presentacion.getObjetivosLegal());
		cue6.setActivo("A");
		cuestionarioRepository.save(cue6);
		
		if(presentacion.getObjetivosLegal().equals("Si")) {
			cue7.setFolio(folio);
			cue7.setPregunta(6);
			cue7.setSubPregunta(4);
			cue7.setRespuesta(presentacion.getHipervinculo());
			cue7.setActivo("A");
			cuestionarioRepository.save(cue7);
		}
		
		
		
		
		cue8.setFolio(folio);
		cue8.setPregunta(6);
		cue8.setSubPregunta(5);
		cue8.setRespuesta(presentacion.getCategorias());
		cue8.setActivo("A");
		cuestionarioRepository.save(cue8);
		
		cue9.setFolio(folio);
		cue9.setPregunta(6);
		cue9.setSubPregunta(6);
		cue9.setRespuesta(presentacion.getGrupVul());
		cue9.setActivo("A");
		cuestionarioRepository.save(cue9);
		
		cue10.setFolio(folio);
		cue10.setPregunta(6);
		cue10.setSubPregunta(7);
		cue10.setRespuesta(presentacion.getDatPerTra());
		cue10.setActivo("A");
		cuestionarioRepository.save(cue10);
		
		cue11.setFolio(folio);
		cue11.setPregunta(6);
		cue11.setSubPregunta(8);
		cue11.setRespuesta(presentacion.getDatPerSen());
		cue11.setActivo("A");
		cuestionarioRepository.save(cue11);
		
		cue12.setFolio(folio);
		cue12.setPregunta(6);
		cue12.setSubPregunta(9);
		cue12.setRespuesta(presentacion.getFinTra());
		cue12.setActivo("A");
		cuestionarioRepository.save(cue12);
		
		cue13.setFolio(folio);
		cue13.setPregunta(6);
		cue13.setSubPregunta(10);
		cue13.setRespuesta(presentacion.getNuProTra());
		cue13.setActivo("A");
		cuestionarioRepository.save(cue13);
		
		cue14.setFolio(folio);
		cue14.setPregunta(6);
		cue14.setSubPregunta(11);
		cue14.setRespuesta(presentacion.getViDePro());
		cue14.setActivo("A");
		cuestionarioRepository.save(cue14);
		
		if(!presentacion.getFormaRecaban().getPersonal().equals("")) {
			cue15.setFolio(folio);
			cue15.setPregunta(6);
			cue15.setSubPregunta(12);
			cue15.setRespuesta(presentacion.getFormaRecaban().getPersonal());
			cue15.setActivo("A");
			cuestionarioRepository.save(cue15);
		}
		
		if(!presentacion.getFormaRecaban().getDirecta().equals("")) {
			cue16.setFolio(folio);
			cue16.setPregunta(6);
			cue16.setSubPregunta(13);
			cue16.setRespuesta(presentacion.getFormaRecaban().getDirecta());
			cue16.setActivo("A");
			cuestionarioRepository.save(cue16);
		}
		
		if(!presentacion.getFormaRecaban().getIndirecta().equals("")) {
			cue17.setFolio(folio);
			cue17.setPregunta(6);
			cue17.setSubPregunta(14);
			cue17.setRespuesta(presentacion.getFormaRecaban().getIndirecta());
			cue17.setActivo("A");
			cuestionarioRepository.save(cue17);
		}
		
		
		cue18.setFolio(folio);
		cue18.setPregunta(6);
		cue18.setSubPregunta(15);
		cue18.setRespuesta(presentacion.getFuProDat());
		cue18.setActivo("A");
		cuestionarioRepository.save(cue18);
		
		cue19.setFolio(folio);
		cue19.setPregunta(6);
		cue19.setSubPregunta(16);
		cue19.setRespuesta(presentacion.getTraDatPer());
		cue19.setActivo("A");
		cuestionarioRepository.save(cue19);
		
		cue20.setFolio(folio);
		cue20.setPregunta(6);
		cue20.setSubPregunta(17);
		cue20.setRespuesta(presentacion.getTipTraDatPer());
		cue20.setActivo("A");
		cuestionarioRepository.save(cue20);
		
		cue21.setFolio(folio);
		cue21.setPregunta(6);
		cue21.setSubPregunta(18);
		cue21.setRespuesta(presentacion.getSecTraDatPer());
		cue21.setActivo("A");
		cuestionarioRepository.save(cue21);
		
		cue22.setFolio(folio);
		cue22.setPregunta(6);
		cue22.setSubPregunta(19);
		cue22.setRespuesta(presentacion.getTieDurTra());
		cue22.setActivo("A");
		cuestionarioRepository.save(cue22);
		
		cue23.setFolio(folio);
		cue23.setPregunta(6);
		cue23.setSubPregunta(20);
		cue23.setRespuesta(presentacion.getIntRel());
		cue23.setActivo("A");
		cuestionarioRepository.save(cue23);
		
		cue24.setFolio(folio);
		cue24.setPregunta(6);
		cue24.setSubPregunta(21);
		cue24.setRespuesta(presentacion.getMedSeg());
		cue24.setActivo("A");
		cuestionarioRepository.save(cue24);
		
		cue25.setFolio(folio);
		cue25.setPregunta(6);
		cue25.setSubPregunta(22);
		cue25.setRespuesta(presentacion.getInfoAdi());
		cue25.setActivo("A");
		cuestionarioRepository.save(cue25);
		
		cue26.setFolio(folio);
		cue26.setPregunta(6);
		cue26.setSubPregunta(23);
		cue26.setRutaArchivo(ruta1+presentacion.getArchivo1().getFilename());
		cue26.setActivo("A");
		cuestionarioRepository.save(cue26);
		
		cue27.setFolio(folio);
		cue27.setPregunta(7);
		cue27.setSubPregunta(1);
		cue27.setRespuesta(presentacion.getSusIdo());
		cue27.setActivo("A");
		cuestionarioRepository.save(cue27);
		
		cue28.setFolio(folio);
		cue28.setPregunta(7);
		cue28.setSubPregunta(2);
		cue28.setRespuesta(presentacion.getProEst());
		cue28.setActivo("A");
		cuestionarioRepository.save(cue28);
		
		cue29.setFolio(folio);
		cue29.setPregunta(7);
		cue29.setSubPregunta(3);
		cue29.setRespuesta(presentacion.getEquFun());
		cue29.setActivo("A");
		cuestionarioRepository.save(cue29);
		
		cue57.setFolio(folio);
		cue57.setPregunta(7);
		cue57.setSubPregunta(4);
		cue57.setRutaArchivo(ruta2+presentacion.getArchivo2().getFilename());
		cue57.setActivo("A");
		cuestionarioRepository.save(cue57);
		
		if(!presentacion.getCheckTipoTratamiento().getObtencion().equals("")) {
			cue30.setFolio(folio);
			cue30.setPregunta(8);
			cue30.setSubPregunta(1);
			cue30.setRespuesta(presentacion.getCheckTipoTratamiento().getObtencion());
			cue30.setActivo("A");
			cuestionarioRepository.save(cue30);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getAprovechamiento().equals("")) {
			cue31.setFolio(folio);
			cue31.setPregunta(8);
			cue31.setSubPregunta(2);
			cue31.setRespuesta(presentacion.getCheckTipoTratamiento().getAprovechamiento());
			cue31.setActivo("A");
			cuestionarioRepository.save(cue31);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getExplotacion().equals("")) {
			cue32.setFolio(folio);
			cue32.setPregunta(8);
			cue32.setSubPregunta(3);
			cue32.setRespuesta(presentacion.getCheckTipoTratamiento().getExplotacion());
			cue32.setActivo("A");
			cuestionarioRepository.save(cue32);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getAlmacenamiento().equals("")) {
			cue33.setFolio(folio);
			cue33.setPregunta(8);
			cue33.setSubPregunta(4);
			cue33.setRespuesta(presentacion.getCheckTipoTratamiento().getAlmacenamiento());
			cue33.setActivo("A");
			cuestionarioRepository.save(cue33);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getConservacion().equals("")) {
			cue34.setFolio(folio);
			cue34.setPregunta(8);
			cue34.setSubPregunta(5);
			cue34.setRespuesta(presentacion.getCheckTipoTratamiento().getConservacion());
			cue34.setActivo("A");
			cuestionarioRepository.save(cue34);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getSupresion().equals("")) {
			cue35.setFolio(folio);
			cue35.setPregunta(8);
			cue35.setSubPregunta(6);
			cue35.setRespuesta(presentacion.getCheckTipoTratamiento().getSupresion());
			cue35.setActivo("A");
			cuestionarioRepository.save(cue35);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getOtra().equals("")) {
			cue36.setFolio(folio);
			cue36.setPregunta(8);
			cue36.setSubPregunta(7);
			cue36.setRespuesta(presentacion.getCheckTipoTratamiento().getOtra());
			cue36.setActivo("A");
			cuestionarioRepository.save(cue36);
		}
		
		if(!presentacion.getCheckTipoTratamiento().getOtra().equals("")) {
			if(!presentacion.getOtroTratamiento().equals("")) {
				cue37.setFolio(folio);
				cue37.setPregunta(8);
				cue37.setSubPregunta(8);
				cue37.setRespuesta(presentacion.getOtroTratamiento());
				cue37.setActivo("A");
				cuestionarioRepository.save(cue37);
			}
		}
		
		cue38.setFolio(folio);
		cue38.setPregunta(8);
		cue38.setSubPregunta(9);
		cue38.setRespuesta(presentacion.getFueInt());
		cue38.setActivo("A");
		cuestionarioRepository.save(cue38);
		
		cue39.setFolio(folio);
		cue39.setPregunta(8);
		cue39.setSubPregunta(10);
		cue39.setRespuesta(presentacion.getAreGruPer());
		cue39.setActivo("A");
		cuestionarioRepository.save(cue39);
		
		cue40.setFolio(folio);
		cue40.setPregunta(8);
		cue40.setSubPregunta(11);
		cue40.setRespuesta(presentacion.getTraEnc());
		cue40.setActivo("A");
		cuestionarioRepository.save(cue40);
		
		cue41.setFolio(folio);
		cue41.setPregunta(8);
		cue41.setSubPregunta(12);
		cue41.setRespuesta(presentacion.getSerNub());
		cue41.setActivo("A");
		cuestionarioRepository.save(cue41);
		
		cue42.setFolio(folio);
		cue42.setPregunta(8);
		cue42.setSubPregunta(13);
		cue42.setRespuesta(presentacion.getTraResp());
		cue42.setActivo("A");
		cuestionarioRepository.save(cue42);
		
		if(presentacion.getTraResp().equals("Si")) {
			cue43.setFolio(folio);
			cue43.setPregunta(8);
			cue43.setSubPregunta(14);
			cue43.setRespuesta(presentacion.getTraRespSec());
			cue43.setActivo("A");
			cuestionarioRepository.save(cue43);
		}
		
		
		cue44.setFolio(folio);
		cue44.setPregunta(8);
		cue44.setSubPregunta(15);
		cue44.setRespuesta(presentacion.getPlaConAlm());
		cue44.setActivo("A");
		cuestionarioRepository.save(cue44);
		
		cue45.setFolio(folio);
		cue45.setPregunta(8);
		cue45.setSubPregunta(16);
		cue45.setRespuesta(presentacion.getTecUti());
		cue45.setActivo("A");
		cuestionarioRepository.save(cue45);
		
		cue58.setFolio(folio);
		cue58.setPregunta(8);
		cue58.setSubPregunta(17);
		cue58.setRutaArchivo(ruta3+presentacion.getArchivo3().getFilename());
		cue58.setActivo("A");
		cuestionarioRepository.save(cue58);
		
		cue46.setFolio(folio);
		cue46.setPregunta(9);
		cue46.setSubPregunta(1);
		cue46.setRespuesta(presentacion.getIdeDes());
		cue46.setActivo("A");
		cuestionarioRepository.save(cue46);
		
		cue47.setFolio(folio);
		cue47.setPregunta(9);
		cue47.setSubPregunta(2);
		cue47.setRespuesta(presentacion.getPonCuan());
		cue47.setActivo("A");
		cuestionarioRepository.save(cue47);
		
		cue48.setFolio(folio);
		cue48.setPregunta(9);
		cue48.setSubPregunta(3);
		cue48.setRespuesta(presentacion.getMedCon());
		cue48.setActivo("A");
		cuestionarioRepository.save(cue48);
		
		cue63.setFolio(folio);
		cue63.setPregunta(9);
		cue63.setSubPregunta(4);
		cue63.setRutaArchivo(ruta4+presentacion.getArchivo4().getFilename());
		cue63.setActivo("A");
		cuestionarioRepository.save(cue63);
		
		cue49.setFolio(folio);
		cue49.setPregunta(10);
		cue49.setSubPregunta(1);
		cue49.setRespuesta(presentacion.getCumPri());
		cue49.setActivo("A");
		cuestionarioRepository.save(cue49);
		
		cue50.setFolio(folio);
		cue50.setPregunta(10);
		cue50.setSubPregunta(2);
		cue50.setRespuesta(presentacion.getCumDeb());
		cue50.setActivo("A");
		cuestionarioRepository.save(cue50);
		
		cue51.setFolio(folio);
		cue51.setPregunta(10);
		cue51.setSubPregunta(3);
		cue51.setRespuesta(presentacion.getCumEsp());
		cue51.setActivo("A");
		cuestionarioRepository.save(cue51);
		
		cue52.setFolio(folio);
		cue52.setPregunta(10);
		cue52.setSubPregunta(4);
		cue52.setRespuesta(presentacion.getVinEje());
		cue52.setActivo("A");
		cuestionarioRepository.save(cue52);
		
		cue59.setFolio(folio);
		cue59.setPregunta(10);
		cue59.setSubPregunta(5);
		cue59.setRutaArchivo(ruta5+presentacion.getArchivo5().getFilename());
		cue59.setActivo("A");
		cuestionarioRepository.save(cue59);
		
		cue53.setFolio(folio);
		cue53.setPregunta(11);
		cue53.setSubPregunta(1);
		cue53.setRespuesta(presentacion.getConExt());
		cue53.setActivo("A");
		cuestionarioRepository.save(cue53);
		
		cue60.setFolio(folio);
		cue60.setPregunta(11);
		cue60.setSubPregunta(2);
		cue60.setRutaArchivo(ruta6+presentacion.getArchivo6().getFilename());
		cue60.setActivo("A");
		cuestionarioRepository.save(cue60);
		
		cue54.setFolio(folio);
		cue54.setPregunta(12);
		cue54.setSubPregunta(1);
		cue54.setRespuesta(presentacion.getReaOpi());
		cue54.setActivo("A");
		cuestionarioRepository.save(cue54);
		
		cue61.setFolio(folio);
		cue61.setPregunta(12);
		cue61.setSubPregunta(2);
		cue61.setRutaArchivo(ruta7+presentacion.getArchivo7().getFilename());
		cue61.setActivo("A");
		cuestionarioRepository.save(cue61);
		
		cue55.setFolio(folio);
		cue55.setPregunta(13);
		cue55.setSubPregunta(1);
		cue55.setRespuesta(presentacion.getDescGen());
		cue55.setActivo("A");
		cuestionarioRepository.save(cue55);
		
		cue56.setFolio(folio);
		cue56.setPregunta(13);
		cue56.setSubPregunta(2);
		cue56.setRespuesta(presentacion.getDescAne());
		cue56.setActivo("A");
		cuestionarioRepository.save(cue56);
		
		cue62.setFolio(folio);
		cue62.setPregunta(13);
		cue62.setSubPregunta(3);
		cue62.setRutaArchivo(ruta8+presentacion.getArchivo8().getFilename());
		cue62.setActivo("A");
		cuestionarioRepository.save(cue62);

		Cuestionario cue64 = new Cuestionario();
		cue64.setFolio(folio);
		cue64.setPregunta(22);
		cue64.setSubPregunta(1);
		cue64.setRespuesta(presentacion.getTratamiento().getGeneral().getCheckGeneral().toString());
		cue64.setActivo("A");
		cuestionarioRepository.save(cue64);
		
		if(presentacion.getTratamiento().getGeneral().getCheckGeneral()) {
			cue64 = new Cuestionario();
			cue64.setFolio(folio);
			cue64.setPregunta(22);
			cue64.setSubPregunta(2);
			cue64.setRespuesta(presentacion.getTratamiento().getGeneral().getValGeneral());
			cue64.setActivo("A");
			cuestionarioRepository.save(cue64);
		}
		
		cue64 = new Cuestionario();
		cue64.setFolio(folio);
		cue64.setPregunta(22);
		cue64.setSubPregunta(3);
		cue64.setRespuesta(presentacion.getTratamiento().getParticular().getCheckParticular().toString());
		cue64.setActivo("A");
		cuestionarioRepository.save(cue64);
		
		if(presentacion.getTratamiento().getParticular().getCheckParticular()) {
			int i=4;
			for(ListaParticularDTO lp: presentacion.getTratamiento().getParticular().getLista()) {
				if(lp.getValCheck()) {
					cue64 = new Cuestionario();
					cue64.setFolio(folio);
					cue64.setPregunta(22);
					cue64.setSubPregunta(i);
					cue64.setRespuesta(lp.getValText());
					cue64.setRutaArchivo(lp.getValTitulo()+"<->"+lp.getValHelp());
					cue64.setActivo("A");
					cuestionarioRepository.save(cue64);
					i++;
				}
			}
		}
		
		validaRutaGeneral(ruta1);
		validaRutaGeneral(ruta2);
		validaRutaGeneral(ruta3);
		validaRutaGeneral(ruta4);
		validaRutaGeneral(ruta5);
		validaRutaGeneral(ruta6);
		validaRutaGeneral(ruta7);
		validaRutaGeneral(ruta8);
		
		generaArchivo(ruta1+presentacion.getArchivo1().getFilename(), presentacion.getArchivo1());
		generaArchivo(ruta2+presentacion.getArchivo2().getFilename(), presentacion.getArchivo2());
		generaArchivo(ruta3+presentacion.getArchivo3().getFilename(), presentacion.getArchivo3());
		generaArchivo(ruta4+presentacion.getArchivo4().getFilename(), presentacion.getArchivo4());
		generaArchivo(ruta5+presentacion.getArchivo5().getFilename(), presentacion.getArchivo5());
		generaArchivo(ruta6+presentacion.getArchivo6().getFilename(), presentacion.getArchivo6());
		generaArchivo(ruta7+presentacion.getArchivo7().getFilename(), presentacion.getArchivo7());
		generaArchivo(ruta8+presentacion.getArchivo8().getFilename(), presentacion.getArchivo8());
		
		
		return cue56;
	}
	
	

	
	@Override
	public List<FlujoDTO> obtenerListaFlujo2(UsuarioDTO usuario) {
		List<Flujo> listCatalogo = new ArrayList<Flujo>();
		List<FlujoDTO> listaCatalogo = new ArrayList<FlujoDTO>();
		ResponseUsuario response = new ResponseUsuario();
		usuarios = null;
		try {
			//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getIdUsuario() == usuario.getIdUsuario() && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			usuarios = usuarioRepositorio.findByIdUsuarioAndActivo(usuario.getIdUsuario(), Constantes.ACTIVO);
			if(usuarios.getIdPerfil() == 1 || usuarios.getIdPerfil() == 3 ) {
				//listCatalogo = flujoRepositorio.findAll().stream().filter(f ->  f.getFlujo() == 2 && f.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
				listCatalogo = flujoRepositorio.findByFlujoAndActivo(2, Constantes.ACTIVO);
				listaCatalogo = listCatalogo.stream().map(FlujoDTO::new).collect(Collectors.toList());
				for(FlujoDTO fl: listaCatalogo) {
					fl.setUsuario(getNombreUsuario(fl.getFolio(),fl.getFlujo()));
					fl.setFecha(getFechaFormato(fl.getFecha()));
					fl.setIdColor(getIdColor(fl.getFolio()));
					fl.setTuvoRia(tuvoRia(fl.getFolio()));
					fl.setTieneRiesgos(tieneRiesgos(fl.getFolio()));
					fl.setTieneRecomendaciones(tieneRecomendaciones(fl.getFolio()));
					fl.setIdEtapa(getIdEtapa(fl.getEtapa()));
					fl.setTieneRiaNoPresentada(tieneRiaNoPresentada(fl.getFolio()));
					if(fl.getFlujo()==1) {
						fl.setTramite(Constantes.CONSULTA);
					}else if(fl.getFlujo()==2) {
						fl.setTramite(Constantes.PRESENTACION);						
					}else if(fl.getFlujo()==3) {
						fl.setTramite(Constantes.INFORME);						
					}
					
					if(fl.getPermiteAcuerdo() == null) {
						fl.setPermiteAcuerdo(0);
					}
					if(fl.getEtapa().equals(Constantes.RIA)) {
						fl.setIdColorRia(getIdcolorRia(fl.getFolio()));
					}
				}
			}else {
				//listCatalogo = flujoRepositorio.findAll().stream().filter(f -> f.getIdUsuario() == usuarios.getIdUsuario() && f.getFlujo() == 2 && f.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
				listCatalogo = flujoRepositorio.findByIdUsuarioAndFlujoAndActivo(usuarios.getIdUsuario(), 2, Constantes.ACTIVO);
				listaCatalogo = listCatalogo.stream().map(FlujoDTO::new).collect(Collectors.toList());
				for(FlujoDTO fl: listaCatalogo) {
					fl.setUsuario(getNombreUsuario(fl.getFolio(),fl.getFlujo()));
					fl.setFecha(getFechaFormato(fl.getFecha()));
					fl.setIdColor(getIdColor(fl.getFolio()));
					fl.setTuvoRia(tuvoRia(fl.getFolio()));
					fl.setTieneRiesgos(tieneRiesgos(fl.getFolio()));
					fl.setTieneRecomendaciones(tieneRecomendaciones(fl.getFolio()));
					fl.setIdEtapa(getIdEtapa(fl.getEtapa()));
					fl.setTieneRiaNoPresentada(tieneRiaNoPresentada(fl.getFolio()));
					if(fl.getFlujo()==1) {
						fl.setTramite(Constantes.CONSULTA);
					}else if(fl.getFlujo()==2) {
						fl.setTramite(Constantes.PRESENTACION);						
					}else if(fl.getFlujo()==3) {
						fl.setTramite(Constantes.INFORME);						
					}
					
					if(fl.getEtapa().equals(Constantes.RIA)) {
						fl.setIdColorRia(getIdcolorRia(fl.getFolio()));
					}
				}
			}
			
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setEstatus(Constantes.ERROR);
 			response.setMensaje(Constantes.ERROR);
		}
		
		return listaCatalogo.stream()
				.sorted(Comparator.comparing(FlujoDTO::getFolio).reversed())
				.collect(Collectors.toList());
		
	}
	
	private Integer getIdcolorRia(String folio) {
		Integer idColor = 0;
		SemaforoRia sem = semaforoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
		if(sem!=null) {
			idColor = sem.getIdColor();
		}
		
		return idColor;
	}
	
	private String getNombreUsuario(String folio, Integer idFlujo) {
		String nombre="Admin";
		try {
			Flujo f = flujoRepositorio.findByFolioAndFlujo(folio, idFlujo);
			if(f!=null) {
				Usuario u = usuarioRepositorio.findByIdUsuarioAndActivo(f.getIdUsuario(), Constantes.ACTIVO);
				nombre = u.getSujetoObligado();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nombre;
	}
	
	private Integer getIdEtapa(String etapa) {
		Integer id =0;
		
		//Catalogo cat = catalogoRepositorio.findAll().stream().filter(u -> u.getValor().equals(etapa) && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		
		try {
			Catalogo cat = catalogoRepositorio.findByValorAndActivo(etapa, Constantes.ACTIVO);
				if(cat!=null) {
					id = cat.getIdCatalogo();
				}
			}catch(Exception e){
				if(etapa.equals(Constantes.RIA)) {
					Catalogo cat = catalogoRepositorio.findByClaveAndActivo(Constantes.RIAD, Constantes.ACTIVO);
					if(cat!=null) {
						id = cat.getIdCatalogo();
					}
				}else if(etapa.equals(Constantes.RRIA)) {
					Catalogo cat = catalogoRepositorio.findByClaveAndActivo(Constantes.RRIAD, Constantes.ACTIVO);
					if(cat!=null) {
						id = cat.getIdCatalogo();
					}
				}else if(etapa.equals(Constantes.REVISION)) {
					Catalogo cat = catalogoRepositorio.findByClaveAndActivo(Constantes.RREVISION, Constantes.ACTIVO);
					if(cat!=null) {
						id = cat.getIdCatalogo();
					}
				}else if(etapa.equals(Constantes.GOT)) {
					Catalogo cat = catalogoRepositorio.findByClaveAndActivo(Constantes.KEY_ETAPAH2, Constantes.ACTIVO);
					if(cat!=null) {
						id = cat.getIdCatalogo();
					}
				}else if(etapa.equals(Constantes.ACUERDO)) {
					Catalogo cat = catalogoRepositorio.findByClaveAndActivo(Constantes.KEY_ETAPACUERDO, Constantes.ACTIVO);
					if(cat!=null) {
						id = cat.getIdCatalogo();
					}
				}
			}
		
		return id;
	}
	
	
	private boolean tuvoRia(String folio) {
		boolean tuvo = false;
		List<Cuestionario> cue = null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 20 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPregunta(folio, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(cue.size()>0) {
			tuvo = true;
		}
		
		return tuvo;
	}
	
	private boolean tieneRiesgos(String folio) {
		boolean tuvo = false;
		Cuestionario cue= null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 17 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPreguntaAndActivo(folio, 17, Constantes.ACTIVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(cue != null) {
			tuvo = true;
		}
		
		return tuvo;
	}
	
	private boolean tieneRecomendaciones(String folio) {
		boolean tuvo = false;
		Cuestionario cue= null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 19 && c.getSubPregunta() == 2 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPreguntaAndSubPreguntaAndActivo(folio, 19, 2, Constantes.ACTIVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(cue != null) {
			tuvo = true;
		}
		
		return tuvo;
	}
	
	private boolean tieneRiaNoPresentada(String folio) {
		boolean tuvo = false;
		Cuestionario cue= null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 21 && c.getSubPregunta() == 1 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPreguntaAndSubPreguntaAndActivo(folio, 24, 1, Constantes.ACTIVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(cue != null) {
			tuvo = true;
		}
		
		return tuvo;
	}
	
	public String getFechaFormato(String fecha) {
		String[] afecha = fecha.split("-");
		String sfecha= afecha[2]+"/"+ afecha[1] +"/"+afecha[0];
		return sfecha;
	}
	
	public Integer getIdColor(String folio) {
		Integer idColor = 0;
		//Periodo periodo = periodoRepository.findAll().stream().filter(p -> p.getFolio().equals(folio)  && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Periodo periodo = periodoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
		if(periodo != null ) {
			idColor = periodo.getIdColor();
		}else {
			idColor = 35;
		}
		return idColor;
	}
	
	
	@Async
	private void generaDocumento(String folio, FormPresentacionDTO presentacion) {
		folio = folio.replace("/", "");		
		
		 try (XWPFDocument doc = new XWPFDocument()) {
			 
			 XWPFParagraph p1 = doc.createParagraph();
			 XWPFRun r1 = p1.createRun();
			 r1.setBold(true);
	         r1.setText("Datos Generales");
	         r1.setColor("AE105A");
	         r1.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p2 = doc.createParagraph();
			 XWPFRun r2 = p2.createRun();
			 r2.setText(" a. Denominación.");
			 r2.setColor("AE105A");
			 
			 
			 XWPFParagraph p3 = doc.createParagraph();
			 XWPFRun r3 = p3.createRun();
			 r3.setText(presentacion.getDenominacion());
	         r3.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p21 = doc.createParagraph();
	         p21.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r21 = p21.createRun();
			 r21.setText(" b. El nombre de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
			 r21.setColor("AE105A");
			 r21.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p31 = doc.createParagraph();
			 XWPFRun r31 = p31.createRun();
			 r31.setText(presentacion.getNomPolPub());
	         r31.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p211 = doc.createParagraph();
	         p211.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r211 = p211.createRun();
			 r211.setText(" c. Los objetivos generales y específicos que persigue la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
			 r211.setColor("AE105A");
			 r211.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p212 = doc.createParagraph();
			 XWPFRun r212 = p212.createRun();
			 r212.setText(" Objetivos Generales.");
			 r212.setColor("AE105A");
			 r212.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p311 = doc.createParagraph();
			 XWPFRun r311 = p311.createRun();
			 r311.setText(presentacion.getObjGen());
	         r311.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p213 = doc.createParagraph();
			 XWPFRun r213 = p213.createRun();
			 r213.setText(" Objetivos Específicos.");
			 r213.setColor("AE105A");
			 r213.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p312 = doc.createParagraph();
			 XWPFRun r312 = p312.createRun();
			 r312.setText(presentacion.getObjEsp());
	         r312.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p313 = doc.createParagraph();
	         p313.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r313 = p313.createRun();
			 r313.setText(" d. El nombre y cargo del servidor o de los servidores públicos que cuentan con facultad expresa para decidir, aprobar o autorizar la puesta en operación o modificación de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
			 r313.setColor("AE105A");
			 r313.addBreak(BreakClear.ALL);
			 
			 
			 if( presentacion.getListaServidor() !=null && presentacion.getListaServidor().size()>0) {
				 	for(Servidor ser: presentacion.getListaServidor()) {
				 		 
				 		 XWPFParagraph p4 = doc.createParagraph();
						 XWPFRun r4 = p4.createRun();
						 r4.setText("Nombre: " + ser.getNombre());
						
						 XWPFParagraph p5 = doc.createParagraph();
						 XWPFRun r5 = p5.createRun();
						 r5.setText("Cargo: " + ser.getCargo());
				         r5.addBreak(BreakClear.ALL);
				         r5.addBreak(BreakClear.ALL);
				 	}
					
			 }
			
			 XWPFParagraph p100 = doc.createParagraph();
			 p100.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r100 = p100.createRun();	
			 r100.addBreak(BreakType.PAGE);
			 r100.setText("Tratamiento intensivo o relevante de datos personales.");
			 r100.setColor("AE105A");
			 r100.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p1011 = doc.createParagraph();
			 p1011.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r1011 = p1011.createRun();			
			 r1011.setText("Indicar si existe tratamiento o tratamientos intensivos o relevantes de datos personales de carácter general y/o particular.");
			 r1011.setColor("AE105A");
			 r1011.addBreak(BreakClear.ALL);
			 
			 
			 if(presentacion.getTratamiento().getGeneral().getCheckGeneral()) {
				 XWPFParagraph p102 = doc.createParagraph();
				 XWPFRun r102 = p102.createRun();
				 r102.setText("General: Seleccionado");
				 r102.setColor("AE105A");
				 r102.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p103 = doc.createParagraph();
				 XWPFRun r103 = p103.createRun();
				 r103.setText(presentacion.getTratamiento().getGeneral().getValGeneral());				 
				 r103.addBreak(BreakClear.ALL);
				 
			 }else {
				 XWPFParagraph p102 = doc.createParagraph();
				 XWPFRun r102 = p102.createRun();
				 r102.setText("General: No seleccionado");
				 r102.setColor("AE105A");
				 r102.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p103 = doc.createParagraph();
				 XWPFRun r103 = p103.createRun();
				 r103.setText("Sin información");				 
				 r103.addBreak(BreakClear.ALL);				 
			 }
			 
			 if(presentacion.getTratamiento().getParticular().getCheckParticular()) {
				 XWPFParagraph p104 = doc.createParagraph();
				 XWPFRun r104 = p104.createRun();
				 r104.setText("Particular: Seleccionado");
				 r104.setColor("AE105A");
				 r104.addBreak(BreakClear.ALL);
				 				 				
				 for(ListaParticularDTO lista: presentacion.getTratamiento().getParticular().getLista()) {
					 if(lista.getValCheck()) {
						 XWPFParagraph p105 = doc.createParagraph();
						 XWPFRun r105 = p105.createRun();
						 r105.setText(lista.getValTitulo());
						 r105.setColor("AE105A");
						 r105.addBreak(BreakClear.ALL);
						 
						 XWPFParagraph p106 = doc.createParagraph();
						 XWPFRun r106 = p106.createRun();
						 r106.setText(lista.getValText());				 
						 r106.addBreak(BreakClear.ALL);
					 }
					 
				 }
				 
			 }else {
				 XWPFParagraph p104 = doc.createParagraph();
				 XWPFRun r104 = p104.createRun();
				 r104.setText("Particular: No seleccionado");
				 r104.setColor("AE105A");
				 r104.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p105 = doc.createParagraph();
				 XWPFRun r105 = p105.createRun();
				 r105.setText("Sin información");				 
				 r105.addBreak(BreakClear.ALL);
			 }
			 
			 
			 XWPFParagraph p6 = doc.createParagraph();
			 p6.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r6 = p6.createRun();
			 r6.addBreak(BreakType.PAGE);
			 r6.setText("1. Descripción de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales que pretenda poner en operación o modificar.");
			 r6.setColor("AE105A");
			 r6.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p61 = doc.createParagraph();
			 XWPFRun r61 = p61.createRun();
			 r61.setText("Objetivos generales.");
			 r61.setColor("AE105A");
			
			 
			 XWPFParagraph p7 = doc.createParagraph();
			 XWPFRun r7 = p7.createRun();
			 r7.setText(presentacion.getObjetivosGenerales());
			 r7.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p62 = doc.createParagraph();
			 XWPFRun r62 = p62.createRun();
			 r62.setText("Objetivos específicos.");
			 r62.setColor("AE105A");
			 
			 
			 XWPFParagraph p71 = doc.createParagraph();
			 XWPFRun r71 = p71.createRun();
			 r71.setText(presentacion.getObjetivosEspecificos());
			 r71.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p63 = doc.createParagraph();
			 XWPFRun r63 = p63.createRun();
			 r63.setText("Fundamento legal.");
			 r63.setColor("AE105A");
			 
			 
			 XWPFParagraph p72 = doc.createParagraph();
			 XWPFRun r72 = p72.createRun();
			 r72.setText(presentacion.getObjetivosLegal());
			 r72.addBreak(BreakClear.ALL);
			 
			 if(presentacion.getHipervinculo() !=null && !presentacion.getHipervinculo().equals("")) {
				 XWPFParagraph p64 = doc.createParagraph();
				 XWPFRun r64 = p64.createRun();
				 r64.setText("Enlace al hipervínculo web.");
				 r64.setColor("AE105A");
				
				 
				 XWPFParagraph p65 = doc.createParagraph();
				 XWPFRun r65 = p65.createRun();
				 r65.setText(presentacion.getHipervinculo());
				 r65.addBreak(BreakClear.ALL);
			 }
			 
			 XWPFParagraph p8 = doc.createParagraph();
			 XWPFRun r8 = p8.createRun();			
			 r8.setText("Categorías de titulares de datos.");
			 r8.setColor("AE105A");
			
			 
			 XWPFParagraph p81 = doc.createParagraph();
			 XWPFRun r81 = p81.createRun();
			 r81.setText(presentacion.getCategorias());
			 r81.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p9 = doc.createParagraph();
			 XWPFRun r9 = p9.createRun();			
			 r9.setText("Grupos vulnerables.");
			 r9.setColor("AE105A");
			 
			 
			 XWPFParagraph p91 = doc.createParagraph();
			 XWPFRun r91 = p91.createRun();
			 r91.setText(presentacion.getGrupVul());
			 r91.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p10 = doc.createParagraph();
			 XWPFRun r10 = p10.createRun();			
			 r10.setText("Datos personales objeto de tratamiento.");
			 r10.setColor("AE105A");
			 
			 
			 XWPFParagraph p101 = doc.createParagraph();
			 XWPFRun r101 = p101.createRun();
			 r101.setText(presentacion.getDatPerTra());
			 r101.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p11 = doc.createParagraph();
			 XWPFRun r11 = p11.createRun();			
			 r11.setText("Datos personales sensibles.");
			 r11.setColor("AE105A");
			 
			 
			 XWPFParagraph p111 = doc.createParagraph();
			 XWPFRun r111 = p111.createRun();
			 r111.setText(presentacion.getDatPerSen());
			 r111.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p12 = doc.createParagraph();
			 XWPFRun r12 = p12.createRun();			
			 r12.setText("Finalidades del tratamiento.");
			 r12.setColor("AE105A");
			
			 
			 XWPFParagraph p121 = doc.createParagraph();
			 XWPFRun r121 = p121.createRun();
			 r121.setText(presentacion.getFinTra());
			 r121.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p13 = doc.createParagraph();
			 XWPFRun r13 = p13.createRun();			
			 r13.setText("Número de procesos que involucran tratamiento.");
			 r13.setColor("AE105A");
			
			 
			 XWPFParagraph p131 = doc.createParagraph();
			 XWPFRun r131 = p131.createRun();
			 r131.setText(presentacion.getNuProTra());
			 r131.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p14 = doc.createParagraph();
			 XWPFRun r14 = p14.createRun();			
			 r14.setText("Vienen descritos los procesos.");
			 r14.setColor("AE105A");
			 
			 
			 XWPFParagraph p141 = doc.createParagraph();
			 XWPFRun r141 = p141.createRun();
			 r141.setText(presentacion.getViDePro());
			 r141.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p15 = doc.createParagraph();
			 XWPFRun r15 = p15.createRun();			
			 r15.setText("Forma en que se recaban datos personales.");
			 r15.setColor("AE105A");
			 r15.addBreak(BreakClear.ALL);
			 
			 if(!presentacion.getFormaRecaban().getPersonal().equals("")) {
					XWPFParagraph p151 = doc.createParagraph();
					XWPFRun r151 = p151.createRun();
					r151.setText(presentacion.getFormaRecaban().getPersonal());
					r151.addBreak(BreakClear.ALL);
			 }
			 
			 if(!presentacion.getFormaRecaban().getDirecta().equals("")) {
					XWPFParagraph p152 = doc.createParagraph();
					XWPFRun r152 = p152.createRun();
					r152.setText(presentacion.getFormaRecaban().getDirecta());
					r152.addBreak(BreakClear.ALL);
			 }
			 
			 if(!presentacion.getFormaRecaban().getIndirecta().equals("")) {
					XWPFParagraph p153 = doc.createParagraph();
					XWPFRun r153 = p153.createRun();
					r153.setText(presentacion.getFormaRecaban().getIndirecta());
					r153.addBreak(BreakClear.ALL);
			 }
			 
			 XWPFParagraph p16 = doc.createParagraph();
			 XWPFRun r16 = p16.createRun();			
			 r16.setText("Fuentes de las cuales provienen los datos.");
			 r16.setColor("AE105A");
			
			 
			 XWPFParagraph p161 = doc.createParagraph();
			 XWPFRun r161 = p161.createRun();
			 r161.setText(presentacion.getFuProDat());
			 r161.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p17 = doc.createParagraph();
			 XWPFRun r17 = p17.createRun();			
			 r17.setText("Transferencias de datos personales.");
			 r17.setColor("AE105A");
			
			 
			 XWPFParagraph p171 = doc.createParagraph();
			 XWPFRun r171 = p171.createRun();
			 r171.setText(presentacion.getTraDatPer());
			 r171.addBreak(BreakClear.ALL);
			 
			 
			 if(presentacion.getTraDatPer() != null && presentacion.getTraDatPer().equals("Si")) {
					
				 XWPFParagraph p172 = doc.createParagraph();
				 XWPFRun r172 = p172.createRun();			
				 r172.setText("Tipo de Transferencia.");
				 r172.setColor("AE105A");
				 
				 
				 XWPFParagraph p173 = doc.createParagraph();
				 XWPFRun r173 = p173.createRun();
				 r173.setText(presentacion.getTipTraDatPer());
				 r173.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p174 = doc.createParagraph();
				 XWPFRun r174 = p174.createRun();			
				 r174.setText("Sector de Transferencia.");
				 r174.setColor("AE105A");
				 
				 
				 XWPFParagraph p175 = doc.createParagraph();
				 XWPFRun r175 = p175.createRun();
				 r175.setText(presentacion.getSecTraDatPer());
				 r175.addBreak(BreakClear.ALL);
					
				}
			 
			 XWPFParagraph p18 = doc.createParagraph();
			 XWPFRun r18 = p18.createRun();			
			 r18.setText("Tiempo de duración del tratamiento.");
			 r18.setColor("AE105A");
			 
			 
			 XWPFParagraph p181 = doc.createParagraph();
			 XWPFRun r181 = p181.createRun();
			 r181.setText(presentacion.getTieDurTra());
			 r181.addBreak(BreakClear.ALL);
			 
			 
			 XWPFParagraph p19 = doc.createParagraph();
			 p19.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r19 = p19.createRun();			
			 r19.setText("La tecnología que se pretende utilizar para efectuar el tratamiento intensivo o relevante de datos personales.");
			 r19.setColor("AE105A");
			 
			 
			 XWPFParagraph p191 = doc.createParagraph();
			 XWPFRun r191 = p191.createRun();
			 r191.setText(presentacion.getIntRel());
			 r191.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p20 = doc.createParagraph();
			 XWPFRun r20 = p20.createRun();			
			 r20.setText("Medidas de seguridad.");
			 r20.setColor("AE105A");
			 
			 
			 XWPFParagraph p201 = doc.createParagraph();
			 XWPFRun r201 = p201.createRun();
			 r201.setText(presentacion.getMedSeg());
			 r201.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p22 = doc.createParagraph();
			 XWPFRun r22 = p22.createRun();			
			 r22.setText("Información adicional.");
			 r22.setColor("AE105A");
			 
			 
			 XWPFParagraph p221 = doc.createParagraph();
			 XWPFRun r221 = p221.createRun();
			 r221.setText(presentacion.getInfoAdi());
			 r221.addBreak(BreakClear.ALL);
			
			 
			 XWPFParagraph p23 = doc.createParagraph();
			 p23.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r23 = p23.createRun();
			 r23.addBreak(BreakType.PAGE);
			 r23.setText("2. Justificación de la necesidad de implementar o modificar la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
			 r23.setColor("AE105A");
			 r23.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p231 = doc.createParagraph();
			 p231.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r231 = p231.createRun();
			 r231.setText("Las medidas propuestas son susceptibles o idóneas para garantizar el derecho a la protección de datos personales de los titulares.");
			 r231.setColor("AE105A");
			 
				
			 XWPFParagraph p232 = doc.createParagraph();
			 XWPFRun r232 = p232.createRun();
			 r232.setText(presentacion.getSusIdo());
			 r232.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p2321 = doc.createParagraph();
			 p2321.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r2321 = p2321.createRun();
			 r2321.setText("Si la o las medidas propuestas son las estrictamente necesarias, en el sentido de ser las más moderadas para garantizar el derecho a la protección de datos personales de los titulares.");
			 r2321.setColor("AE105A");
			 
				
			 XWPFParagraph p233 = doc.createParagraph();
			 XWPFRun r233 = p233.createRun();
			 r233.setText(presentacion.getProEst());
			 r233.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p234 = doc.createParagraph();
			 p234.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r234 = p234.createRun();
			 r234.setText("Si la o las medidas son equilibradas en función del mayor número de beneficios o ventajas que perjuicios para el garantizar el derecho a la protección de datos personales de los titulares.");
			 r234.setColor("AE105A");
			 
				
			 XWPFParagraph p235 = doc.createParagraph();
			 XWPFRun r235 = p235.createRun();
			 r235.setText(presentacion.getEquFun());
			 r235.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p24 = doc.createParagraph();
			 p24.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r24 = p24.createRun();
			 r24.addBreak(BreakType.PAGE);
			 r24.setText("3. La representación del ciclo de vida de los datos personales a tratar (obtención, aprovechamiento, explotación, almacenamiento, conservación o cualquier otra operación realizada, hasta la supresión).");
			 r24.setColor("AE105A");
			 r24.addBreak(BreakClear.ALL);
				
			
			 
			 XWPFParagraph p25 = doc.createParagraph();
			 XWPFRun r25 = p25.createRun();
			 r25.setText("Tipo de tratamiento.");
			 r25.setColor("AE105A");
			 r25.addBreak(BreakClear.ALL);
			 
			 if(!presentacion.getCheckTipoTratamiento().getObtencion().equals("")) {
				 XWPFParagraph p251 = doc.createParagraph();
				 XWPFRun r251 = p251.createRun();
				 r251.setText(presentacion.getCheckTipoTratamiento().getObtencion());
				 r251.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getAprovechamiento().equals("")) {
				 XWPFParagraph p252 = doc.createParagraph();
				 XWPFRun r252 = p252.createRun();
				 r252.setText(presentacion.getCheckTipoTratamiento().getAprovechamiento());
				 r252.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getExplotacion().equals("")) {
				 XWPFParagraph p253 = doc.createParagraph();
				 XWPFRun r253 = p253.createRun();
				 r253.setText(presentacion.getCheckTipoTratamiento().getExplotacion());
				 r253.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getAlmacenamiento().equals("")) {
				 XWPFParagraph p254 = doc.createParagraph();
				 XWPFRun r254 = p254.createRun();
				 r254.setText(presentacion.getCheckTipoTratamiento().getAlmacenamiento());
				 r254.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getConservacion().equals("")) {
				 XWPFParagraph p255 = doc.createParagraph();
				 XWPFRun r255 = p255.createRun();
				 r255.setText(presentacion.getCheckTipoTratamiento().getConservacion());
				 r255.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getSupresion().equals("")) {
				 XWPFParagraph p256 = doc.createParagraph();
				 XWPFRun r256 = p256.createRun();
				 r256.setText(presentacion.getCheckTipoTratamiento().getSupresion());
				 r256.addBreak(BreakClear.ALL);	
			 }
			 
			 if(!presentacion.getCheckTipoTratamiento().getOtra().equals("")) {
				 XWPFParagraph p257 = doc.createParagraph();
				 XWPFRun r257 = p257.createRun();
				 r257.setText(presentacion.getCheckTipoTratamiento().getOtra());
				 r257.addBreak(BreakClear.ALL);	
			 }
			 
			 
			 if(presentacion.getCheckTipoTratamiento().getOtra() !=null && presentacion.getCheckTipoTratamiento().getOtra().equals("Otra")) {
				 XWPFParagraph p258 = doc.createParagraph();
				 XWPFRun r258 = p258.createRun();
				 r258.setText("Otro tipo de tratamiento.");
				 r258.setColor("AE105A");
				 r258.addBreak(BreakClear.ALL);
					
				 XWPFParagraph p259 = doc.createParagraph();
				 XWPFRun r259 = p259.createRun();
				 r259.setText(presentacion.getOtroTratamiento());
				 r259.addBreak(BreakClear.ALL);
			 }
				

			 XWPFParagraph p26 = doc.createParagraph();
			 p26.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r26 = p26.createRun();
			 r26.setText("Las fuentes internas y/o externas, así como los medios y procedimientos a través de los cuales se recabarán los datos personales, o bien, son recabados.");
			 r26.setColor("AE105A");
			 r26.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p261 = doc.createParagraph();
			 XWPFRun r261 = p261.createRun();
			 r261.setText(presentacion.getFueInt());
			 r261.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p27 = doc.createParagraph();
			 p27.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r27 = p27.createRun();
			 r27.setText("Las áreas, grupos o personas que llevarán a cabo operaciones específicas de tratamiento con los datos personales.");
			 r27.setColor("AE105A");
			 r27.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p271 = doc.createParagraph();
			 XWPFRun r271 = p271.createRun();
			 r271.setText(presentacion.getAreGruPer());
			 r271.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p272 = doc.createParagraph();
			 XWPFRun r272 = p272.createRun();
			 r272.setText("Se realiza tratamiento por parte de encargados.");
			 r272.setColor("AE105A");
			 r272.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p273 = doc.createParagraph();
			 XWPFRun r273 = p273.createRun();
			 r273.setText(presentacion.getTraEnc());
			 r273.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p28 = doc.createParagraph();
			 XWPFRun r28 = p28.createRun();
			 r28.setText("Encargados que brindan servicio de cómputo en nube.");
			 r28.setColor("AE105A");
			 r28.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p281 = doc.createParagraph();
			 XWPFRun r281 = p281.createRun();
			 r281.setText(presentacion.getSerNub());
			 r281.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p29 = doc.createParagraph();
			 XWPFRun r29 = p29.createRun();
			 r29.setText("En el tratamiento de datos personales participan otros responsables.");
			 r29.setColor("AE105A");
			 r29.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p291 = doc.createParagraph();
			 XWPFRun r291 = p291.createRun();
			 r291.setText(presentacion.getTraResp());
			 r291.addBreak(BreakClear.ALL);
				
			 if(presentacion.getTraResp().equals("Si")) {
					
				 XWPFParagraph p30 = doc.createParagraph();
				 XWPFRun r30 = p30.createRun();
				 r30.setText("Sector.");
				 r30.setColor("AE105A");
				 r30.addBreak(BreakClear.ALL);
					
				 XWPFParagraph p301 = doc.createParagraph();
				 XWPFRun r301 = p301.createRun();
				 r301.setText(presentacion.getTraRespSec());
				 r301.addBreak(BreakClear.ALL);
					
			 }
			 
			 XWPFParagraph p32 = doc.createParagraph();
			 XWPFRun r32 = p32.createRun();
			 r32.setText("Los plazos de conservación o almacenamiento de los datos personales.");
			 r32.setColor("AE105A");
			 r32.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p321 = doc.createParagraph();
			 XWPFRun r321 = p321.createRun();
			 r321.setText(presentacion.getPlaConAlm());
			 r321.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p33 = doc.createParagraph();
			 XWPFRun r33 = p33.createRun();
			 r33.setText("Las técnicas a utilizar para garantizar el borrado seguro de los datos personales.");
			 r33.setColor("AE105A");
			 r33.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p331 = doc.createParagraph();
			 XWPFRun r331 = p331.createRun();
			 r331.setText(presentacion.getTecUti());
			 r331.addBreak(BreakClear.ALL);
				
				
			 XWPFParagraph p34 = doc.createParagraph();
			 p34.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r34 = p34.createRun();
			 r34.addBreak(BreakType.PAGE);
			 r34.setText("4. La identificación, análisis y descripción de la gestión de los riesgos inherentes para la protección de los datos personales.");
			 r34.setColor("AE105A");
			 r34.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p341 = doc.createParagraph();
			 p341.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r341 = p341.createRun();
			 r341.setText("Se identifican y describen específicamente los riesgos administrativos, físicos o tecnológicos que podrían presentarse con la puesta en operación o modificación de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
			 r341.setColor("AE105A");
			 r341.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p342 = doc.createParagraph();
			 XWPFRun r342 = p342.createRun();
			 r342.setText(presentacion.getIdeDes());
			 r342.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p35 = doc.createParagraph();
			 p35.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r35 = p35.createRun();
			 r35.setText("Se llevó a cabo la ponderación cuantitativa y/o cualitativa de la probabilidad de que los riesgos identificados sucedan, así como su nivel de impacto en los titulares en lo que respecta al tratamiento de sus datos personales.");
			 r35.setColor("AE105A");
			 r35.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p351 = doc.createParagraph();
			 XWPFRun r351 = p351.createRun();
			 r351.setText(presentacion.getPonCuan());
			 r351.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p36 = doc.createParagraph();
			 p36.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r36 = p36.createRun();
			 r36.setText("Señala las medidas y controles concretos que el responsable adoptará para eliminar, mitigar, transferir o retener los riesgos detectados, de tal manera que no tengan un impacto en la esfera de los titulares, en lo que respecta al tratamiento de sus datos personales.");
			 r36.setColor("AE105A");
			 r36.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p361 = doc.createParagraph();
			 XWPFRun r361 = p361.createRun();
			 r361.setText(presentacion.getMedCon());
			 r361.addBreak(BreakClear.ALL);
				
			 XWPFParagraph p37 = doc.createParagraph();
			 p37.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r37 = p37.createRun();
			 r37.addBreak(BreakType.PAGE);
			 r37.setText("5. Análisis de cumplimiento normativo en materia de protección de datos personales de conformidad con la Ley General o las legislaciones estatales en la materia y demás disposiciones aplicables.");
			 r37.setColor("AE105A");
			 r37.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p371 = doc.createParagraph();
			 XWPFRun r371 = p371.createRun();
			 r371.setText("Análisis de cumplimiento de principios.");
			 r371.setColor("AE105A");
			 r371.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p372 = doc.createParagraph();
			 XWPFRun r372 = p372.createRun();
			 r372.setText(presentacion.getCumPri());
			 r372.addBreak(BreakClear.ALL);

			 XWPFParagraph p38 = doc.createParagraph();
			 XWPFRun r38 = p38.createRun();
			 r38.setText("Análisis de cumplimiento de deberes.");
			 r38.setColor("AE105A");
			 r38.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p382 = doc.createParagraph();
			 XWPFRun r382 = p382.createRun();
			 r382.setText(presentacion.getCumDeb());
			 r382.addBreak(BreakClear.ALL);

			 XWPFParagraph p39 = doc.createParagraph();
			 XWPFRun r39 = p39.createRun();
			 r39.setText("Análisis de cumplimiento de obligaciones específicas.");
			 r39.setColor("AE105A");
			 r39.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p392 = doc.createParagraph();
			 XWPFRun r392 = p392.createRun();
			 r392.setText(presentacion.getCumEsp());
			 r392.addBreak(BreakClear.ALL);

			 XWPFParagraph p40 = doc.createParagraph();
			 XWPFRun r40 = p40.createRun();
			 r40.setText("¿El tratamiento tiene vinculación con ejercicio de derechos ARCO?");
			 r40.setColor("AE105A");
			 r40.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p401 = doc.createParagraph();
			 XWPFRun r401 = p401.createRun();
			 r401.setText(presentacion.getVinEje());
			 r401.addBreak(BreakClear.ALL);

			 
			 XWPFParagraph p41 = doc.createParagraph();
			 XWPFRun r41 = p41.createRun();
			 r41.addBreak(BreakType.PAGE);
			 r41.setText("6. Los resultados de la o las consultas externas que, en su caso, se efectúen.");
			 r41.setColor("AE105A");
			 r41.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p411 = doc.createParagraph();
			 p411.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r411 = p411.createRun();
			 r411.setText("Los resultados de la consulta externa influyeron en la modificación del tratamiento relevante o intensivo.");
			 r411.setColor("AE105A");
			 r411.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p412 = doc.createParagraph();
			 XWPFRun r412 = p412.createRun();
			 r412.setText(presentacion.getConExt());
			 r412.addBreak(BreakClear.ALL);
			 
			 
			 XWPFParagraph p42 = doc.createParagraph();
			 p42.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r42 = p42.createRun();
			 r42.addBreak(BreakType.PAGE);
			 r42.setText("7. La opinión técnica del oficial de protección de datos personales respecto del tratamiento intensivo o relevante de datos personales que implique la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología.");
			 r42.setColor("AE105A");
			 r42.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p421 = doc.createParagraph();
			 XWPFRun r421 = p421.createRun();
			 r421.setText("¿Quién realiza la opinión?");
			 r421.setColor("AE105A");
			 r421.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p422 = doc.createParagraph();
			 XWPFRun r422 = p422.createRun();
			 r422.setText(presentacion.getReaOpi());
			 r422.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p43 = doc.createParagraph();
			 p43.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r43 = p43.createRun();
			 r43.addBreak(BreakType.PAGE);
			 r43.setText("8. Cualquier otra información o documentos que considere conveniente hacer del conocimiento del Instituto o los organismos garantes en función de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales y que pretenda poner en operación o modificar.");
			 r43.setColor("AE105A");
			 r43.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p431 = doc.createParagraph();
			 XWPFRun r431 = p431.createRun();
			 r431.setText("Descripción general de la información adicional:");
			 r431.setColor("AE105A");
			 r431.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p432 = doc.createParagraph();
			 XWPFRun r432 = p432.createRun();
			 r432.setText(presentacion.getDescGen());
			 r432.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p44 = doc.createParagraph();
			 XWPFRun r44 = p44.createRun();
			 r44.setText("Descripción general de los documentos anexos:");
			 r44.setColor("AE105A");
			 r44.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p441 = doc.createParagraph();
			 XWPFRun r441 = p441.createRun();
			 r441.setText(presentacion.getDescAne());
			 r441.addBreak(BreakClear.ALL);
			 
			 
			 XWPFParagraph p45 = doc.createParagraph();
			 p45.setAlignment(ParagraphAlignment.BOTH);
			 XWPFRun r45 = p45.createRun();
			 r45.addBreak(BreakType.PAGE);
			 r45.setText("9. Se trata de una evaluación de impacto en la protección de datos personales interinstitucional.");
			 r45.setColor("AE105A");
			 r45.addBreak(BreakClear.ALL);
			 
			 if(presentacion.getRadioEvaluacionImpacto() ==null || presentacion.getRadioEvaluacionImpacto().equals("") || presentacion.getRadioEvaluacionImpacto().equals("No aplica")) {
				 XWPFParagraph p46 = doc.createParagraph();
				 XWPFRun r46 = p46.createRun();
				 r46.setText("No aplica");
				 r46.addBreak(BreakClear.ALL);
			 }else {
				 XWPFParagraph p47 = doc.createParagraph();
				 XWPFRun r47 = p47.createRun();
				 r47.setText(presentacion.getRadioEvaluacionImpacto());
				 r47.addBreak(BreakClear.ALL);
			 }
			 
			 if(presentacion.getRadioEvaluacionImpacto() !=null && !presentacion.getRadioEvaluacionImpacto().equals("") && !presentacion.getRadioEvaluacionImpacto().equals("No aplica") ) {
				 XWPFParagraph p48 = doc.createParagraph();
				 XWPFRun r48 = p48.createRun();
				 r48.setText("La denominación de los responsables conjuntos que presentan la evaluación de impacto en la protección de datos personales.");
				 r48.setColor("AE105A");
				 r48.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p482 = doc.createParagraph();
				 p482.setAlignment(ParagraphAlignment.BOTH);
				 XWPFRun r482 = p482.createRun();
				 r482.setText(presentacion.getEvaUno());
				 r482.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p49 = doc.createParagraph();
				 XWPFRun r49 = p49.createRun();
				 r49.setText("La denominación del responsable líder del proyecto, entendido para efecto de las presentes Disposiciones administrativas como el responsable que tiene a su cargo coordinar las acciones necesarias entre los distintos responsables para la elaboración de la evaluación de impacto en la protección de datos personales.");
				 r49.setColor("AE105A");
				 r49.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p492 = doc.createParagraph();
				 p492.setAlignment(ParagraphAlignment.BOTH);
				 XWPFRun r492 = p492.createRun();
				 r492.setText(presentacion.getEvaDos());
				 r492.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p50 = doc.createParagraph();
				 XWPFRun r50 = p50.createRun();
				 r50.setText("Las obligaciones, deberes, responsabilidades, límites y demás cuestiones relacionadas con la participación de todos los responsables.");
				 r50.setColor("AE105A");
				 r50.addBreak(BreakClear.ALL);
				 
				 XWPFParagraph p452 = doc.createParagraph();
				 p452.setAlignment(ParagraphAlignment.BOTH);
				 XWPFRun r452 = p452.createRun();
				 r452.setText(presentacion.getEvaTres());
				 r452.addBreak(BreakClear.ALL);
			 }
			 
			 try (FileOutputStream out = new FileOutputStream(dirflujo2+folio+".docx")) {
	                doc.write(out);
	         }
		 }catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public String getdirflujo2() {
		return dirflujo2;
	}
	

	public void setdirflujo2(String dirflujo2) {
		this.dirflujo2 = dirflujo2;
	}
	
	
	
	
	public String getDirflujo3() {
		return dirflujo3;
	}

	public void setDirflujo3(String dirflujo3) {
		this.dirflujo3 = dirflujo3;
	}

	private void enviarCorreoAltaFlujo2(FormPresentacionDTO presentacion, String folio){
	
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		//Usuario usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getIdUsuario() == presentacion.getIdUsuario() && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Usuario usuarios = usuarioRepositorio.findByIdUsuarioAndActivo(presentacion.getIdUsuario(), Constantes.ACTIVO);
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("de")) {
				de = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("nombreDe")) {
				nombreDe = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("destinatarios")) {
				destinatarios = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("asunto")) {
				asunto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("textoflujo2")) {
				body = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("servidor")) {
				servidor = listCatalogo.get(i).getValor();
				continue;
			}
		}
		
		body = body.replace("<user>", usuarios.getUsuario());
		body = body.replace("<folio>", folio);
		
		servidor = servidor + "eipdp";
		body = body.replace("<servidor>", servidor);
		asunto = Constantes.ALTA_FLUJO2;
		
		iCorreoService.enviaCorreoNotificacionAltaUsuario();
		iCorreoService.configurarCorreo();
		iCorreoService.enviarCorreo(de, nombreDe, destinatarios, asunto, body);
	}

	
	@Async
    private void actualizaPeriodoValido(String folio) {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		try {
			
			while(i<5) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				//dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfechaFinal.toString()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			Periodo pe = new Periodo();
			pe.setFolio(folio);
			pe.setFechaInicioPeriodo(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			pe.setFechaFinPeriodo(sfechaFinal);
			pe.setDiasPeriodo(5);
			pe.setIdColor(32);
			pe.setActivo("A");
			
			periodoRepository.save(pe);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo2(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
			cuestionario = cuestionarioRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
			if(cuestionario !=null) {
				cue = cuestionario.stream().map(CuestionarioDTO::new).collect(Collectors.toList());
			}
			
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setEstatus(Constantes.ERROR);
 			response.setMensaje(Constantes.ERROR);
		}
		
		return cue;
	}
	
	@Override
	public ResponseUsuario aceptarfdos(AceptarFUnoDTO aceptar) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaAceptar();
        	 actualizaFlujo2(aceptar.getFolio(), etapa);
        	 generaFolioDirectorio(aceptar.getFolio());
        	 altaCuestionarioAceptar(aceptar);
        	 actualizaPeriodoFlujo2(aceptar); 
        	 generaDocumentoAceptar(aceptar,"9");
        	 enviarCorreoAceptarFlujo2(aceptar);
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 9 + Constantes.SEPARADORD + aceptar.getUpload().getFilename();
        	 altaSecuenciaFlujo(aceptar.getFolio(), Constantes.ACEPTADA,ruta);
        	 
        	 altaSecuenciaFlujo(aceptar.getFolio(), Constantes.ACUERDO, "");
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerEtapaAceptar() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPACUERDO);
		
		return etapa;
	}
	
	private void actualizaFlujo2(String folio, String etapa)throws Exception {
		//Flujo flujo = flujoRepositorio.findAll().stream().filter(f -> f.getFolio().equals(folio) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Flujo flujo = flujoRepositorio.findByFolio(folio);
		flujo.setEtapa(etapa);
		flujoRepositorio.save(flujo);
	}
	
	private void altaCuestionarioAceptar(AceptarFUnoDTO a)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		cue.setPregunta(15);
		cue.setSubPregunta(1);
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "9" + Constantes.SEPARADORD + a.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	@Async
	private void actualizaPeriodoFlujo2(AceptarFUnoDTO a)throws Exception {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		
			
			while(i<30) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				//dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfechaFinal.toString()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			//Periodo pe = periodoRepository.findAll().stream().filter(f -> f.getFolio().equals(a.getFolio()) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			Periodo pe = periodoRepository.findByFolioAndActivo(a.getFolio(), Constantes.ACTIVO);
			pe.setFechaInicioPeriodo(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			pe.setFechaFinPeriodo(sfechaFinal);
			pe.setDiasPeriodo(30);
			pe.setIdColor(32);
			pe.setActivo("A");
			
			periodoRepository.save(pe);
	}
	
	private void generaDocumentoAceptar(AceptarFUnoDTO a, String pregunta){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + pregunta + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + pregunta + Constantes.SEPARADORD + a.getUpload().getFilename();
		generaArchivo(ruta, a.getUpload());
	}
	
	private void enviarCorreoAceptarFlujo2(AceptarFUnoDTO a){
		
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		//Flujo folio = flujoRepositorio.findAll().stream().filter(f -> f.getFolio().equals(a.getFolio()) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Flujo folio = flujoRepositorio.findByFolio(a.getFolio());
		//Usuario usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getIdUsuario() == folio.getIdUsuario() && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Usuario usuarios = usuarioRepositorio.findByIdUsuarioAndActivo(folio.getIdUsuario(), Constantes.ACTIVO);
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("de")) {
				de = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("nombreDe")) {
				nombreDe = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("destinatarios")) {
				destinatarios = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("asunto")) {
				asunto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("textoaceptarflujo2")) {
				body = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("servidor")) {
				servidor = listCatalogo.get(i).getValor();
				continue;
			}
		}
		
		body = body.replace("<user>", usuarios.getUsuario());
		body = body.replace("<folio>", a.getFolio());
		
		servidor = servidor + "eipdp";
		body = body.replace("<servidor>", servidor);
		asunto = Constantes.ACEPTAR_FLUJO1;
		destinatarios = usuarios.getEmailInstitucional();
		
		iCorreoService.enviaCorreoNotificacionAltaUsuario();
		iCorreoService.configurarCorreo();
		iCorreoService.enviarCorreo(de, nombreDe, destinatarios, asunto, body);
	}

	@Override
	public ResponseUsuario guardarReunion(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		
		 try {
        	 
			 altaReunion(documento);
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
			
		return response;
	}
	
	private void altaReunion(SubeDocumento sb)throws Exception {
		int subPre = 0;
		//List<Cuestionario> cu = cuestionarioRepository.findAll().stream().filter(c -> c.getPregunta() == 16 && c.getFolio().equals(sb.getFolio()) && c.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
		List<Cuestionario> cu = cuestionarioRepository.findByFolioAndPregunta(sb.getFolio(), 16);
		if(cu.size() == 0) {
			subPre = 1;
		}else {
			subPre = cu.size() + 1;
		}
		
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(16);
		cued.setSubPregunta(subPre);
		cued.setRespuesta(sb.getAsunto());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}

	@Override
	public List<CuestionarioDTO> obtenerListaReunion(SubeDocumento sb) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		//List<Cuestionario> cu = cuestionarioRepository.findAll().stream().filter(c -> c.getPregunta() == 16 && c.getFolio().equals(sb.getFolio()) && c.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
		List<Cuestionario> cu = cuestionarioRepository.findByFolioAndPregunta(sb.getFolio(), 16);
		if(cu !=null) {
			cue = cu.stream().map(CuestionarioDTO::new).collect(Collectors.toList());
		}
		
		return cue.stream()
				.sorted(Comparator.comparing(CuestionarioDTO::getSubpregunta))
				.collect(Collectors.toList());

	}
	
	@Override
	public ResponseUsuario actualizarMinuta(AceptarFUnoDTO aceptar) {
		ResponseUsuario response = new ResponseUsuario();
		Integer subpregunta = Integer.parseInt(aceptar.getEmail());
		
		 try {
			 generaFolioDirectorio(aceptar.getFolio());
			 //Cuestionario cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(aceptar.getFolio()) && c.getPregunta() == 16 && c.getSubPregunta() == subpregunta && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			 Cuestionario cue = cuestionarioRepository.findByFolioAndPreguntaAndSubPreguntaAndActivo(aceptar.getFolio(), 16, subpregunta, Constantes.ACTIVO);
			 cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "10" + Constantes.SEPARADORD + aceptar.getUpload().getFilename());
			 generaDocumentoAceptar(aceptar,"10");
			 cuestionarioRepository.save(cue);
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	@Override
	public ResponseUsuario subirRiesgos(SubeDocumento documento){
		ResponseUsuario response = new ResponseUsuario();
		
		 try {
			 generaFolioDirectorio(documento.getFolio());
        	 altaRiesgos(documento);
        	 generaDocumentoRiesgos(documento);
        	 
        	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		return response;
	}
	
	private void altaRiesgos(SubeDocumento sb)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(17);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "11" + Constantes.SEPARADORD + sb.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
	}
	
	private void generaDocumentoRiesgos(SubeDocumento sd){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + "11" + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + "11" + Constantes.SEPARADORD + sd.getUpload().getFilename();
		generaArchivo(ruta, sd.getUpload());
	}
	
	@Override
	public ResponseUsuario subirDictamen(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaDictamen();
        	 actualizaFlujo2(documento.getFolio(), etapa);
        	 generaFolioDirectorio(documento.getFolio());
        	 altaDictamen(documento);
        	 generaDocumentoDictamen(documento);
        	 //Periodo periodo = periodoRepository.findAll().stream().filter(p -> p.getFolio().equals(documento.getFolio())  && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
        	 
        	 Periodo periodo = periodoRepository.findByFolioAndActivo(documento.getFolio(), Constantes.ACTIVO);
        	 periodo.setIdColor(35);
        	 periodoRepository.save(periodo);
        	 
        	 //Flujo flujo = flujoRepositorio.findByFolio(documento.getFolio());
     		 //flujo.setPermiteAcuerdo(-1);
     		 //flujoRepositorio.save(flujo);
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 12 + Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.DEO,ruta);
        	 
        	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerEtapaDictamen() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH6);
		
		return etapa;
	}
	
	private void altaDictamen(SubeDocumento sb)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(18);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "12" + Constantes.SEPARADORD + sb.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	private void generaDocumentoDictamen(SubeDocumento sd){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + "12" + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + "12" + Constantes.SEPARADORD + sd.getUpload().getFilename();
		generaArchivo(ruta, sd.getUpload());
	}
	
	@Override
	public ResponseUsuario solicitarInforme(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaInforme();
        	 actualizaFlujo2(documento.getFolio(), etapa);
        	 generaFolioDirectorio(documento.getFolio());
        	 altaInforme(documento);
        	 actualizaPeriodoValidoInforme(documento.getFolio());
        	 generaDocumentoInforme(documento);
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 13 + Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.SRNV,ruta);
        	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerEtapaInforme() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH7);
		
		return etapa;
	}
	
	private void altaInforme(SubeDocumento sb)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(19);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "13" + Constantes.SEPARADORD + sb.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	private void generaDocumentoInforme(SubeDocumento sd){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + "13" + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + "13" + Constantes.SEPARADORD + sd.getUpload().getFilename();
		generaArchivo(ruta, sd.getUpload());
	}

	@Async
    private void actualizaPeriodoValidoInforme(String folio) {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		try {
			
			while(i<20) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				//dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfechaFinal.toString()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			
			 //Periodo periodo = periodoRepository.findAll().stream().filter(p -> p.getFolio().equals(folio)  && p.getActivo().equals(Constantes.INACTIVO)).findAny().orElse(null);
			 Periodo periodo = periodoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
			 periodo.setFechaInicioPeriodo(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			 periodo.setFechaFinPeriodo(sfechaFinal);
        	 periodo.setDiasPeriodo(20);
        	 periodo.setIdColor(32);
        	 periodo.setActivo("A");
        	 periodoRepository.save(periodo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public ResponseUsuario subirInforme(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaCargaInforme();
        	 actualizaFlujo2(documento.getFolio(), etapa);
        	 generaFolioDirectorio(documento.getFolio());
        	 altaDictamenCargaInforme(documento);
        	 generaDocumentoCargaInforme(documento);
        	 
        	 //Periodo periodo = periodoRepository.findAll().stream().filter(p -> p.getFolio().equals(documento.getFolio())  && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
        	 Periodo periodo = periodoRepository.findByFolioAndActivo(documento.getFolio(), Constantes.ACTIVO);
        	 periodo.setActivo("I");
        	 periodoRepository.save(periodo);
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 14 + Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.SRNVF,ruta);
        	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerEtapaCargaInforme() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH9);
		
		return etapa;
	}
	
	private void altaDictamenCargaInforme(SubeDocumento sb)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(19);
		cue.setSubPregunta(2);
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "14" + Constantes.SEPARADORD + sb.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	private void generaDocumentoCargaInforme(SubeDocumento sd){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + "14" + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + "14" + Constantes.SEPARADORD +sd.getUpload().getFilename();
		generaArchivo(ruta, sd.getUpload());
	}
	
	@Override
	public ResponseUsuario solicitaRia(AceptarFUnoDTO documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaRIA();
        	 actualizaFlujo2(documento.getFolio(), etapa);
        	 generaFolioDirectorio(documento.getFolio());
        	 altaSubirRia(documento);
        	 actualizaPeriodoRiaFlujo2(documento);
        	 generaDocumentoRia(documento,"15");
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 15 + Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.RIA,ruta);
        	 
        	 Periodo pe = periodoRepository.findByFolio(documento.getFolio()).get();
        	 pe.setSuspendido(1);
        	 periodoRepository.save(pe);
        	 
        	 actualizaPeriodoValidoRia(documento.getFolio());
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private void actualizaPeriodoValidoRia(String folio) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fechaInicio = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		try {
			
			while(i<5) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));				
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			SemaforoRia sr = new SemaforoRia();
	       	 sr.setFolio(folio);
	       	 sr.setFechaInicioPeriodo(formatter.format(fechaInicio));
	       	 sr.setFechaFinPeriodo(formatter.format(fechaFinal));
	       	 sr.setDiasPeriodo(5);
	       	 sr.setIdColor(32);
	       	 sr.setActivo(Constantes.ACTIVO);
	       	 semaforoRepository.save(sr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String obtenerEtapaRIA() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH4);
		
		return etapa;
	}
	
	private void altaSubirRia(AceptarFUnoDTO a)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		cue.setPregunta(20);
		cue.setSubPregunta(1);
		cue.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "15" + Constantes.SEPARADORD + a.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	@Async
	private void actualizaPeriodoRiaFlujo2(AceptarFUnoDTO a)throws Exception {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		
			
			while(i<5) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				//dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfechaFinal.toString()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			//Periodo pe = periodoRepository.findAll().stream().filter(f -> f.getFolio().equals(a.getFolio()) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			Periodo pe = periodoRepository.findByFolioAndActivo(a.getFolio(), Constantes.ACTIVO);
			pe.setFechaInicioPeriodo(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			pe.setFechaFinPeriodo(sfechaFinal);
			pe.setDiasPeriodo(5);
			pe.setIdColor(32);
			pe.setActivo("A");
			
			periodoRepository.save(pe);
	}
	
	private void generaDocumentoRia(AceptarFUnoDTO a, String pregunta){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + pregunta + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + pregunta + Constantes.SEPARADORD +a.getUpload().getFilename();
		generaArchivo(ruta, a.getUpload());
	}
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo2(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getPregunta() == 20 && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
			cuestionario = cuestionarioRepository.findByFolioAndPregunta(folio, 20);
			if(cuestionario !=null) {
				cue = cuestionario.stream().map(CuestionarioDTO::new).collect(Collectors.toList());
			}
			
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setEstatus(Constantes.ERROR);
 			response.setMensaje(Constantes.ERROR);
		}
		
		return cue;
	}

	@Override
	public ResponseUsuario aceptarRiaDos(AceptarFUnoDTO aceptar) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaAceptar();
        	 actualizaFlujo2(aceptar.getFolio(), etapa);
        	 generaFolioDirectorio(aceptar.getFolio());
        	 actualizaPeriodoFlujo2(aceptar); 
        	         	 
        	 altaSecuenciaFlujo(aceptar.getFolio(), Constantes.RIAADA,"");        	         	
        	 altaSecuenciaFlujo(aceptar.getFolio(), Constantes.ACUERDO, "");
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	@Override
	public ResponseUsuario subirRiaRequerida(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerSubirEtapaRIA();
        	 actualizaFlujo2(documento.getFolio(), etapa);
        	 generaFolioDirectorio(documento.getFolio());
        	 altaSubirRia(documento);
        	 actualizaPeriodoSubirRIaFlujo2(documento.getFolio());
        	 generaDocumentoSubirRia(documento);
        	 
        	 
        	 String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + 16 + Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.RRIA,ruta);
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerSubirEtapaRIA() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH5);
		
		return etapa;
	}
	
	private void altaSubirRia(SubeDocumento sb)throws Exception {
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(20);
		cued.setSubPregunta(2);
		cued.setRutaArchivo(dirflujo2 + folioDir + Constantes.SEPARADORD + "16" + Constantes.SEPARADORD + sb.getUpload().getFilename());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}

	private void generaDocumentoSubirRia(SubeDocumento sd){
		validaRutaGeneral(dirflujo2 + folioDir + Constantes.SEPARADORD + "16" + Constantes.SEPARADORD);
		String ruta = dirflujo2 + folioDir + Constantes.SEPARADORD + "16" + Constantes.SEPARADORD + sd.getUpload().getFilename();
		generaArchivo(ruta, sd.getUpload());
	}

	@Async
	private void actualizaPeriodoSubirRIaFlujo2(String folio)throws Exception {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		
			
			while(i<5) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				//dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfechaFinal.toString()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			//Periodo pe = periodoRepository.findAll().stream().filter(f -> f.getFolio().equals(folio) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			Periodo pe = periodoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
			pe.setFechaInicioPeriodo(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			pe.setFechaFinPeriodo(sfechaFinal);
			pe.setDiasPeriodo(5);
			pe.setIdColor(32);
			pe.setActivo("A");
			
			periodoRepository.save(pe);
	}
	
	@Override
	public ResponseUsuario notificaRiaNoPresentada(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		AceptarFUnoDTO a = new AceptarFUnoDTO();
		
		 try {
        	 altaRiaNoPresentada(documento);
        	 a.setUpload(documento.getUpload());
        	 a.setFolio(documento.getFolio());
        	 generaDocumentoRiaNoPresentada(a);
        	 
        	 
        	 String dirFolio = documento.getFolio().replaceAll("/", "_");
        	 dirFolio = dirFolio.replaceAll("-", "_");
        	 actualizaFlujo1(documento.getFolio(), Constantes.ADNPF);    
        	 
        	 if(a.getFolio().contains("INAI/SPDP/DGNC/EIPDP-IE")) {
        		 String ruta = dirflujo3+dirFolio+Constantes.SEPARADORD+"24"+Constantes.SEPARADORD + documento.getUpload().getFilename();
            	 altaSecuenciaFlujo(documento.getFolio(), Constantes.ADNPF, ruta);            	 
        	 }else {
        		 String ruta = dirflujo2+dirFolio+Constantes.SEPARADORD+"24"+Constantes.SEPARADORD + documento.getUpload().getFilename();
            	 altaSecuenciaFlujo(documento.getFolio(), Constantes.ADNPF, ruta);
        	 }
        	 
        	 Periodo p = periodoRepository.findByFolio(documento.getFolio()).get();
        	 p.setActivo(Constantes.INACTIVO);
        	 periodoRepository.save(p);
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private void actualizaFlujo1(String folio, String etapa)throws Exception {		
		Flujo flujo = flujoRepositorio.findByFolio(folio);
		flujo.setEtapa(etapa);
		flujoRepositorio.save(flujo);
	}
	
	
	private void generaDocumentoRiaNoPresentada(AceptarFUnoDTO a){
	 String dirFolio = a.getFolio().replaceAll("/", "_");
   	 dirFolio = dirFolio.replaceAll("-", "_");
   	 String ruta="";
   	 String rutaCre="";
   	 
   	 if(a.getFolio().contains("INAI/SPDP/DGNC/EIPDP-IE")) {
   		dirFolio = a.getFolio().replaceAll("/", "");
   		ruta = dirflujo3+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"24"+Constantes.SEPARADORD+a.getUpload().getFilename();
		rutaCre = dirflujo3+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"24"; 
   	 }else {
   		ruta = dirflujo2+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"24"+Constantes.SEPARADORD+a.getUpload().getFilename();
		rutaCre = dirflujo2+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"24";
   	 }
		
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, a.getUpload());
	}
	
	private void altaRiaNoPresentada(SubeDocumento a)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		if(a.getFolio().contains("INAI/SPDP/DGNC/EIPDP-IE")) {
			cue.setPregunta(24);
			cue.setRutaArchivo(dirflujo3 + a.getFolio().replace("/", "") + Constantes.SEPARADORD + "24" + Constantes.SEPARADORD + a.getUpload().getFilename());
		}else {
			cue.setPregunta(24);
			String ruta = a.getFolio().replaceAll("/", "_");
			cue.setRutaArchivo(dirflujo2 + ruta + Constantes.SEPARADORD + "24" + Constantes.SEPARADORD + a.getUpload().getFilename());
		}
		cue.setSubPregunta(1);
		cue.setRespuesta(a.getAsunto());		
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			cuestionario = cuestionarioRepository.findByFolioAndPregunta(folio, 24);
			
			if(cuestionario !=null) {
				cue = cuestionario.stream().map(CuestionarioDTO::new).collect(Collectors.toList());
			}
			
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setEstatus(Constantes.ERROR);
 			response.setMensaje(Constantes.ERROR);
		}
		
		return cue;
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
	public ResponseUsuario acuerdoAdmisionf2(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		
		Flujo flujo = flujoRepositorio.findByFolioAndActivo(documento.getFolio(), Constantes.ACTIVO);
		
		 try {
        	 etapa = obtenerEtapaGenerado();
        	 
        	 if(flujo.getPermiteAcuerdo()==null) {        		     
        		 flujo.setEtapa(etapa);
        		 flujoRepositorio.save(flujo);        		 
        	 }
        	 
        	 altaAcuerdo(documento);        	 
        	 generaDocumentoAdmision(documento);    
        	 
        	 
        	 SecuenciaFlujo se = secuenciaReposiroty.findByFolioAndEtapa(documento.getFolio(), Constantes.ACUERDO);
        	 String ruta = dirflujo2+folioDir+Constantes.SEPARADORD+"23"+Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 if(se!=null) {
        		 se.setRutaArchivo(ruta);
        		 secuenciaReposiroty.save(se);
        	 }else {
        		 altaSecuenciaFlujo(documento.getFolio(), Constantes.ACUERDO, ruta);
        	 }
        	 
        	 
        	 if(flujo.getPermiteAcuerdo()==null) {
        		 altaSecuenciaFlujo(documento.getFolio(), Constantes.GDM, "");
        	 }
        	 
        	 flujo.setPermiteAcuerdo(0);
        	 flujoRepositorio.save(flujo);
        	 
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
				
		return response;
	}
	
	private String obtenerEtapaGenerado() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH);
		
		return etapa;
	}
	
	private void altaAcuerdo(SubeDocumento sb)throws Exception {
		
		folioDir = sb.getFolio().replaceAll("/", "_");
		folioDir =folioDir.replaceAll("-", "_");
		
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(23);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(23);
		cued.setSubPregunta(2);
		cued.setRutaArchivo(dirflujo2+folioDir+Constantes.SEPARADORD+"23"+Constantes.SEPARADORD + sb.getUpload().getFilename());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}
	
	private void generaDocumentoAdmision(SubeDocumento sd){
		String ruta = dirflujo2+folioDir+Constantes.SEPARADORD+"23"+Constantes.SEPARADORD + sd.getUpload().getFilename();
		String rutaCre = dirflujo2+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"23";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, sd.getUpload());
	}
	
	@Override
	public ResponseUsuario terminarNoVinculantes(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		
		
		 try {
			 Flujo flujo = flujoRepositorio.findByFolioAndActivo(documento.getFolio(), Constantes.ACTIVO);

			 String etapa = Constantes.FINALIZADO;  		     
        	 flujo.setEtapa(etapa);
        	 flujo.setPermiteAcuerdo(0);
        	 flujoRepositorio.save(flujo);
        	         	         	         	         	        	      
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.FINALIZADO, "");
        	 
        	 Periodo pe = periodoRepository.findByFolioAndActivo(documento.getFolio(),Constantes.ACTIVO);
        	 pe.setActivo(Constantes.INACTIVO);
        	 periodoRepository.save(pe);
        	         	         	         	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
				
		return response;
	}
}
	

