package entity;

import java.util.ArrayList;

import enumeration.StatoRichiesta;

public class Richiesta {

	private int idRichiesta = 0;
	private Studente studente = null;
	private ArrayList<Preferenza> listaPreferenze = null;
	private StatoRichiesta statoRichiesta = StatoRichiesta.IN_ATTESA;
	
	public int getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(int idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Studente getStudente() {
		return studente;
	}
	public void setStudente(Studente studente) {
		this.studente = studente;
	}
	public ArrayList<Preferenza> getListaPreferenze() {
		return listaPreferenze;
	}
	public void setListaPreferenze(ArrayList<Preferenza> listaPreferenze) {
		this.listaPreferenze = listaPreferenze;
	}
	public StatoRichiesta getStatoRichiesta() {
		return statoRichiesta;
	}
	public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}
}
