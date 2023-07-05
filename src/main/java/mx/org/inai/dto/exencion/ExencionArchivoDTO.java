package mx.org.inai.dto.exencion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

/**
 * The Class ExencionArchivoDTO.
 *
 * @author A. Juarez
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExencionArchivoDTO extends ExencionAbstractBaseDTO {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6582787203597522557L;

	/** The filename. */
    private String filename;
    
    /** The filetype. */
    private String filetype;
    
    /** The value. */
    @ToString.Exclude private String value;

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
