package entity;

import java.util.ArrayList;

public class Docente {
	
	private int idDocente = 0;
	private ArrayList<Elaborato> listaElaborati = null;
	private ArrayList<Assegnazione> listaAssegnazioni = null;
	
	public int getIdDocente() {
		return idDocente;
	}
	public void setIdDocente(int idDocente) {
		this.idDocente = idDocente;
	}
	public ArrayList<Elaborato> getListaElaborati() {
		return listaElaborati;
	}
	public void setListaElaborati(ArrayList<Elaborato> listaElaborati) {
		this.listaElaborati = listaElaborati;
	}
	public ArrayList<Assegnazione> getListaAssegnazioni() {
		return listaAssegnazioni;
	}
	public void setListaAssegnazioni(ArrayList<Assegnazione> listaAssegnazioni) {
		this.listaAssegnazioni = listaAssegnazioni;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Docente) {
			Docente other = (Docente) obj;
			return this.getIdDocente() == other.getIdDocente();
		} else {
			return false;
		}
	}
	
}
