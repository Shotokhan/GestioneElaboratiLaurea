package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Preferenza;
import entity.Richiesta;
import enumeration.StatoRichiesta;

public class PreferenzaDAO {
	
	public Preferenza create(Preferenza preferenza, int priorita) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO PREFERENZA (STATOPREFERENZA, PRIORITA, "
					+ "RICHIESTA, ELABORATO) VALUES (?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, preferenza.getStato().toString());
			stmt.setInt(2, priorita);
			stmt.setInt(3, preferenza.getRichiesta().getIdRichiesta());
			stmt.setInt(4, preferenza.getElaborato().getIdElaborato());
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				preferenza.setIdPreferenza(result.getInt("IDPREFERENZA"));
			}
			return preferenza;
		} catch (SQLException e) {
			throw new DAOException("Creazione preferenza non riuscita");
		}
	}

	public Preferenza read(int idPreferenza) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		RichiestaDAO richiestaDAO = new RichiestaDAO();
		Preferenza preferenza = new Preferenza();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM PREFERENZA WHERE IDPREFERENZA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idPreferenza);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				preferenza.setIdPreferenza(idPreferenza);
				preferenza.setStato(StatoRichiesta.valueOf(result.getString("STATOPREFERENZA")));
				preferenza.setElaborato(elaboratoDAO.read(result.getInt("ELABORATO")));
				preferenza.setRichiesta(richiestaDAO.read(result.getInt("RICHIESTA")));
			} else {
				throw new DAOException("Preferenza non trovata: id non valido");
			}
			return preferenza;
		} catch (SQLException e) {
			throw new DAOException("Lettura preferenza non riuscita");
		}
	}
	
	public List<Preferenza> read(Richiesta richiesta) throws DAOException {
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		List<Preferenza> listaPreferenze = new ArrayList<Preferenza>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM PREFERENZA WHERE RICHIESTA = ? "
					+ "ORDER BY PRIORITA DESC;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, richiesta.getIdRichiesta());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Preferenza preferenza = new Preferenza();
				preferenza.setIdPreferenza(result.getInt("IDPREFERENZA"));
				preferenza.setStato(StatoRichiesta.valueOf(result.getString("STATOPREFERENZA")));
				preferenza.setElaborato(elaboratoDAO.read(result.getInt("ELABORATO")));
				preferenza.setRichiesta(richiesta);
				listaPreferenze.add(preferenza);
			}
			return listaPreferenze;
		} catch (SQLException e) {
			throw new DAOException("Lettura preferenza non riuscita");
		}
	}
	
	public Preferenza update(StatoRichiesta nuovoStato, Preferenza preferenza) throws DAOException{
		try {
			Connection conn = DBManager.getConnection();
			String query = "UPDATE PREFERENZA SET STATOPREFERENZA = ? "
					+ "WHERE IDPREFERENZA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nuovoStato.toString());
			stmt.setInt(2, preferenza.getIdPreferenza());
			int affected = stmt.executeUpdate();
			if(affected == 0) {
				throw new DAOException("Aggiornamento stato preferenza non riuscito: "
						+ "id non corretto");
			}
			return preferenza;
		} catch (SQLException e) {
			throw new DAOException("Aggiornamento stato preferenza non riuscito");
		}
	}
	
	public boolean delete(int idPreferenza) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM PREFERENZA WHERE IDPREFERENZA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idPreferenza);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			throw new DAOException("Eliminazione preferenza non riuscita");
		}
	}
	
}
