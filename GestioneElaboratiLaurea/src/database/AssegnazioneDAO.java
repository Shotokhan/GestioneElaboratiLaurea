package database;

import java.util.List;

import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;
import entity.Studente;

public class AssegnazioneDAO {
	// TODO
	private ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
	private StudenteDAO studenteDAO = new StudenteDAO();
	
	public Assegnazione create(Elaborato elaborato, Studente studente) {
		return null;
	}

	public List<Assegnazione> read(Docente docente){
		return null;
	}
	
	public void update() {};
	
	public boolean delete(int idAssegnazione) {
		return false;
	}
	
}
