package mx.org.inai.dto;

import java.io.Serializable;

public class UploadF implements Serializable {

	private static final long serialVersionUID = 1L;

	public  String filename;
    public  String filetype;
    public  String value;
	
    public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}