package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Preferenza;
import entity.Richiesta;
import enumeration.StatoRichiesta;

public class RichiestaDAO {
	
	public Richiesta create(Richiesta richiesta) throws DAOException {
		PreferenzaDAO preferenzaDAO = new PreferenzaDAO();
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO RICHIESTA(STATORICHIESTA, STUDENTE) "
					+ "VALUES(?, ?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, richiesta.getStatoRichiesta().toString());
			stmt.setInt(2, richiesta.getStudente().getIdStudente());
			stmt.executeUpdate();
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
		} catch (SQLException e) {
			throw new DAOException("Creazione richiesta non riuscita");
		}
		return richiesta;
	}
	
	public Richiesta read(int idRichiesta) throws DAOException {
		// TODO : questa funzione deve settare i campi listaPreferenze e studente
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
			}
			return richiesta;
		} catch (SQLException e) {
			throw new DAOException("Lettura richiesta non riuscita");
		}
	}
	
	public Richiesta update(StatoRichiesta nuovoStato, Richiesta richiesta) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "UPDATE RICHIESTA SET STATORICHIESTA = ? "
					+ "WHERE IDRICHIESTA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, nuovoStato.toString());
			stmt.setInt(2, richiesta.getIdRichiesta());
			int affected = stmt.executeUpdate();
			if(affected == 0) {
				throw new DAOException("Aggiornamento stato richiesta non riuscito: "
						+ "id non corretto");
			}
			return richiesta;
		} catch (SQLException e) {
			throw new DAOException("Aggiornamento stato richiesta non riuscito");
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
