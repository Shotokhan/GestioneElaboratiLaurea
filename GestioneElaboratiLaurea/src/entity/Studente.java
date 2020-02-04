package entity;

public class Studente {

	private int idStudente = 0;
	private int CFU = 0;
	private Assegnazione elaborato = null;
	
	public int getIdStudente() {
		return idStudente;
	}
	public void setIdStudente(int idStudente) {
		this.idStudente = idStudente;
	}
	public int getCFU() {
		return CFU;
	}
	public void setCFU(int CFU) {
		this.CFU = CFU;
	}
	public Assegnazione getElaborato() {
		return elaborato;
	}
	public void setElaborato(Assegnazione elaborato) {
		this.elaborato = elaborato;
	}
	
}
