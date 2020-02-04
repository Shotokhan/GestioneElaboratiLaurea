package entity;

import enumeration.StatoRichiesta;

public class Preferenza {

	private int idPreferenza = 0;
	private Richiesta richiesta = null;
	private Elaborato elaborato = null;
	private StatoRichiesta stato = StatoRichiesta.IN_ATTESA;
	
	public int getIdPreferenza() {
		return idPreferenza;
	}
	public void setIdPreferenza(int idPreferenza) {
		this.idPreferenza = idPreferenza;
	}
	public Richiesta getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(Richiesta richiesta) {
		this.richiesta = richiesta;
	}
	public Elaborato getElaborato() {
		return elaborato;
	}
	public void setElaborato(Elaborato elaborato) {
		this.elaborato = elaborato;
	}
	public StatoRichiesta getStato() {
		return stato;
	}
	public void setStato(StatoRichiesta stato) {
		this.stato = stato;
	}
}
