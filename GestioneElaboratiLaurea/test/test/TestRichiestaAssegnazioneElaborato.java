package test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import control.AssegnazioneElaboratoException;
import control.GestioneElaborati;
import database.AssegnazioneDAO;
import database.DBManager;
import database.DocenteDAO;
import database.StudenteDAO;
import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;
import entity.Preferenza;
import entity.Richiesta;
import entity.Studente;
import main.DataDefinition;

public class TestRichiestaAssegnazioneElaborato {

	InputStream sysInBackup;
	GestioneElaborati gestioneElaborati = new GestioneElaborati();
	StudenteDAO studenteDAO = new StudenteDAO();
	DocenteDAO docenteDAO = new DocenteDAO();
	AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataDefinition.createTables();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DataDefinition.dropTables();
	}

	@Before
	public void setUp() throws Exception {
		sysInBackup = System.in;
	}

	@After
	public void tearDown() throws Exception {
		System.setIn(sysInBackup);
		Connection conn = DBManager.getConnection();
		String cleanDocente = "DELETE FROM DOCENTE";
		String cleanStudente = "DELETE FROM STUDENTE";
		Statement stmt = conn.createStatement();
		stmt.execute(cleanDocente);
		stmt.execute(cleanStudente);
		// mediante i cascade in questo modo elimino tutte le entità
	}
	
	@Test
	public void test_1() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			Preferenza preferenza = new Preferenza();
			preferenza.setRichiesta(richiesta);
			preferenza.setElaborato(elaborato);
			listaPreferenze.add(preferenza);
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(elaborato);
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_2() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Reti di calcolatori");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 3; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\nR\nA\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(2));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_3() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente_1 = docenteDAO.create();
			Docente docente_2 = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_1);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_1);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_2);
			elaborato.setInsegnamento("Reti di calcolatori");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 3; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\nR\nA\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(2));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_4() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			for(int i=0; i<10; i++) {
				elaborato = new Elaborato();
				elaborato.setDocente(docente);
				elaborato.setInsegnamento("");
				elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
				// listaElaborati.add(elaborato);
				Richiesta req = new Richiesta();
				Studente stud = new Studente();
				stud.setCFU(180);
				stud = studenteDAO.create(stud);
				req.setStudente(stud);
				req.setListaPreferenze(new ArrayList<Preferenza>());
				gestioneElaborati.richiestaAssegnazioneElaborato(req);
			}
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 1; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(1));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_5() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 1; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(1));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_6() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Reti di calcolatori");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Elettronica generale");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 3; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\nR\nR\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(3));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_7() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente_1 = docenteDAO.create();
			Docente docente_2 = docenteDAO.create();
			Docente docente_3 = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_1);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_1);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_2);
			elaborato.setInsegnamento("Reti di calcolatori");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente_3);
			elaborato.setInsegnamento("Elettronica generale");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 2; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\nR\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(2));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_8() {
		try {
			Studente studente = new Studente();
			studente.setCFU(170);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			Preferenza preferenza = new Preferenza();
			preferenza.setRichiesta(richiesta);
			preferenza.setElaborato(elaborato);
			listaPreferenze.add(preferenza);
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
		} catch(AssegnazioneElaboratoException e) {
			assertEquals(e.getMessage(), "Non è stato possibile assegnare un elaborato: CFU insufficienti");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_9() {
		try {
			Studente studente = new Studente();
			studente.setCFU(177);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			Preferenza preferenza = new Preferenza();
			preferenza.setRichiesta(richiesta);
			preferenza.setElaborato(elaborato);
			listaPreferenze.add(preferenza);
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(elaborato);
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_10() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Sistemi operativi");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Reti di calcolatori");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 3; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "R\nR\nR\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(2));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch(AssegnazioneElaboratoException e) {
			assertEquals(e.getMessage(), "Non è stato possibile assegnare un elaborato: nessun elaborato rimanente");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_11() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			richiesta.setListaPreferenze(listaPreferenze);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(elaborato);
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_12() {
		try {
			Studente studente = new Studente();
			studente.setCFU(180);
			studente = studenteDAO.create(studente);
			
			Docente docente = docenteDAO.create();
			
			Elaborato elaborato;
			ArrayList<Elaborato> listaElaborati = new ArrayList<Elaborato>();
			
			for(int i=0; i<9; i++) {
				elaborato = new Elaborato();
				elaborato.setDocente(docente);
				elaborato.setInsegnamento("");
				elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
				
				Richiesta req = new Richiesta();
				Studente stud = new Studente();
				stud.setCFU(180);
				stud = studenteDAO.create(stud);
				req.setStudente(stud);
				req.setListaPreferenze(new ArrayList<Preferenza>());
				gestioneElaborati.richiestaAssegnazioneElaborato(req);
			}
			
			elaborato = new Elaborato();
			elaborato.setDocente(docente);
			elaborato.setInsegnamento("Ingegneria del software");
			elaborato = gestioneElaborati.inserisciNuovoElaborato(elaborato);
			listaElaborati.add(elaborato);
			
			Richiesta richiesta = new Richiesta();
			richiesta.setStudente(studente);
			ArrayList<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
			
			for(int i = 0; i < 1; i++) {
				Preferenza preferenza = new Preferenza();
				preferenza.setRichiesta(richiesta);
				preferenza.setElaborato(listaElaborati.get(i));
				listaPreferenze.add(preferenza);
			}
			richiesta.setListaPreferenze(listaPreferenze);
			
			String inputString = "A\n";
			InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
			System.setIn(testInput);
			
			Assegnazione assegnazione = gestioneElaborati.richiestaAssegnazioneElaborato(richiesta);
			
			Assegnazione expected = new Assegnazione(listaElaborati.get(0));
			expected.setStudente(studente);
			expected.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			
			assertTrue(
				assegnazione.getIdElaborato() == expected.getIdElaborato() && 
				assegnazione.getStudente().getIdStudente() == expected.getStudente().getIdStudente() &&
				assegnazione.getDataAssegnazione().equals(expected.getDataAssegnazione())
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
