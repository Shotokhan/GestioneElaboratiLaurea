package main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import control.GestioneElaborati;
import database.DBManager;
import database.DocenteDAO;
import database.StudenteDAO;
import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;
import entity.Preferenza;
import entity.Richiesta;
import entity.Studente;

public class Main {

	public static void main(String[] args) {
		DataDefinition.dropTables();
		DataDefinition.createTables();
		GestioneElaborati gestioneElaborati = new GestioneElaborati();
		StudenteDAO studenteDAO = new StudenteDAO();
		DocenteDAO docenteDAO = new DocenteDAO();
		
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			
			ArrayList<Elaborato> listaDisponibili = (ArrayList<Elaborato>) gestioneElaborati.ottieniListaElaboratiDisponibili();
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			Preferenza preferenza = new Preferenza();
			preferenza.setRichiesta(richiesta);
			preferenza.setElaborato(listaDisponibili.get(0));
			listaPreferenze.add(preferenza);
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			System.out.println(assegnazione.getIdElaborato() == elaborato.getIdElaborato());
			System.out.println(assegnazione.getStudente().getIdStudente() == studente.getIdStudente());
			System.out.println(assegnazione.getDataAssegnazione());
			
			for(int i=0; i<10; i++) {
				Richiesta req = new Richiesta();
				Studente stud = new Studente();
				stud.setCFU(180);
				stud = studenteDAO.create(stud);
				req.setStudente(stud);
				req.setListaPreferenze(new ArrayList<Preferenza>());
				try {
					gestioneElaborati.richiestaAssegnazioneElaborato(req);
				} catch(Exception e) {
					System.out.println("Nuovo studente in attesa di servizio: " + e.getMessage());
				}
			}
			
			for(int i=0; i<10; i++) {
				elaborato = new Elaborato();
				elaborato.setDocente(docente);
				elaborato.setInsegnamento("");
				elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			}
			gestioneElaborati.serviRichiesteInAttesa();
			System.out.println("Le richieste in attesa sono state servite");
			
			DBManager.closeConnection();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
