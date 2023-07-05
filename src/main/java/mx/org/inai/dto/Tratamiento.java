package mx.org.inai.dto;

public class Tratamiento {
	private TratamientoGeneral general;
	private TratamientoParticular particular;

	public TratamientoGeneral getGeneral() {
		return general;
	}

	public void setGeneral(TratamientoGeneral general) {
		this.general = general;
	}

	public TratamientoParticular getParticular() {
		return particular;
	}

	public void setParticular(TratamientoParticular particular) {
		this.particular = particular;
	}

}
