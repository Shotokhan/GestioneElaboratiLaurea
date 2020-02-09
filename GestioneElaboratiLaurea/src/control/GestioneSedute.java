package control;

import java.util.ArrayList;
import java.util.Date;

import database.DAOException;
import database.DocenteDAO;
import database.SedutaDiLaureaDAO;
import entity.Docente;
import entity.SedutaDiLaurea;

public class GestioneSedute {

	private SedutaDiLaureaDAO sedutaDiLaureaDAO = new SedutaDiLaureaDAO();
	private DocenteDAO docenteDAO = new DocenteDAO();
	
	public SedutaDiLaurea aggiungiSeduta(Date dataScadenzaCaricamento) throws CreazioneSedutaException {
		try {
			return sedutaDiLaureaDAO.create(dataScadenzaCaricamento);
		} catch (DAOException e) {
			throw new CreazioneSedutaException("Non è stato possibile creare la seduta");
		}
	}

	public Docente aggiungiDocente() throws CreazioneDocenteException {
		try {
			return docenteDAO.create();
		} catch (DAOException e) {
			throw new CreazioneDocenteException("Non è stato possibile inserire il docente nel database");
		}
	}
	
	public SedutaDiLaurea composizioneCommissione() throws ComposizioneCommissioneException {
		ArrayList<SedutaDiLaurea> listaSedute = null;
		try {
			listaSedute = (ArrayList<SedutaDiLaurea>) sedutaDiLaureaDAO.readAll();
		} catch (DAOException e) {
			throw new ComposizioneCommissioneException("Non è stato possibile ottenere la lista delle sedute");
		}
		if(listaSedute.size() == 0) {
			throw new ComposizioneCommissioneException("Nessuna seduta disponibile");
		}
		// simulo una scelta
		SedutaDiLaurea seduta = listaSedute.get(0);
		try {
			seduta = sedutaDiLaureaDAO.read(seduta.getIdSedutaDiLaurea());
		} catch (DAOException e) {
			throw new ComposizioneCommissioneException("Non è stato possibile comporre la commissione");
		}
		return seduta;	
	}
	
}
