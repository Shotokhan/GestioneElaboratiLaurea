package entity;

import java.util.ArrayList;
import java.util.Date;

public class SedutaDiLaurea {

	private int idSedutaDiLaurea = 0;
	private ArrayList<Studente> listaStudenti = null;
	private ArrayList<Docente> listaDocenti = null;
	private Date scadenzaCaricamentoElaborati = null;
	
	public int getIdSedutaDiLaurea() {
		return idSedutaDiLaurea;
	}
	public void setIdSedutaDiLaurea(int idSedutaDiLaurea) {
		this.idSedutaDiLaurea = idSedutaDiLaurea;
	}
	public ArrayList<Studente> getListaStudenti() {
		return listaStudenti;
	}
	public void setListaStudenti(ArrayList<Studente> listaStudenti) {
		this.listaStudenti = listaStudenti;
	}
	public ArrayList<Docente> getListaDocenti() {
		return listaDocenti;
	}
	public void setListaDocenti(ArrayList<Docente> listaDocenti) {
		this.listaDocenti = listaDocenti;
	}
	public Date getScadenzaCaricamentoElaborati() {
		return scadenzaCaricamentoElaborati;
	}
	public void setScadenzaCaricamentoElaborati(Date scadenzaCaricamentoElaborati) {
		this.scadenzaCaricamentoElaborati = scadenzaCaricamentoElaborati;
	}
}
