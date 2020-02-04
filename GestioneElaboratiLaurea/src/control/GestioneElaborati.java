package control;

import java.util.List;

import database.AssegnazioneDAO;
import database.ElaboratoDAO;
import database.PreferenzaDAO;
import database.RichiestaDAO;
import database.StudenteDAO;
import entity.Assegnazione;
import entity.Elaborato;
import entity.Preferenza;
import entity.Richiesta;

public class GestioneElaborati {

	private RichiestaDAO richiestaDAO = new RichiestaDAO();
	private AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
	private PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
	private ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
	private StudenteDAO studenteDAO = new StudenteDAO();
	
	public List<Elaborato> ottieniListaElaboratiDisponibili(){
		// TODO
		return null;
	}
	
	public Assegnazione richiestaAssegnazioneElaborato(Richiesta richiesta) {
		// TODO
		return null;
	}
	
	public boolean stubEmail(Preferenza preferenza) {
		// TODO
		return true;
	}
	
	public void accettaRichiesta(Preferenza preferenza) {
		// TODO
	}
	
	public void rifiutaRichiesta(Preferenza preferenza) {
		// TODO
	}
	
	public Elaborato inserisciNuovoElaborato(Elaborato elaborato) {
		// TODO
		return null;
	}
}
