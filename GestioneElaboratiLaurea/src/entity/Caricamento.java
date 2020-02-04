package entity;

import java.util.Date;

public class Caricamento extends Assegnazione{
	
	private int idCaricamento;
	private Date dataCaricamento;
	private SedutaDiLaurea sedutaDiLaurea;
	
	public Caricamento(Elaborato grandfather) {
		super(grandfather);
		idCaricamento = 0;
		dataCaricamento = null;
		sedutaDiLaurea = null;
	}
	
	public Caricamento(Assegnazione father) {
		super(father);
		idCaricamento = 0;
		dataCaricamento = null;
		sedutaDiLaurea = null;
	}

	public int getIdCaricamento() {
		return idCaricamento;
	}

	public void setIdCaricamento(int idCaricamento) {
		this.idCaricamento = idCaricamento;
	}

	public Date getDataCaricamento() {
		return dataCaricamento;
	}

	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	public SedutaDiLaurea getSedutaDiLaurea() {
		return sedutaDiLaurea;
	}

	public void setSedutaDiLaurea(SedutaDiLaurea sedutaDiLaurea) {
		this.sedutaDiLaurea = sedutaDiLaurea;
	}
}
