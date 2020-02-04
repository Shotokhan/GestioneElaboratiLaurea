package entity;

public class Elaborato {

	private int idElaborato = 0;
	private Docente docente = null;
	private String insegnamento = null;
	
	public Elaborato(Elaborato other) {
		this.setIdElaborato(other.getIdElaborato());
		this.setDocente(other.getDocente());
		this.setInsegnamento(other.getInsegnamento());
	}
	
	public int getIdElaborato() {
		return idElaborato;
	}
	public void setIdElaborato(int idElaborato) {
		this.idElaborato = idElaborato;
	}
	public Docente getDocente() {
		return docente;
	}
	public void setDocente(Docente docente) {
		this.docente = docente;
	}
	public String getInsegnamento() {
		return insegnamento;
	}
	public void setInsegnamento(String insegnamento) {
		this.insegnamento = insegnamento;
	}
}
