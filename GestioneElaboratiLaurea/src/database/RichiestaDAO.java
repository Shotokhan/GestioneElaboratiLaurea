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
import enumeration.StatoStudente;

public class RichiestaDAO {
	
	public Richiesta create(Richiesta richiesta) throws DAOException {
		PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
		StudenteDAO studenteDAO = new StudenteDAO();
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			String query = "INSERT INTO RICHIESTA(STATORICHIESTA, STUDENTE) "
					+ "VALUES(?, ?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			if(studenteDAO.read(richiesta.getStudente().getIdStudente(), StatoStudente.IN_ATTESA)) {
				throw new DAOException("Creazione richiesta non riuscita: è già esistente "
						+ "una richiesta in attesa di essere servita per questo studente");
			}
			stmt.setString(1, richiesta.getStatoRichiesta().toString());
			stmt.setInt(2, richiesta.getStudente().getIdStudente());
			conn.setAutoCommit(false);
			// begin transaction
			stmt.executeUpdate();
			studenteDAO.update(StatoStudente.IN_ATTESA, richiesta.getStudente());
			// end transaction
			conn.commit();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				richiesta.setIdRichiesta(result.getInt("IDRICHIESTA"));
				ArrayList<Preferenza> listaPreferenzeConID = new ArrayList<Preferenza>();
				for(int i = 0; i < richiesta.getListaPreferenze().size(); i++) {
					Preferenza preferenza = richiesta.getListaPreferenze().get(i);
					preferenza = preferenzaDAO.create(preferenza, 3-i);
					listaPreferenzeConID.add(preferenza);
				}
				richiesta.setListaPreferenze(listaPreferenzeConID);
			}
			return richiesta;
		} catch (SQLException e) {
			throw new DAOException("Creazione richiesta non riuscita");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new DAOException("Problemi con la connessione");
			}
		}
		
	}
	
	public Richiesta read(int idRichiesta) throws DAOException {
		StudenteDAO studenteDAO = new StudenteDAO();
		PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
		Richiesta richiesta = new Richiesta();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM RICHIESTA WHERE IDRICHIESTA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idRichiesta);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				richiesta.setIdRichiesta(idRichiesta);
				richiesta.setStatoRichiesta(StatoRichiesta.valueOf(result.getString("STATORICHIESTA")));
				richiesta.setStudente(studenteDAO.read(result.getInt("STUDENTE")));
				richiesta.setListaPreferenze((ArrayList<Preferenza>) preferenzaDAO.read(richiesta));
			} else {
				throw new DAOException("Richiesta non trovata: id non valido");
			}
			return richiesta;
		} catch (SQLException e) {
			throw new DAOException("Lettura richiesta non riuscita");
		}
	}
	
	public List<Richiesta> read(StatoRichiesta stato) throws DAOException {
		StudenteDAO studenteDAO = new StudenteDAO();
		PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
		List<Richiesta> listaRichieste = new ArrayList<Richiesta>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM RICHIESTA WHERE STATORICHIESTA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, stato.toString());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Richiesta richiesta = new Richiesta();
				richiesta.setIdRichiesta(result.getInt("IDRICHIESTA"));
				richiesta.setStatoRichiesta(StatoRichiesta.valueOf(result.getString("STATORICHIESTA")));
				richiesta.setStudente(studenteDAO.read(result.getInt("STUDENTE")));
				richiesta.setListaPreferenze((ArrayList<Preferenza>) preferenzaDAO.read(richiesta));
				listaRichieste.add(richiesta);
			}
			return listaRichieste;
		} catch (SQLException e) {
			throw new DAOException("Lettura richieste non riuscita");
		}
	}
	
	public Richiesta update(StatoRichiesta nuovoStato, Richiesta richiesta) throws DAOException {
		PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
		StudenteDAO studenteDAO = new StudenteDAO();
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			String query = "UPDATE RICHIESTA SET STATORICHIESTA = ? "
					+ "WHERE IDRICHIESTA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nuovoStato.toString());
			stmt.setInt(2, richiesta.getIdRichiesta());
			conn.setAutoCommit(false);
			// begin transaction
			int affected = stmt.executeUpdate();
			if(affected == 0) {
				throw new DAOException("Aggiornamento stato richiesta non riuscito: "
						+ "id non corretto");
			}
			if(nuovoStato.equals(StatoRichiesta.RESPINTA)) {
				for(Preferenza preferenza : richiesta.getListaPreferenze()) {
					preferenzaDAO.update(StatoRichiesta.RESPINTA, preferenza);
				}
				studenteDAO.update(StatoStudente.IN_RICERCA, richiesta.getStudente());
			}
			// end transaction
			conn.commit();
			return richiesta;
		} catch (SQLException e) {
			throw new DAOException("Aggiornamento stato richiesta non riuscito");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new DAOException("Problemi con la connessione");
			}
		}
	}
	
	public boolean delete(int idRichiesta) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM RICHIESTA WHERE IDRICHIESTA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idRichiesta);
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			throw new DAOException("Eliminazione richiesta non riuscita");
		}
	}
}
