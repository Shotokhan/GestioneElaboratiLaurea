package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Assegnazione;
import entity.Docente;
import entity.Elaborato;

public class DocenteDAO {
	
	public Docente create() throws DAOException {
		Docente docente = new Docente();
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO DOCENTE VALUES();";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				docente.setIdDocente(result.getInt("IDDOCENTE"));
			}
			return docente;
		} catch (SQLException e) {
			throw new DAOException("Creazione docente non riuscita");
		}
	}
	
	public Docente read(int idDocente) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
		Docente docente = new Docente();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM DOCENTE WHERE IDDOCENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idDocente);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				docente.setIdDocente(result.getInt("IDDOCENTE"));
				docente.setListaElaborati((ArrayList<Elaborato>) elaboratoDAO.read(docente));
				docente.setListaAssegnazioni((ArrayList<Assegnazione>) assegnazioneDAO.read(docente));
			}
			return docente;
		} catch (SQLException e) {
			throw new DAOException("Lettura docente non riuscita");
		}
	}
	
	public Docente read(Elaborato elaborato) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
		Docente docente = new Docente();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM DOCENTE WHERE IDDOCENTE IN ("
					+ "SELECT DOCENTE FROM ELABORATO "
					+ "WHERE IDELABORATO = ?);";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, elaborato.getIdElaborato());
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				docente.setIdDocente(result.getInt("IDDOCENTE"));
				docente.setListaElaborati((ArrayList<Elaborato>) elaboratoDAO.read(docente));
				docente.setListaAssegnazioni((ArrayList<Assegnazione>) assegnazioneDAO.read(docente));
			}
			return docente;
		} catch (SQLException e) {
			throw new DAOException("Lettura docente non riuscita");
		}
	}
	
	public void update() {}

	public boolean delete(int idDocente) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM DOCENTE WHERE IDDOCENTE = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idDocente);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			throw new DAOException("Eliminazione docente non riuscita");
		}
	}
	
}
