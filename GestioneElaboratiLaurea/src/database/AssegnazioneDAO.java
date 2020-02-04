package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;
import entity.Studente;
import enumeration.StatoElaborato;
import enumeration.StatoStudente;

public class AssegnazioneDAO {
	
	public Assegnazione create(Elaborato elaborato, Studente studente) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		StudenteDAO studenteDAO = new StudenteDAO();
		Assegnazione assegnazione = new Assegnazione(elaborato);
		assegnazione.setStudente(studente);
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			if(!elaboratoDAO.check(elaborato.getIdElaborato(), StatoElaborato.DISPONIBILE)) {
				throw new DAOException("Impossibile assegnare un elaborato non disponibile");
			}
			String query = "INSERT INTO ASSEGNAZIONE(ELABORATO, STUDENTE, DATAASSEGNAZIONE) "
					+ "VALUES(?, ?, CURRENT_TIMESTAMP);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, elaborato.getIdElaborato());
			stmt.setInt(2, studente.getIdStudente());
			// stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
			conn.setAutoCommit(false);
			// begin transaction
			stmt.executeUpdate();
			studenteDAO.update(StatoStudente.IN_COMPILAZIONE, studente);
			elaboratoDAO.update(StatoElaborato.ASSEGNATO, elaborato);
			// end transaction
			conn.commit();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				assegnazione.setIdAssegnazione(result.getInt("IDASSEGNAZIONE"));
			}
			return assegnazione;
		} catch (SQLException e) {
			throw new DAOException("Creazione assegnazione non riuscita");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new DAOException("Problemi con la connessione");
			}
		}
	}

	public List<Assegnazione> read(Docente docente) throws DAOException{
		// ritorna le assegnazioni relative all'anno corrente per un certo docente
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		StudenteDAO studenteDAO = new StudenteDAO();
		List<Assegnazione> listaAssegnazioni = new ArrayList<Assegnazione>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ASSEGNAZIONE WHERE "
					+ "EXTRACT(YEAR FROM DATAASSEGNAZIONE) = "
					+ "EXTRACT(YEAR FROM CURRENT_TIMESTAMP) AND ELABORATO IN "
					+ "(SELECT IDELABORATO FROM ELABORATO WHERE DOCENTE = ?);";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, docente.getIdDocente());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Assegnazione assegnazione = new Assegnazione(
						elaboratoDAO.read(result.getInt("ELABORATO"), docente));
				assegnazione.setIdAssegnazione(result.getInt("IDASSEGNAZIONE"));
				assegnazione.setDataAssegnazione(result.getDate("DATAASSEGNAZIONE"));
				assegnazione.setStudente(studenteDAO.read(result.getInt("STUDENTE"), assegnazione));
				listaAssegnazioni.add(assegnazione);
			}
			return listaAssegnazioni;
		} catch (SQLException e) {
			throw new DAOException("Lettura assegnazioni non riuscita");
		}
	}
	
	public Assegnazione read(Studente studente) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ASSEGNAZIONE WHERE STUDENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, studente.getIdStudente());
			ResultSet result = stmt.executeQuery();
			Assegnazione assegnazione = null;
			if(result.next()) {
				assegnazione = new Assegnazione(
						elaboratoDAO.read(result.getInt("ELABORATO")));
				assegnazione.setIdAssegnazione(result.getInt("IDASSEGNAZIONE"));
				assegnazione.setDataAssegnazione(result.getDate("DATAASSEGNAZIONE"));
				assegnazione.setStudente(studente);
			}
			return assegnazione;
		} catch (SQLException e) {
			throw new DAOException("Lettura assegnazione non riuscita");
		}
	}
	
	public void update() {};
	
	public boolean delete(int idAssegnazione) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM ASSEGNAZIONE WHERE IDASSEGNAZIONE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idAssegnazione);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			throw new DAOException("Eliminazione assegnazione non riuscita");
		}
	}
	
}
