package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Studente;
import enumeration.StatoStudente;

public class StudenteDAO {
	
	public Studente create(Studente studente) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO STUDENTE(CFU, STATOSTUDENTE) VALUES(?, ?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, studente.getCFU());
			stmt.setString(2, StatoStudente.IN_RICERCA.toString());
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				studente.setIdStudente(result.getInt("IDSTUDENTE"));
			}
			return studente;
		} catch (SQLException e) {
			throw new DAOException("Creazione studente non riuscita");
		}
	}
	
	public Studente read(int idStudente) throws DAOException {
		// TODO - deve riempire il campo "elaborato" con l'elaborato a cui lo studente
		// è eventualmente stato assegnato
		Studente studente = new Studente();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM STUDENTE WHERE IDSTUDENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idStudente);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				studente.setIdStudente(result.getInt("IDSTUDENTE"));
				studente.setCFU(result.getInt("CFU"));
			}
			return studente;
		} catch (SQLException e) {
			throw new DAOException("Lettura studente non riuscita");
		}
	}
	
	public Studente update(StatoStudente nuovoStato, Studente studente) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "UPDATE STUDENTE SET STATOSTUDENTE = ? WHERE IDSTUDENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nuovoStato.toString());
			stmt.setInt(2, studente.getIdStudente());
			int affected = stmt.executeUpdate();
			if(affected == 0) {
				throw new DAOException("Aggiornamento stato studente non riuscito: "
						+ "id non corretto");
			}
			return studente;
		} catch (SQLException e) {
			throw new DAOException("Aggiornamento stato studente non riuscito");
		}
	}
	
	public boolean delete(int idStudente) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM STUDENTE WHERE IDSTUDENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idStudente);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Eliminazione studente non riuscita");
		}
	}
}
