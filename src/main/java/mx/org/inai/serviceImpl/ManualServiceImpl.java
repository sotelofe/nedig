package mx.org.inai.serviceImpl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.ManualesDTO;
import mx.org.inai.dto.ResponseArchivo;
import mx.org.inai.repository.ManualesRepository;
import mx.org.inai.service.IManualesService;

@ConfigurationProperties("manuales")
@Service
public class ManualServiceImpl implements IManualesService{

	@Autowired
	private ManualesRepository manualRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	
	@NotNull
    private String ruta;
	
	@Override
	public List<ManualesDTO> obtenerLista() {		
		return manualRepository.findByActivoOrderByOrdenAsc(Constantes.ACTIVO).stream().map(ma -> modelMapper.map(ma, ManualesDTO.class)).collect(Collectors.toList());		
	}
	
	@Override
	public byte[]  getDocumento(String nombreDocumento) {
		byte[] pdfData=null;
		
		try {
			File pdfFile = new File(ruta+nombreDocumento);
			pdfData = new byte[(int) pdfFile.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(pdfFile));
			dis.readFully(pdfData);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pdfData;
	}
	
	@Override
	public ResponseArchivo descargaDocumento(String nombreDocumento)  {	
		ResponseArchivo res = new ResponseArchivo();
		String b64="";
		try {
			File pdfFile = new File(ruta+nombreDocumento);					
			byte [] bytes = Files.readAllBytes(pdfFile.toPath());
            b64 = Base64.getEncoder().encodeToString(bytes);	
            res.setBase64(b64);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return res;
	}
	
	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
}

	
	

