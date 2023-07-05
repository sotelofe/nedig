package mx.org.inai.service;

public interface IEncryptService {
	public  String base64encode(String text,  String key);
	public  String base64decode(String text,  String key);
}
