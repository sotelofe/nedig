package mx.org.inai.serviceImpl;

import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import mx.org.inai.service.IEncryptService;

@Service
public class EncryptService implements IEncryptService{
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Override
	public String base64encode(String text, String key) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(key);
		String myEncryptedText = textEncryptor.encrypt(text);
		return myEncryptedText;
	}
	
	@Override
	public String base64decode(String text,  String key) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(key);
		String plainText = textEncryptor.decrypt(text);
		return plainText;
	}

	public Logger getLogger() {
		return null;
	}

	public void setLogger(Logger logger) {
		//this.logger = logger;
	}
}
