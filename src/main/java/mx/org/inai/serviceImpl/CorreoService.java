package mx.org.inai.serviceImpl;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.model.Catalogo;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.service.ICorreoService;

@ConfigurationProperties("eipdp")
@Service
public class CorreoService implements ICorreoService{
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String smtp;
	private String puerto;
	private String usuario;
	private String passWord;
	private MimeMessage msg;
	private Session session;
	
	@Autowired
	private CatalogoRepository catalogoRepositorio;
	
	@NotNull
    private String imagenescorreo1;
	
	@NotNull
    private String imagenescorreo2;
	
	public void enviaCorreoNotificacionAltaUsuario() {
		Catalogo catalogo = catalogoRepositorio.findAll().stream().filter(c -> c.getClave().equals(Constantes.SMTP) && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		List<Catalogo> listCatalogo = catalogoRepositorio.findAll().stream().filter(ca ->  ca.getIdSubCatalogo() == catalogo.getIdCatalogo()).collect(Collectors.toList());
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("url")) {
				smtp = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("puerto")) {
				puerto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("usuario")) {
				usuario = (listCatalogo.get(i).getValor() == null || listCatalogo.get(i).getValor().equals(""))?"":listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("password")) {
				passWord = (listCatalogo.get(i).getValor() == null || listCatalogo.get(i).getValor().equals(""))?"":listCatalogo.get(i).getValor();
				continue;
			}
		}
	}
	
	public void  configurarCorreo() {
		Properties props = new Properties();
    	//props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtp);
    	props.put("mail.smtp.port", puerto); 
    	//props.put("mail.smtp.auth", "true");
    	//props.put("mail.smtp.starttls.enable", "true");
    	session = Session.getInstance(props);   	 
	} 
	
	@Async
	public void enviarCorreo(String de, String nombreDe, String para, String asunto, String body) {
		
		Transport transport = null;
		
        try{
        	msg = new MimeMessage(session);
    		msg.setFrom(new InternetAddress(de,nombreDe));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
            msg.setSubject(asunto);
            msg.setContent(body,"text/html");
            
            
         
            MimeMultipart multipart = new MimeMultipart();
          
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = body;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
          
            BodyPart imgPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(imagenescorreo1);
            imgPart.setDataHandler(new DataHandler(fds));
            imgPart.setHeader("Content-ID", "<head>");
            
            BodyPart imgPart2 = new MimeBodyPart();
            DataSource fdsd = new FileDataSource(imagenescorreo2);
            imgPart2.setDataHandler(new DataHandler(fdsd));
            imgPart2.setHeader("Content-ID", "<head2>");
            multipart.addBodyPart(imgPart);
            multipart.addBodyPart(imgPart2);
          
            msg.setContent(multipart);
            
            
            
            transport = session.getTransport("smtp");
            //transport.connect(smtp, usuario, passWord);
            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            //logger.info("Mensaje enviado");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            //logger.info(ex.getMessage());
        }
        finally
        {
            try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			//	logger.info(e.getMessage());
			}
        }
		
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public MimeMessage getMsg() {
		return msg;
	}

	public void setMsg(MimeMessage msg) {
		this.msg = msg;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getImagenescorreo1() {
		return imagenescorreo1;
	}

	public void setImagenescorreo1(String imagenescorreo1) {
		this.imagenescorreo1 = imagenescorreo1;
	}

	public String getImagenescorreo2() {
		return imagenescorreo2;
	}

	public void setImagenescorreo2(String imagenescorreo2) {
		this.imagenescorreo2 = imagenescorreo2;
	}	
	
	
}
