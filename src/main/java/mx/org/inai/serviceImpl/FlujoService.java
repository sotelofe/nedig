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
import mx.org.inai.dto.FormPreconsultaDTO;
import mx.org.inai.dto.ResponseUsuario;
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
import mx.org.inai.service.IFlujoService;

@ConfigurationProperties("eipdp")
@Service
public class FlujoService implements IFlujoService{
	
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
    private String dirflujo1;
	
	@Autowired
	ICatalogoService iCatalogoService;
	
	@Autowired
	PeriodoRepository periodoRepository;
	
	@Autowired
	DiaRepository diaRepository;
	
	@Autowired
	SecuenciaFlujoRepository secuenciaReposiroty;
	
	@Autowired
	private SemaforoRiaRepository semaforoRepository;
	
	String sfechaFinal = null;
	
	private Usuario usuarios;
	
	String folioDir;
	
	@Override
	public ResponseUsuario altaFlujo1(FormPreconsultaDTO preconsulta) {
		ResponseUsuario response = new ResponseUsuario();
		String folio = "";
		String etapa = "";
		Flujo flu = null;
		Cuestionario cue = null;
		
		
        try {
			
        	 folio  = generarFolio();
        	 etapa  = obtenerEtapa();
        	 flu    = altaFlujo(preconsulta, folio, etapa);
        	 altaPreCuestionario(preconsulta, folio);
        	 cue    = altaCuestionario(preconsulta, folio);
        	 generaDocumento(folio, preconsulta);
        	 enviarCorreoAltaFlujo1(preconsulta, folio);
        	 actualizaPeriodoValido(folio);
        	 String fol = folio.replaceAll("/", "");
        	 altaSecuenciaFlujo(folio, etapa, dirflujo1+fol+".docx");
        	 
        	 if(flu.getIdFlujo() !=null && cue.getIdCuestionario() !=null) {
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
	
	private String generarFolio() {
		String sFolio = "";
		Integer numeroFolio = 0;
		String sNumeroFolio = "";
		String clave="";
		LocalDate fecha=null;
		
		clave = obtenerClave(Constantes.KEY_FOLIO, Constantes.KEY_FOLIO1 );
		numeroFolio = obtenerSecuenciaFlujo();
		sNumeroFolio = generaCadenaSecuenciaFolio(numeroFolio);
		fecha = obtenerFecha();
		
		sFolio = clave+Constantes.SEPARADORD+ sNumeroFolio +Constantes.SEPARADORD+fecha.getYear();
		folioDir = clave+Constantes.SEPARADORBA+ sNumeroFolio +Constantes.SEPARADORBA+fecha.getYear();
		folioDir = folioDir.replace('-', '_').replaceAll("/", "_");
		
		return sFolio;
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
		
		//folio = folioRepositorio.findAll().stream().filter(fo -> fo.getIdFolio() == 1 && fo.getActivo().equals("A")).findAny().orElse(null);
		folio = folioRepositorio.findByIdFolioAndActivo(1, Constantes.ACTIVO);
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

	private Flujo altaFlujo(FormPreconsultaDTO preconsulta, String folio, String etapa) {
		Flujo flu = new Flujo();
		flu.setIdUsuario(preconsulta.getIdUsuario());
		flu.setFlujo(1);
		flu.setFolio(folio);
		flu.setEtapa(etapa);
		flu.setFecha(obtenerFecha().toString());
		flu.setActivo("A");
		flujoRepositorio.save(flu);
		
		return flu;
	}

	private Cuestionario altaPreCuestionario(FormPreconsultaDTO preconsulta, String folio) {
		Cuestionario preCue = new Cuestionario();
		Cuestionario preCue1 = new Cuestionario();
		Cuestionario preCue2 = new Cuestionario();
		Cuestionario preCue3 = new Cuestionario();
		Cuestionario preCue4 = new Cuestionario();
		Cuestionario preCue5 = new Cuestionario();
		
		if(preconsulta.getRadioPreconsulta() !=null && !preconsulta.getRadioPreconsulta().equals("")) {
			preCue.setFolio(folio);
			preCue.setPregunta(1);
			preCue.setRespuesta(preconsulta.getRadioPreconsulta());
			preCue.setActivo("A");
			cuestionarioRepository.save(preCue);
		}
		
		if(preconsulta.getRadioPreconsultab() !=null && !preconsulta.getRadioPreconsultab().equals("")) {
			preCue1.setFolio(folio);
			preCue1.setPregunta(1);
			preCue1.setSubPregunta(1);
			preCue1.setRespuesta(preconsulta.getRadioPreconsultab());
			preCue1.setActivo("A");
			cuestionarioRepository.save(preCue1);
		}
		
		if(preconsulta.getRadioPreconsultaeg1() !=null && !preconsulta.getRadioPreconsultaeg1().equals("")) {
			preCue2.setFolio(folio);
			preCue2.setPregunta(1);
			preCue2.setSubPregunta(2);
			preCue2.setRespuesta(preconsulta.getRadioPreconsultaeg1());
			preCue2.setActivo("A");
			cuestionarioRepository.save(preCue2);
		}
		
		if(preconsulta.getRadioPreconsultaeg2() !=null && !preconsulta.getRadioPreconsultaeg2().equals("")) {
			preCue3.setFolio(folio);
			preCue3.setPregunta(1);
			preCue3.setSubPregunta(3);
			preCue3.setRespuesta(preconsulta.getRadioPreconsultaeg2());
			preCue3.setActivo("A");
			cuestionarioRepository.save(preCue3);
		}
		
		if(preconsulta.getRadioPreconsultaf() !=null && !preconsulta.getRadioPreconsultaf().equals("")) {
			preCue4.setFolio(folio);
			preCue4.setPregunta(1);
			preCue4.setSubPregunta(4);
			preCue4.setRespuesta(preconsulta.getRadioPreconsultaf());
			preCue4.setActivo("A");
			cuestionarioRepository.save(preCue4);
		}
		
		if(preconsulta.getUpload1() !=null && !preconsulta.getUpload1().filename.equals("") && !preconsulta.getUpload1().filename.equals("no cargado")) {
			String ruta = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"1"+Constantes.SEPARADORD+preconsulta.getUpload1().getFilename();
			preCue5.setFolio(folio);
			preCue5.setPregunta(1);
			preCue5.setSubPregunta(5);
			preCue5.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"1"+Constantes.SEPARADORD+preconsulta.getUpload1().getFilename());
			preCue5.setActivo("A");
			cuestionarioRepository.save(preCue5);
			
			
			String rutaCre = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"1";
			validaRutaGeneral(rutaCre);
			generaArchivo(ruta, preconsulta.getUpload1());
		}
		
		return preCue;
	}
	
	private void  generaArchivo(String ruta, UploadF upload) {
		
		try (FileOutputStream stream = new FileOutputStream(ruta)) {
			byte[] decodedBytes = Base64.getDecoder().decode(upload.getValue().getBytes("UTF-8"));
		    stream.write(decodedBytes);
		}catch (Exception e) {
		//	logger.error(e.getMessage());
		}
	}
	
	private Cuestionario altaCuestionario(FormPreconsultaDTO preconsulta, String folio) {
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
		
		cue.setFolio(folio);
		cue.setPregunta(2);
		cue1.setSubPregunta(1);
		cue.setRespuesta(preconsulta.getRadioConsulta1());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
		cue1.setFolio(folio);
		cue1.setPregunta(2);
		cue1.setSubPregunta(2);
		cue1.setRespuesta(preconsulta.getHipervinculo());
		cue1.setActivo("A");
		cuestionarioRepository.save(cue1);
		
		cue2.setFolio(folio);
		cue2.setPregunta(2);
		cue2.setSubPregunta(3);
		cue2.setRespuesta(preconsulta.getRadioConsulta2());
		cue2.setActivo("A");
		cuestionarioRepository.save(cue2);
		
		cue3.setFolio(folio);
		cue3.setPregunta(2);
		cue3.setSubPregunta(4);
		cue3.setRespuesta(preconsulta.getRadioConsulta31());
		cue3.setActivo("A");
		cuestionarioRepository.save(cue3);
		
		cue4.setFolio(folio);
		cue4.setPregunta(2);
		cue4.setSubPregunta(5);
		cue4.setRespuesta(preconsulta.getRadioConsulta32());
		cue4.setActivo("A");
		cuestionarioRepository.save(cue4);
		
		cue5.setFolio(folio);
		cue5.setPregunta(2);
		cue5.setSubPregunta(6);
		cue5.setRespuesta(preconsulta.getRadioConsulta4());
		cue5.setActivo("A");
		cuestionarioRepository.save(cue5);
		
		cue6.setFolio(folio);
		cue6.setPregunta(2);
		cue6.setSubPregunta(7);
		cue6.setRespuesta(preconsulta.getRadioConsulta51());
		cue6.setActivo("A");
		cuestionarioRepository.save(cue6);
		
		cue7.setFolio(folio);
		cue7.setPregunta(2);
		cue7.setSubPregunta(8);
		cue7.setRespuesta(preconsulta.getRadioConsulta52());
		cue7.setActivo("A");
		cuestionarioRepository.save(cue7);
		
		cue8.setFolio(folio);
		cue8.setPregunta(2);
		cue8.setSubPregunta(9);
		cue8.setRespuesta(preconsulta.getRadioConsulta53());
		cue8.setActivo("A");
		cuestionarioRepository.save(cue8);
		
		cue9.setFolio(folio);
		cue9.setPregunta(2);
		cue9.setSubPregunta(10);
		cue9.setRespuesta(preconsulta.getRadioConsulta6());
		cue9.setActivo("A");
		cuestionarioRepository.save(cue9);
		
		cue10.setFolio(folio);
		cue10.setPregunta(2);
		cue10.setSubPregunta(11);
		cue10.setRespuesta(preconsulta.getRelevante());
		cue10.setActivo("A");
		cuestionarioRepository.save(cue10);
		
		cue11.setFolio(folio);
		cue11.setPregunta(3);
		cue11.setSubPregunta(1);
		cue11.setRespuesta(preconsulta.getNombre());
		cue11.setActivo("A");
		cuestionarioRepository.save(cue11);
		
		cue12.setFolio(folio);
		cue12.setPregunta(3);
		cue12.setSubPregunta(2);
		cue12.setRespuesta(preconsulta.getCargo());
		cue12.setActivo("A");
		cuestionarioRepository.save(cue12);
		
		cue13.setFolio(folio);
		cue13.setPregunta(3);
		cue13.setSubPregunta(3);
		cue13.setRespuesta(preconsulta.getUnidad());
		cue13.setActivo("A");
		cuestionarioRepository.save(cue13);
		
		cue14.setFolio(folio);
		cue14.setPregunta(3);
		cue14.setSubPregunta(4);
		cue14.setRespuesta(preconsulta.getCorreo());
		cue14.setActivo("A");
		cuestionarioRepository.save(cue14);
		
		cue15.setFolio(folio);
		cue15.setPregunta(3);
		cue15.setSubPregunta(5);
		cue15.setRespuesta(preconsulta.getTelefono());
		cue15.setActivo("A");
		cuestionarioRepository.save(cue15);
		
		cue16.setFolio(folio);
		cue16.setPregunta(4);
		cue16.setSubPregunta(1);
		cue16.setRespuesta(preconsulta.getDesGenAdi());
		cue16.setActivo("A");
		cuestionarioRepository.save(cue16);
		
		cue17.setFolio(folio);
		cue17.setPregunta(4);
		cue17.setSubPregunta(2);
		cue17.setRespuesta(preconsulta.getDesGenAne());
		cue17.setActivo("A");
		cuestionarioRepository.save(cue17);
		
		String ruta2 = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"2"+Constantes.SEPARADORD + preconsulta.getUpload2().getFilename();
		String ruta3 = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"3"+Constantes.SEPARADORD + preconsulta.getUpload3().getFilename();
		
		cue18.setFolio(folio);
		cue18.setPregunta(2);
		cue18.setSubPregunta(12);
		cue18.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"2"+Constantes.SEPARADORD + preconsulta.getUpload2().getFilename());
		cue18.setActivo("A");
		cuestionarioRepository.save(cue18);
		
		cue19.setFolio(folio);
		cue19.setPregunta(4);
		cue19.setSubPregunta(3);
		cue19.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"3"+Constantes.SEPARADORD + preconsulta.getUpload3().getFilename());
		cue19.setActivo("A");
		cuestionarioRepository.save(cue19);
		
		
		String rutaCre1 = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"2";
		String rutaCre2 = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"3";
		validaRutaGeneral(rutaCre1);
		validaRutaGeneral(rutaCre2);
		generaArchivo(ruta2, preconsulta.getUpload2());
		generaArchivo(ruta3, preconsulta.getUpload3());
		
		
		return cue19;
	}

	public List<FlujoDTO> obtenerListaFlujo1(UsuarioDTO usuario) {
		List<Flujo> listCatalogo = new ArrayList<Flujo>();
		List<FlujoDTO> listaCatalogo = new ArrayList<FlujoDTO>();
		ResponseUsuario response = new ResponseUsuario();
		usuarios = null;
		try {
			
			usuarios = usuarioRepositorio.findByIdUsuarioAndActivo(usuario.getIdUsuario(), Constantes.ACTIVO);
			if(usuarios.getIdPerfil() == 1 || usuarios.getIdPerfil() == 3 ) {
				
				listCatalogo = flujoRepositorio.findByFlujoAndActivo(1, Constantes.ACTIVO);
				listaCatalogo = listCatalogo.stream().map(FlujoDTO::new).collect(Collectors.toList());
				
				for(FlujoDTO fl: listaCatalogo) {
					fl.setUsuario(getNombreUsuario(fl.getFolio(),fl.getFlujo()));
					fl.setFecha(getFechaFormato(fl.getFecha()));
					fl.setIdColor(getIdColor(fl.getFolio()));
					fl.setTuvoRia(tuvoRia(fl.getFolio()));
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
	
				listCatalogo = flujoRepositorio.findByIdUsuarioAndFlujoAndActivo(usuarios.getIdUsuario(), 1, Constantes.ACTIVO);
				listaCatalogo = listCatalogo.stream().map(FlujoDTO::new).collect(Collectors.toList());
				for(FlujoDTO fl: listaCatalogo) {
					fl.setUsuario(getNombreUsuario(fl.getFolio(),fl.getFlujo()));
					fl.setFecha(getFechaFormato(fl.getFecha()));
					fl.setIdColor(getIdColor(fl.getFolio()));
					fl.setTuvoRia(tuvoRia(fl.getFolio()));
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
	
	private Integer getIdcolorRia(String folio) {
		Integer idColor = 0;
		SemaforoRia sem = semaforoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
		if(sem!=null) {
			idColor = sem.getIdColor();
		}
		
		return idColor;
	}
	
	private boolean tieneRiaNoPresentada(String folio) {
		boolean tuvo = false;
		Cuestionario cue= null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 9 && c.getSubPregunta() == 1 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPreguntaAndSubPreguntaAndActivo(folio, 9, 1, Constantes.ACTIVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(cue != null) {
			tuvo = true;
		}
		
		return tuvo;
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
		Cuestionario cue= null;
		try {
			//cue = cuestionarioRepository.findAll().stream().filter(c -> c.getFolio().equals(folio) && c.getPregunta() == 7 && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			cue = cuestionarioRepository.findByFolioAndPreguntaAndActivo(folio, 7, Constantes.ACTIVO);
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
	private void generaDocumento(String folio, FormPreconsultaDTO preconsulta) {
		folio = folio.replace("/", "");		
		
		 try (XWPFDocument doc = new XWPFDocument()) {
			 
			 XWPFParagraph p1 = doc.createParagraph();
			 XWPFRun r1 = p1.createRun();
			 r1.setBold(true);
	         r1.setText("Consultas respecto a la obligación de elaborar y presentar una EIPDP");
	         r1.setColor("AE105A");
	         r1.addBreak(BreakClear.ALL);
	         
	         XWPFParagraph p2 = doc.createParagraph();
			 XWPFRun r2 = p2.createRun();
			 r2.setText(" En caso de haber presentado una consulta respecto a la obligación de elaborar y presentar una EIPDP, indique su resultado:");
			 r2.addBreak(BreakClear.ALL);
			 
			 XWPFParagraph p3 = doc.createParagraph();
			 XWPFRun r3 = p3.createRun();
			 r3.setText("Resultado : "+ preconsulta.getRadioPreconsulta());
			 r3.setColor("AE105A");
	         r3.addBreak(BreakClear.ALL);
			 
			 
			 if( preconsulta.getRadioPreconsulta()==null ||  preconsulta.getRadioPreconsulta().equals("")) {
					XWPFParagraph p4 = doc.createParagraph();
					XWPFRun r4 = p4.createRun();
					r4.setText("Autoevaluación no presentada");
					
				}
				
				
				if( preconsulta.getRadioPreconsulta().equals("B")) {
					
					
					XWPFParagraph p5 = doc.createParagraph(); 
					p5.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r5 = p5.createRun();
					r5.setText("Señale los tratamientos que realiza sobre los cuales tiene duda de que configuren un tratamiento intensivo o relevante de datos personales de carácter general o particular:");
					r5.setColor("AE105A");
					r5.addBreak(BreakClear.ALL);
					 
					XWPFParagraph p6 = doc.createParagraph();
					p6.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r6 = p6.createRun();
					r6.setText(preconsulta.getRadioPreconsultab());
					
				}
				
				
				if( preconsulta.getRadioPreconsulta().equals("D") || preconsulta.getRadioPreconsulta().equals("G")) {
					
					XWPFParagraph p7 = doc.createParagraph();
					p7.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r7 = p7.createRun();
					r7.setText("Tome en cuenta que la tramitación de una consulta respecto a la obligación de elaborar y presentar una EIPDP tiene un plazo de 15 días hábiles contados a partir del día siguiente de su recepción, por lo que, se sugiere considerar si el tratamiento que pretende implementar es de urgencia se contacte directamente con la Dirección General de Normatividad y Consulta o en su caso indique si es posible aplazar la puesta en operación o modificación de una política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales:");
					r7.setColor("AE105A");
					r7.addBreak(BreakClear.ALL);
					 
					XWPFParagraph p8 = doc.createParagraph();
					p8.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r8 = p8.createRun();
					r8.setText(preconsulta.getRadioPreconsultaeg1());
					
					
					XWPFParagraph p9 = doc.createParagraph();
					p9.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r9 = p9.createRun();
					r9.setText("Señale las razones por las cuales el aplazamiento de la puesta en operación o modificación de la de una política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales no afecta su implementación e indique la nueva fecha de manera tentativa o probable:");
					r9.setColor("AE105A");
					r9.addBreak(BreakClear.ALL);
					 
					XWPFParagraph p10 = doc.createParagraph();
					p10.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r10 = p10.createRun();
					r10.setText(preconsulta.getRadioPreconsultaeg2());
					
				}
				
				
				if( preconsulta.getRadioPreconsulta().equals("F")) {
					
					
					XWPFParagraph p11 = doc.createParagraph();
					p11.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun r11 = p11.createRun();
					r11.setText("Proporcione más información del porque considera que es necesario elaborar una EIPDP y no presentar un informe de exención:");
					r11.setColor("AE105A");
					r11.addBreak(BreakClear.ALL);
					 
					XWPFParagraph p12 = doc.createParagraph();
					XWPFRun r12 = p12.createRun();
					r12.setText(preconsulta.getRadioPreconsultaf() );
					
					
				}
			 
				
				
				XWPFParagraph p13 = doc.createParagraph();
				XWPFRun r13 = p13.createRun();
				r13.addBreak(BreakType.PAGE);
				r13.setText("1. Descripción detallada del tratamiento de datos personales que se pretende efectuar o modificar");
				r13.setColor("AE105A");
				r13.addBreak(BreakClear.ALL);
				
				XWPFParagraph p14 = doc.createParagraph();
				XWPFRun r14 = p14.createRun();
				r14.setText("El fundamento que lo habilita a tratar los datos personales conforme a la normatividad que le resulte aplicable");
				r14.setColor("AE105A");
				r14.addBreak(BreakClear.ALL);
				
				
				XWPFParagraph p15 = doc.createParagraph();
				XWPFRun r15 = p15.createRun();
				r15.setText(preconsulta.getRadioConsulta1());
				r15.addBreak(BreakClear.ALL);
				
				if(preconsulta.getRadioConsulta1().equals("Si")) {
					
					XWPFParagraph p16 = doc.createParagraph();
					XWPFRun r16 = p16.createRun();
					r16.setText("Enlace al hipervínculo web");
					r16.setColor("AE105A");
					r16.addBreak(BreakClear.ALL);
					
					XWPFParagraph p17 = doc.createParagraph();
					XWPFRun r17 = p17.createRun();
					r17.setText(preconsulta.getHipervinculo());
					r17.addBreak(BreakClear.ALL);
					
				}
				
				
				XWPFParagraph p18 = doc.createParagraph();
				XWPFRun r18 = p18.createRun();
				r18.setText("Finalidades concretas, lícitas, explícitas y legítimas del tratamiento");
				r18.setColor("AE105A");
				
				XWPFParagraph p19 = doc.createParagraph();
				XWPFRun r19 = p19.createRun();
				r19.setText(preconsulta.getRadioConsulta2());
				r19.addBreak(BreakClear.ALL);
				
				XWPFParagraph p20 = doc.createParagraph();
				XWPFRun r20 = p20.createRun();
				r20.setText("Tipo de datos personales");
				r20.setColor("AE105A");
				r20.addBreak(BreakClear.ALL);
				
				XWPFParagraph p21 = doc.createParagraph();
				XWPFRun r21 = p21.createRun();
				r21.setText("  Datos personales objeto de tratamiento");

				
				XWPFParagraph p22 = doc.createParagraph();
				XWPFRun r22 = p22.createRun();
				r22.setText("     "+preconsulta.getRadioConsulta31());
				
				XWPFParagraph p23 = doc.createParagraph();
				XWPFRun r23 = p23.createRun();
				r23.setText("  Datos personales sensibles");
				
				
				XWPFParagraph p24 = doc.createParagraph();
				XWPFRun r24 = p24.createRun();
				r24.setText("     "+preconsulta.getRadioConsulta32());
				r24.addBreak(BreakClear.ALL);
				
				XWPFParagraph p25 = doc.createParagraph();
				XWPFRun r25 = p25.createRun();
				r25.setText("Categorías de titulares");
				r25.setColor("AE105A");
				r25.addBreak(BreakClear.ALL);
				
				
				XWPFParagraph p26 = doc.createParagraph();
				XWPFRun r26 = p26.createRun();
				r26.setText(preconsulta.getRadioConsulta4());
				r26.addBreak(BreakClear.ALL);
				
				XWPFParagraph p27 = doc.createParagraph();
				XWPFRun r27 = p27.createRun();
				r27.setText("Transferencias de datos personales");
				r27.setColor("AE105A");
				
				
				XWPFParagraph p28 = doc.createParagraph();
				XWPFRun r28 = p28.createRun();
				r28.setText(preconsulta.getRadioConsulta51());
				
				
				if(preconsulta.getRadioConsulta51().equals("Si")) {
					XWPFParagraph p29 = doc.createParagraph();
					XWPFRun r29 = p29.createRun();
					r29.setText(preconsulta.getRadioConsulta52());
					
					XWPFParagraph p30 = doc.createParagraph();
					XWPFRun r30 = p30.createRun();
					r30.setText(preconsulta.getRadioConsulta53());
				}
				
				XWPFParagraph p31 = doc.createParagraph();
				XWPFRun r31 = p31.createRun();
				r31.addBreak(BreakClear.ALL);
				r31.setText("Tecnología utilizada");
				r31.setColor("AE105A");
				r31.addBreak(BreakClear.ALL);
				
				XWPFParagraph p32 = doc.createParagraph();
				XWPFRun r32 = p32.createRun();
				r32.setText(preconsulta.getRadioConsulta6());
				
				
				XWPFParagraph p33 = doc.createParagraph();
				XWPFRun r33 = p33.createRun();
				r33.addBreak(BreakClear.ALL);
				r33.setText("2.	Datos de la persona designada para proporcionar mayor información y/o documentación al respecto");
				r33.setColor("AE105A");
				r33.addBreak(BreakClear.ALL);
				
				XWPFParagraph p34 = doc.createParagraph();
				XWPFRun r34 = p34.createRun();
				r34.setText("Nombre completo");
				r34.setColor("AE105A");
							
				
				XWPFParagraph p35 = doc.createParagraph();
				XWPFRun r35 = p35.createRun();
				r35.setText(preconsulta.getNombre());
				
				XWPFParagraph p36 = doc.createParagraph();
				XWPFRun r36 = p36.createRun();
				r36.addBreak(BreakClear.ALL);
				r36.setText("Cargo");
				r36.setColor("AE105A");
				
				XWPFParagraph p37 = doc.createParagraph();
				XWPFRun r37 = p37.createRun();
				r37.setText(preconsulta.getCargo());
				
			
				XWPFParagraph p38 = doc.createParagraph();
				XWPFRun r38 = p38.createRun();
				r38.addBreak(BreakClear.ALL);
				r38.setText("Unidad administrativa de adscripción");
				r38.setColor("AE105A");
				
				XWPFParagraph p39 = doc.createParagraph();
				XWPFRun r39 = p39.createRun();
				r39.setText(preconsulta.getUnidad());
				
				
				XWPFParagraph p40 = doc.createParagraph();
				XWPFRun r40 = p40.createRun();
				r40.addBreak(BreakClear.ALL);
				r40.setText("Correo electrónico");
				r40.setColor("AE105A");
				r40.addBreak(BreakClear.ALL);
				
				XWPFParagraph p41 = doc.createParagraph();
				XWPFRun r41 = p41.createRun();
				r41.setText(preconsulta.getCorreo());
				
				
				XWPFParagraph p42 = doc.createParagraph();
				XWPFRun r42 = p42.createRun();
				r42.addBreak(BreakClear.ALL);
				r42.setText("Teléfono institucional");
				r42.setColor("AE105A");
				r42.addBreak(BreakClear.ALL);
				
				XWPFParagraph p43 = doc.createParagraph();
				XWPFRun r43 = p43.createRun();
				r43.setText(preconsulta.getTelefono());
				
				
				XWPFParagraph p44 = doc.createParagraph();
				XWPFRun r44 = p44.createRun();
				r44.addBreak(BreakClear.ALL);
				r44.setText("3.	Documentos que el responsable considere conveniente hacer del conocimiento");
				r44.setColor("AE105A");
				r44.addBreak(BreakClear.ALL);
				
				XWPFParagraph p45 = doc.createParagraph();
				XWPFRun r45 = p45.createRun();
				r45.setText("Descripción general de la información adicional:");
				r45.setColor("AE105A");
				
				
				XWPFParagraph p46 = doc.createParagraph();
				XWPFRun r46 = p46.createRun();
				r46.setText(preconsulta.getDesGenAdi());
			
				
				XWPFParagraph p47 = doc.createParagraph();
				XWPFRun r47 = p47.createRun();
				r47.addBreak(BreakClear.ALL);
				r47.setText("Descripción general de los documentos anexos:");
				r47.setColor("AE105A");
				r47.addBreak(BreakClear.ALL);
				
				
				XWPFParagraph p48 = doc.createParagraph();
				XWPFRun r48 = p48.createRun();
				r48.setText(preconsulta.getDesGenAne());
				
			 
	         
	        
			 try (FileOutputStream out = new FileOutputStream(dirflujo1+folio+".docx")) {
	                doc.write(out);
	         }
		 }catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public String getDirflujo1() {
		return dirflujo1;
	}

	public void setDirflujo1(String dirflujo1) {
		this.dirflujo1 = dirflujo1;
	}
	
	private void enviarCorreoAltaFlujo1(FormPreconsultaDTO preconsulta, String folio){
	
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		//Usuario usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getIdUsuario() == preconsulta.getIdUsuario() && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Usuario usuarios = usuarioRepositorio.findByIdUsuarioAndActivo(preconsulta.getIdUsuario(), Constantes.ACTIVO);
		
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
			
			if(listCatalogo.get(i).getClave().equals("textoflujo1")) {
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
		asunto = Constantes.ALTA_FLUJO1;
		
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
			
			while(i<=15) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));				
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
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo1(String folio) {
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
	public ResponseUsuario aceptarfuno(AceptarFUnoDTO aceptar) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaAceptar();
        	 actualizaFlujo1(aceptar.getFolio(), etapa);
        	 altaCuestionarioAceptar(aceptar);
        	 actualizaPeriodoFlujo1(aceptar); 
        	 generaDocumentoAceptar(aceptar);
        	 enviarCorreoAceptarFlujo1(aceptar);
        	 
        	 String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"4"+Constantes.SEPARADORD + aceptar.getUpload().getFilename();
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
	
	private void actualizaFlujo1(String folio, String etapa)throws Exception {		
		Flujo flujo = flujoRepositorio.findByFolio(folio);
		flujo.setEtapa(etapa);
		flujoRepositorio.save(flujo);
	}
	
	private void altaCuestionarioAceptar(AceptarFUnoDTO a)throws Exception {	
		folioDir = a.getFolio().replaceAll("/", "_");
		folioDir = folioDir.replace('-', '_');
		
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		cue.setPregunta(5);
		cue.setSubPregunta(1);
		cue.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"4"+Constantes.SEPARADORD + a.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	@Async
	private void actualizaPeriodoFlujo1(AceptarFUnoDTO a)throws Exception {
		LocalDate fechaActual = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		
			
			while(i<15) {
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
			pe.setDiasPeriodo(15);
			pe.setIdColor(32);
			pe.setActivo("A");
			
			periodoRepository.save(pe);
	}
	
	private void generaDocumentoAceptar(AceptarFUnoDTO a){
		String ruta = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"4"+Constantes.SEPARADORD + a.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"4";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, a.getUpload());
	}
	
	private void enviarCorreoAceptarFlujo1(AceptarFUnoDTO a){
		
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
			
			if(listCatalogo.get(i).getClave().equals("textoaceptarflujo1")) {
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
	public ResponseUsuario generarOpinion(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaOpinion();
        	 actualizaFlujo1(documento.getFolio(), etapa);
        	 altaOpinion(documento);
        	 generaDocumentoOpinion(documento);
        	 //Periodo periodo = periodoRepository.findAll().stream().filter(p -> p.getFolio().equals(documento.getFolio())  && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
        	 Periodo p = periodoRepository.findByFolio(documento.getFolio()).get();
        	 p.setActivo(Constantes.INACTIVO);
        	 periodoRepository.save(p);
        	 
        	 
        	 String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"5"+Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), etapa, ruta);
        	
	         response.setEstatus(Constantes.OK);
	 		 response.setMensaje(Constantes.OK);
	 		 
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		
		return response;
	}
	
	private String obtenerEtapaOpinion() {
		String etapa="";
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH3);
		
		return etapa;
	}
	
	private void altaOpinion(SubeDocumento sb)throws Exception {
		String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"5"+Constantes.SEPARADORD + sb.getUpload().getFilename();
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(6);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(6);
		cued.setSubPregunta(2);
		cued.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"5"+Constantes.SEPARADORD + sb.getUpload().getFilename());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}
	
	private void generaDocumentoOpinion(SubeDocumento sd){
		String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"5"+Constantes.SEPARADORD + sd.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"5";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, sd.getUpload());
	}
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleOpinionFlujo1(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getPregunta() == 6 && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
			cuestionario = cuestionarioRepository.findByFolioAndPregunta(folio, 6);
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
	public ResponseUsuario riafuno(AceptarFUnoDTO ria) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaRIA();
        	 actualizaFlujo1(ria.getFolio(), etapa);
        	 altaCuestionarioRIA(ria);
        	 actualizaPeriodoRiaFlujo1(ria); 
        	 generaDocumentoRia(ria);
        	 
        	 
        	 String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"6"+Constantes.SEPARADORD + ria.getUpload().getFilename();
        	 altaSecuenciaFlujo(ria.getFolio(), etapa, ruta);
        	 
        	 Periodo pe = periodoRepository.findByFolio(ria.getFolio()).get();
        	 pe.setSuspendido(1);
        	 periodoRepository.save(pe);
        	 
        	
        	 actualizaPeriodoValidoRia(ria.getFolio());
        	 
        	 
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
	
	private void altaCuestionarioRIA(AceptarFUnoDTO a)throws Exception {
		String ruta = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"6"+Constantes.SEPARADORD+a.getUpload().getFilename();
		
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		cue.setPregunta(7);
		cue.setSubPregunta(1);
		cue.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"6"+Constantes.SEPARADORD+a.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}

	@Async
	private void actualizaPeriodoRiaFlujo1(AceptarFUnoDTO a)throws Exception {
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

	private void generaDocumentoRia(AceptarFUnoDTO a){
		String dirFolio = a.getFolio().replaceAll("/", "_");
   	 	dirFolio = dirFolio.replaceAll("-", "_");
		String ruta = dirflujo1+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"6"+Constantes.SEPARADORD+a.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"6";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, a.getUpload());
	}
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo1(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getPregunta() == 7 && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
			cuestionario = cuestionarioRepository.findByFolioAndPregunta(folio, 7);
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
	public ResponseUsuario subirRiaRequerida(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerSubirEtapaRIA();
        	 actualizaFlujo1(documento.getFolio(), etapa);
        	 altaSubirRia(documento);
        	 actualizaPeriodoSubirRIaFlujo1(documento.getFolio());
        	 generaDocumentoSubirRia(documento);
        	 
        	 String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"7"+Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.CTR, ruta);
        	 
        	
        	 
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
		String ruta = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"7"+Constantes.SEPARADORD+sb.getUpload().getFilename();
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(8);
		cued.setSubPregunta(1);
		cued.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"7"+Constantes.SEPARADORD+sb.getUpload().getFilename());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}

	private void generaDocumentoSubirRia(SubeDocumento sd){
		String ruta = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"7"+Constantes.SEPARADORD+sd.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"7";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, sd.getUpload());
	}

	@Async
	private void actualizaPeriodoSubirRIaFlujo1(String folio)throws Exception {
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
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo12(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getPregunta() == 8 && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
			cuestionario = cuestionarioRepository.findByFolioAndPregunta(folio, 8);
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
	public ResponseUsuario aceptarRiaUno(AceptarFUnoDTO aceptar) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		 try {
        	 etapa = obtenerEtapaAceptar();
        	 actualizaFlujo1(aceptar.getFolio(), etapa);
        	 
        	 //actualizaPeriodoFlujo1(aceptar); 
        	 Periodo pe = periodoRepository.findByFolio(aceptar.getFolio()).get();
        	 pe.setSuspendido(0);
        	 periodoRepository.save(pe);
        	 
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
        	 String ruta = dirflujo1+dirFolio+Constantes.SEPARADORD+"20"+Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 altaSecuenciaFlujo(documento.getFolio(), Constantes.ADNPF, ruta);
        	 
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
	
	private void generaDocumentoRiaNoPresentada(AceptarFUnoDTO a){
		String dirFolio = a.getFolio().replaceAll("/", "_");
   	 	dirFolio = dirFolio.replaceAll("-", "_");
		String ruta = dirflujo1+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"20"+Constantes.SEPARADORD+a.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+dirFolio+Constantes.SEPARADORD+"20";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, a.getUpload());
	}
	
	private void altaRiaNoPresentada(SubeDocumento a)throws Exception {
		Cuestionario cue = new Cuestionario();
		cue.setFolio(a.getFolio());
		String dirFolio = a.getFolio().replaceAll("/", "_");
   	 	dirFolio = dirFolio.replaceAll("-", "_");
		//if(a.getFolio().substring(0,1).equals("I")) {
			cue.setPregunta(20);
		//}else {
		//	cue.setPregunta(9);
		//}
		cue.setSubPregunta(1);
		cue.setRespuesta(a.getAsunto());
		cue.setRutaArchivo(dirflujo1 +dirFolio+Constantes.SEPARADORD+"20"+Constantes.SEPARADORD+a.getUpload().getFilename());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
	}
	
	
	@Override
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada(String folio) {
		List<CuestionarioDTO> cue = new ArrayList<>();
		ResponseUsuario response = new ResponseUsuario();
		List<Cuestionario> cuestionario = null;
		try {
			//cuestionario = cuestionarioRepository.findAll().stream().filter(cu -> cu.getFolio().equals(folio) && cu.getPregunta() == 9 && cu.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
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
	
	private void validaRutaGeneral(String ruta) {
		 File directorio = new File(ruta);
	        if (!directorio.exists()) {
	            if (directorio.mkdirs()) {
	              
	            } else {
	              
	            }
	        }
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

	@Override
	public ResponseUsuario acuerdoAdmision(SubeDocumento documento) {
		ResponseUsuario response = new ResponseUsuario();
		String etapa="";
		
		
		Flujo flujo = flujoRepositorio.findByFolioAndActivo(documento.getFolio(), Constantes.ACTIVO);
		
		 try {
        	 etapa = obtenerEtapaGenerado();
        	 
        	 if(flujo.getPermiteAcuerdo()==null) {
        		 actualizaFlujo1(documento.getFolio(), etapa);        		
        	 }
        	 
        	 altaAcuerdo(documento);        	 
        	 generaDocumentoAdmision(documento);    
        	 
        	 SecuenciaFlujo se = secuenciaReposiroty.findByFolioAndEtapa(documento.getFolio(), Constantes.ACUERDO);
        	 String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"21"+Constantes.SEPARADORD + documento.getUpload().getFilename();
        	 if(se!=null) {
        		 se.setRutaArchivo(ruta);
        		 secuenciaReposiroty.save(se);
        	 }else {
        		 altaSecuenciaFlujo(documento.getFolio(), Constantes.ACUERDO, ruta);
        	 }
        	         	         	         	
        	 if(flujo.getPermiteAcuerdo()==null) {
        		 altaSecuenciaFlujo(documento.getFolio(), Constantes.GOT, "");
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
		etapa = obtenerClave(Constantes.KEY_ETAPA, Constantes.KEY_ETAPAH2);
		
		return etapa;
	}
	
	private void altaAcuerdo(SubeDocumento sb)throws Exception {
		
		folioDir = sb.getFolio().replaceAll("/", "_");
		folioDir =folioDir.replaceAll("-", "_");
		
		Cuestionario cue = new Cuestionario();
		cue.setFolio(sb.getFolio());
		cue.setPregunta(21);
		cue.setSubPregunta(1);
		cue.setRespuesta(sb.getAsunto());
		cue.setActivo("A");
		cuestionarioRepository.save(cue);
		
		Cuestionario cued = new Cuestionario();
		cued.setFolio(sb.getFolio());
		cued.setPregunta(21);
		cued.setSubPregunta(2);
		cued.setRutaArchivo(dirflujo1+folioDir+Constantes.SEPARADORD+"21"+Constantes.SEPARADORD + sb.getUpload().getFilename());
		cued.setActivo("A");
		cuestionarioRepository.save(cued);

	}
	
	private void generaDocumentoAdmision(SubeDocumento sd){
		String ruta = dirflujo1+folioDir+Constantes.SEPARADORD+"21"+Constantes.SEPARADORD + sd.getUpload().getFilename();
		String rutaCre = dirflujo1+Constantes.SEPARADORD+folioDir+Constantes.SEPARADORD+"21";
		validaRutaGeneral(rutaCre);
		generaArchivo(ruta, sd.getUpload());
	}

}
	

