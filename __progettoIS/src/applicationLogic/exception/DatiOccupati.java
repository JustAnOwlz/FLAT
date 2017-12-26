package applicationLogic.exception;

public class DatiOccupati extends Exception {
	private static final long serialVersionUID = 1L;
	private String campo = null;
	
	public DatiOccupati() {
		super("Dati gi� in uso");
	}

	public DatiOccupati(String message) {
		super("Dati gi� in uso: " + message);
		this.campo = message;
	}
	
	public String getCampo() {
		return campo;
	}
	
	public void setCampo(String campo) {
		this.campo = campo;
	}

	
}
