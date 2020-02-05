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
			
			DBManager.closeConnection();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
