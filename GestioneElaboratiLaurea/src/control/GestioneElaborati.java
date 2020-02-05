package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import database.AssegnazioneDAO;
import database.DAOException;
import database.ElaboratoDAO;
import database.PreferenzaDAO;
import database.RichiestaDAO;
import entity.Assegnazione;
import entity.Elaborato;
import entity.Preferenza;
import entity.Richiesta;
import enumeration.StatoElaborato;
import enumeration.StatoRichiesta;

public class GestioneElaborati {

	private static final int numCFU = 177;
	
	private RichiestaDAO richiestaDAO = new RichiestaDAO();
	private AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
	private PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
	private ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
	
	public List<Elaborato> ottieniListaElaboratiDisponibili() throws ListaElaboratiException{
		try {
			return elaboratoDAO.read(StatoElaborato.DISPONIBILE);
		} catch (DAOException e) {
			throw new ListaElaboratiException(e);
		}
	}
	
	public Assegnazione richiestaAssegnazioneElaborato(Richiesta richiesta) throws AssegnazioneElaboratoException {
		try {
			richiesta = richiestaDAO.create(richiesta);
		} catch (DAOException e) {
			throw new AssegnazioneElaboratoException("Non è stato possibile inoltrare la richiesta");
		}
		return servizioAssegnazioneElaborato(richiesta);
	}
	
	private Assegnazione servizioAssegnazioneElaborato(Richiesta richiesta) throws AssegnazioneElaboratoException {
		ArrayList<Elaborato> listaElaboratiPreferenze = new ArrayList<Elaborato>();
		try {
			if(richiesta.getStudente().getCFU() < numCFU) {
				richiestaDAO.update(StatoRichiesta.RESPINTA, richiesta);
				richiesta.setStatoRichiesta(StatoRichiesta.RESPINTA);
				for(Preferenza preferenza : richiesta.getListaPreferenze()) {
					preferenza.setStato(StatoRichiesta.RESPINTA);
				}
				throw new AssegnazioneElaboratoException("Non è stato possibile assegnare un elaborato: CFU insufficienti");
			}
			
			Scanner stubLink = new Scanner(System.in);
			for(Preferenza preferenza : richiesta.getListaPreferenze()) {
				listaElaboratiPreferenze.add(preferenza.getElaborato());
				if(preferenza.getStato().equals(StatoRichiesta.RESPINTA)) {
					// nothing to do - continue cycle
				} else if(assegnazioneDAO.read(preferenza.getElaborato().getDocente()).size() >= 10) {
					preferenzaDAO.update(StatoRichiesta.RESPINTA, preferenza);
					preferenza.setStato(StatoRichiesta.RESPINTA);
				} else {
					try {
						if(stubEmail(preferenza, stubLink)) {
							richiestaDAO.update(StatoRichiesta.ACCOLTA, richiesta);
							richiesta.setStatoRichiesta(StatoRichiesta.ACCOLTA);
							stubLink.close();
							return assegnazioneDAO.create(preferenza.getElaborato(), richiesta.getStudente());
						}
					} catch (IOException e) {
						System.err.println("Problema nella comunicazione con il docente");
						e.printStackTrace();
					} catch (RispostaDocenteException e) {
						System.err.println("Problema aggiornamento base di dati");
						e.printStackTrace();
					}
				}
			}
			
			ArrayList<Elaborato> listaDisponibili = null;
			try {
				listaDisponibili = (ArrayList<Elaborato>) ottieniListaElaboratiDisponibili();
			} catch (ListaElaboratiException cause) {
				throw new AssegnazioneElaboratoException("Non è stato possibile assegnare un elaborato", cause);
			}
			for(Elaborato elaborato : listaDisponibili) {
				if(!listaElaboratiPreferenze.contains(elaborato)) {
					richiestaDAO.update(StatoRichiesta.ACCOLTA, richiesta);
					richiesta.setStatoRichiesta(StatoRichiesta.ACCOLTA);
					return assegnazioneDAO.create(elaborato, richiesta.getStudente());
				}
			}
			throw new AssegnazioneElaboratoException("Non è stato possibile assegnare un elaborato: nessun elaborato rimanente");
			
		} catch (DAOException cause) {
			throw new AssegnazioneElaboratoException("Non è stato possibile assegnare "
					+ "un elaborato", cause);
		}
	}
	
	public void serviRichiesteInAttesa() {
		try {
			ArrayList<Richiesta> listaRichiesteInAttesa = (ArrayList<Richiesta>) richiestaDAO.read(StatoRichiesta.IN_ATTESA);
			for(Richiesta richiesta : listaRichiesteInAttesa) {
				try {
					servizioAssegnazioneElaborato(richiesta);
				} catch (AssegnazioneElaboratoException e) {
					// TODO Blocco catch generato automaticamente
					e.printStackTrace();
				}
			}
		} catch (DAOException e) {
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
	}
	
	public boolean stubEmail(Preferenza preferenza, Scanner stubLink) throws IOException, RispostaDocenteException {
		if(stubLink.nextLine().charAt(0) == 'A') {
			accettaRichiesta(preferenza);
			return true;
		} else {
			rifiutaRichiesta(preferenza);
			return false;
		}
	}
	
	public void accettaRichiesta(Preferenza preferenza) throws RispostaDocenteException {
		try {
			preferenzaDAO.update(StatoRichiesta.ACCOLTA, preferenza);
			preferenza.setStato(StatoRichiesta.ACCOLTA);
		} catch (DAOException cause) {
			throw new RispostaDocenteException("Non è stato possibile inviare la risposta", cause);
		}
	}
	
	public void rifiutaRichiesta(Preferenza preferenza) throws RispostaDocenteException {
		try {
			preferenzaDAO.update(StatoRichiesta.RESPINTA, preferenza);
			preferenza.setStato(StatoRichiesta.RESPINTA);
		} catch (DAOException cause) {
			throw new RispostaDocenteException("Non è stato possibile inviare la risposta", cause);
		}
	}
	
	public Elaborato inserisciNuovoElaborato(Elaborato elaborato) throws CreazioneElaboratoException {
		try {
			return elaboratoDAO.create(elaborato);
		} catch (DAOException cause) {
			throw new CreazioneElaboratoException("Non è stato possibile creare l'elaborato", cause);
		}
	}
}
