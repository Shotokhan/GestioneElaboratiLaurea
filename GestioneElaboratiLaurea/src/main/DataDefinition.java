package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DBManager;

public class DataDefinition {
	
	public static void createTables() {
		try {
			Connection conn = DBManager.getConnection();
			ArrayList<String> createTableStatements = new ArrayList<String>();
			String docente = "CREATE TABLE DOCENTE("
					+ "IDDOCENTE INT AUTO_INCREMENT PRIMARY KEY);";
			createTableStatements.add(docente);
			String studente = "CREATE TABLE STUDENTE("
					+ "IDSTUDENTE INT AUTO_INCREMENT PRIMARY KEY, "
					+ "CFU INT NOT NULL, "
					+ "STATOSTUDENTE VARCHAR(16) NOT NULL);";
			createTableStatements.add(studente);
			String sedutaDiLaurea = "CREATE TABLE SEDUTADILAUREA("
					+ "IDSEDUTADILAUREA INT AUTO_INCREMENT PRIMARY KEY, "
					+ "SCADENZACARICAMENTOELABORATI DATE NOT NULL);";
			createTableStatements.add(sedutaDiLaurea);
			String elaborato = "CREATE TABLE ELABORATO("
					+ "IDELABORATO INT AUTO_INCREMENT PRIMARY KEY, "
					+ "INSEGNAMENTO VARCHAR(50) NOT NULL, "
					+ "DOCENTE INT NOT NULL, "
					+ "STATOELABORATO VARCHAR(14) NOT NULL, "
					+ "FOREIGN KEY (DOCENTE) REFERENCES DOCENTE "
					+ "ON DELETE CASCADE);";
			createTableStatements.add(elaborato);
			String richiesta = "CREATE TABLE RICHIESTA("
					+ "IDRICHIESTA INT AUTO_INCREMENT PRIMARY KEY, "
					+ "STATORICHIESTA VARCHAR(9) NOT NULL, "
					+ "STUDENTE INT NOT NULL, "
					+ "FOREIGN KEY (STUDENTE) REFERENCES STUDENTE "
					+ "ON DELETE CASCADE);";
			createTableStatements.add(richiesta);
			String preferenza = "CREATE TABLE PREFERENZA("
					+ "IDPREFERENZA INT AUTO_INCREMENT PRIMARY KEY, "
					+ "STATOPREFERENZA VARCHAR(9) NOT NULL, "
					+ "PRIORITA INT NOT NULL, "
					+ "RICHIESTA INT NOT NULL, "
					+ "ELABORATO INT NOT NULL, "
					+ "FOREIGN KEY (RICHIESTA) REFERENCES RICHIESTA "
					+ "ON DELETE CASCADE, "
					+ "FOREIGN KEY (ELABORATO) REFERENCES ELABORATO "
					+ "ON DELETE CASCADE);";
			createTableStatements.add(preferenza);
			String assegnazione = "CREATE TABLE ASSEGNAZIONE("
					+ "IDASSEGNAZIONE INT AUTO_INCREMENT PRIMARY KEY, "
					+ "ELABORATO INT NOT NULL, "
					+ "STUDENTE INT NOT NULL, "
					+ "DATAASSEGNAZIONE DATE NOT NULL, "
					+ "FOREIGN KEY (ELABORATO) REFERENCES ELABORATO "
					+ "ON DELETE CASCADE, "
					+ "FOREIGN KEY (STUDENTE) REFERENCES STUDENTE "
					+ "ON DELETE CASCADE);";
			createTableStatements.add(assegnazione);
			String caricamento = "CREATE TABLE CARICAMENTO("
					+ "IDCARICAMENTO INT AUTO_INCREMENT PRIMARY KEY, "
					+ "ASSEGNAZIONE INT NOT NULL, "
					+ "SEDUTADILAUREA INT, "
					+ "DATACARICAMENTO DATE NOT NULL, "
					+ "FOREIGN KEY (ASSEGNAZIONE) REFERENCES ASSEGNAZIONE "
					+ "ON DELETE CASCADE, "
					+ "FOREIGN KEY (SEDUTADILAUREA) REFERENCES SEDUTADILAUREA "
					+ "ON DELETE SET NULL);";
			createTableStatements.add(caricamento);
			Statement stmt = conn.createStatement();
			for(String definition : createTableStatements) {
				System.out.println(definition);
				stmt.executeUpdate(definition);
			}
		} catch (SQLException e) {
			System.out.println("Errore creazione tabelle");
			dropTables();
		}
	}
	
	public static void dropTables() {
		try {
			Connection conn = DBManager.getConnection();
			ArrayList<String> dropTableStatements = new ArrayList<String>();
			String caricamento = "DROP TABLE IF EXISTS CARICAMENTO;";
			dropTableStatements.add(caricamento);
			String assegnazione = "DROP TABLE IF EXISTS ASSEGNAZIONE;";
			dropTableStatements.add(assegnazione);
			String preferenza = "DROP TABLE IF EXISTS PREFERENZA;";
			dropTableStatements.add(preferenza);
			String richiesta = "DROP TABLE IF EXISTS RICHIESTA;";
			dropTableStatements.add(richiesta);
			String elaborato = "DROP TABLE IF EXISTS ELABORATO;";
			dropTableStatements.add(elaborato);
			String sedutaDiLaurea = "DROP TABLE IF EXISTS SEDUTADILAUREA;";
			dropTableStatements.add(sedutaDiLaurea);
			String studente = "DROP TABLE IF EXISTS STUDENTE;";
			dropTableStatements.add(studente);
			String docente = "DROP TABLE IF EXISTS DOCENTE;";
			dropTableStatements.add(docente);	
			Statement stmt = conn.createStatement();
			for(String definition : dropTableStatements) {
				System.out.println(definition);
				stmt.executeUpdate(definition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
