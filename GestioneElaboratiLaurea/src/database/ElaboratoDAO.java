package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Docente;
import entity.Elaborato;
import enumeration.StatoElaborato;

public class ElaboratoDAO {
	
	public Elaborato create(Elaborato elaborato) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO ELABORATO(INSEGNAMENTO, DOCENTE, STATOELABORATO) "
					+ "VALUES(?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, elaborato.getInsegnamento());
			stmt.setInt(2, elaborato.getDocente().getIdDocente());
			stmt.setString(3, StatoElaborato.DISPONIBILE.toString());
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				elaborato.setIdElaborato(result.getInt("IDELABORATO"));
			}
			return elaborato;
		} catch (SQLException e) {
			throw new DAOException("Creazione elaborato non riuscita");
		}
	}

	public Elaborato read(int idElaborato) throws DAOException {
		DocenteDAO docenteDAO = new DocenteDAO();
		Elaborato elaborato = new Elaborato();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ELABORATO WHERE IDELABORATO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idElaborato);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				elaborato.setIdElaborato(result.getInt("IDELABORATO"));
				elaborato.setDocente(docenteDAO.read(result.getInt("DOCENTE")));
				elaborato.setInsegnamento(result.getString("INSEGNAMENTO"));
			} else {
				throw new DAOException("Elaborato non trovato: id non valido");
			}
			return elaborato;
		} catch (SQLException e) {
			throw new DAOException("Lettura elaborato non riuscita");
		}
	}
	
	public Elaborato read(int idElaborato, Docente docente) throws DAOException {
		Elaborato elaborato = new Elaborato();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ELABORATO WHERE IDELABORATO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idElaborato);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				elaborato.setIdElaborato(result.getInt("IDELABORATO"));
				elaborato.setDocente(docente);
				elaborato.setInsegnamento(result.getString("INSEGNAMENTO"));
			} else {
				throw new DAOException("Elaborato non trovato: id non valido");
			}
			return elaborato;
		} catch (SQLException e) {
			throw new DAOException("Lettura elaborato non riuscita");
		}
	}
	
	public List<Elaborato> read(Docente docente) throws DAOException {
		List<Elaborato> listaElaborati = new ArrayList<Elaborato>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ELABORATO WHERE DOCENTE = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, docente.getIdDocente());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Elaborato elaborato = new Elaborato();
				elaborato.setIdElaborato(result.getInt("IDELABORATO"));
				elaborato.setDocente(docente);
				elaborato.setInsegnamento(result.getString("INSEGNAMENTO"));
				listaElaborati.add(elaborato);
			}
			return listaElaborati;
		} catch (SQLException e) {
			throw new DAOException("Lettura elaborati non riuscita");
		}
	}
	
	public List<Elaborato> read(StatoElaborato stato) throws DAOException {
		DocenteDAO docenteDAO = new DocenteDAO();
		List<Elaborato> listaElaborati = new ArrayList<Elaborato>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ELABORATO WHERE STATOELABORATO = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, stato.toString());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Elaborato elaborato = new Elaborato();
				elaborato.setIdElaborato(result.getInt("IDELABORATO"));
				elaborato.setDocente(docenteDAO.read(result.getInt("DOCENTE")));
				elaborato.setInsegnamento(result.getString("INSEGNAMENTO"));
				listaElaborati.add(elaborato);
			}
			return listaElaborati;
		} catch (SQLException e) {
			throw new DAOException("Lettura elaborati non riuscita");
		}
	}
	
	public Elaborato update(StatoElaborato nuovoStato, Elaborato elaborato) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "UPDATE ELABORATO SET STATOELABORATO = ? "
					+ "WHERE IDELABORATO = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nuovoStato.toString());
			stmt.setInt(2, elaborato.getIdElaborato());
			int affected = stmt.executeUpdate();
			if(affected == 0) {
				throw new DAOException("Aggiornamento elaborato non riuscito: "
						+ "id non corretto");
			}
			return elaborato;
		} catch (SQLException e) {
			throw new DAOException("Aggiornamento elaborato non riuscito");
		}
	}
	
	public boolean delete(int idElaborato) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM ELABORATO WHERE IDELABORATO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idElaborato);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			throw new DAOException("Eliminazione elaborato non riuscita");
		}
	}
	
	public boolean check(int idElaborato, StatoElaborato stato) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM ELABORATO WHERE IDELABORATO = ? "
					+ "AND STATOELABORATO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idElaborato);
			stmt.setString(2, stato.toString());
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Controllo stato elaborato non riuscito");
		}
	}
	
}
