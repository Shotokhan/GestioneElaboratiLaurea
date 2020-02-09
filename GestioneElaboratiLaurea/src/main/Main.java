package main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import control.GestioneElaborati;
import control.GestioneSedute;
import database.AssegnazioneDAO;
import database.CaricamentoDAO;
import database.DBManager;
import database.StudenteDAO;
import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;
import entity.Preferenza;
import entity.Richiesta;
import entity.SedutaDiLaurea;
import entity.Studente;

public class Main {

	public static void main(String[] args) {
		DataDefinition.dropTables();
		DataDefinition.createTables();
		GestioneElaborati gestioneElaborati = new GestioneElaborati();
		GestioneSedute gestioneSedute = new GestioneSedute();
		StudenteDAO studenteDAO = new StudenteDAO();
		AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
		CaricamentoDAO caricamentoDAO = new CaricamentoDAO();
		
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = gestioneSedute.aggiungiDocente();
			
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
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(2020, 6, 20);
			
			SedutaDiLaurea seduta = gestioneSedute.aggiungiSeduta(calendar.getTime());
			
			ArrayList<Assegnazione> listaAssegnazioni = (ArrayList<Assegnazione>) assegnazioneDAO.read(docente);
			for(Assegnazione assign : listaAssegnazioni) {
				caricamentoDAO.create(assign, seduta);
			}
			
			seduta = gestioneSedute.composizioneCommissione();
			
			DBManager.closeConnection();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
