package mx.org.inai.serviceImpl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.DocumentosDTO;
import mx.org.inai.service.IDocumentosService;

@ConfigurationProperties("eipdp")
@Service
public class DocumentosServiceImpl implements IDocumentosService{
	@NotNull
    private String ruta;
	
	//private Logger logger = LoggerFactory.getLogger(DocumentosServiceImpl.class);

	@Override
	public byte[]  aviso() {
		byte[] pdfData=null;
		
		try {
			File pdfFile = new File(ruta+Constantes.SEPARADOR+Constantes.AVISO_PRIVACIDAD);
			pdfData = new byte[(int) pdfFile.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(pdfFile));
			dis.readFully(pdfData);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pdfData;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Override
	public DocumentosDTO avison() {
		byte[] pdfData=null;
		DocumentosDTO doc= new DocumentosDTO();
		try {
			File pdfFile = new File(ruta+Constantes.SEPARADOR+Constantes.AVISO_PRIVACIDAD);
			pdfData = new byte[(int) pdfFile.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(pdfFile));
			dis.readFully(pdfData);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		doc.setNombre(Constantes.AVISO_PRIVACIDAD);
		doc.setData(pdfData);
		
		return doc;
	}
}

	
	

