package mx.org.inai.dto.exencion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.NoArgsConstructor;

/**
 * The Class AbstractBaseDTO.
 *
 * @author A. Juarez
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(
        ignoreUnknown = true)
@NoArgsConstructor
public abstract class ExencionAbstractBaseDTO implements java.io.Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

}
