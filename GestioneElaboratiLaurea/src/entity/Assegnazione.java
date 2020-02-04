package entity;

import java.util.Date;

public class Assegnazione extends Elaborato {

	private int idAssegnazione;
	private Studente studente;
	private Date dataAssegnazione;
	
	public Assegnazione(Elaborato father) {
		super(father);
		idAssegnazione = 0;
		studente = null;
		dataAssegnazione = null;
	}
	
	public Assegnazione(Assegnazione other) {
		super(other);
		this.setIdAssegnazione(other.getIdAssegnazione());
		this.setStudente(other.getStudente());
		this.setDataAssegnazione(other.getDataAssegnazione());
	}

	public int getIdAssegnazione() {
		return idAssegnazione;
	}

	public void setIdAssegnazione(int idAssegnazione) {
		this.idAssegnazione = idAssegnazione;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Date getDataAssegnazione() {
		return dataAssegnazione;
	}

	public void setDataAssegnazione(Date dataAssegnazione) {
		this.dataAssegnazione = dataAssegnazione;
	}

}
