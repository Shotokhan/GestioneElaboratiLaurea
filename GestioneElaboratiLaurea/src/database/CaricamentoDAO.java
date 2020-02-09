package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Assegnazione;
import entity.Caricamento;
import entity.SedutaDiLaurea;
import enumeration.StatoElaborato;
import enumeration.StatoStudente;

public class CaricamentoDAO {

	public Caricamento create(Assegnazione assegnazione, SedutaDiLaurea seduta) throws DAOException {
		Connection conn = null;
		StudenteDAO studenteDAO = new StudenteDAO();
		ElaboratoDAO elaboratoDAO = new ElaboratoDAO();
		Caricamento caricamento = new Caricamento(assegnazione);
		caricamento.setSedutaDiLaurea(seduta);
		if(seduta.getScadenzaCaricamentoElaborati().before(java.sql.Date.valueOf(LocalDate.now()))) {
			throw new DAOException("Impossibile caricare l'elaborato: data di scadenza superata");
		}
		try {
			conn = DBManager.getConnection();
			String query = "INSERT INTO CARICAMENTO (ASSEGNAZIONE, SEDUTADILAUREA, DATACARICAMENTO) "
					+ "VALUES(?, ?, CURRENT_TIMESTAMP);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, caricamento.getIdAssegnazione());
			stmt.setInt(2, caricamento.getSedutaDiLaurea().getIdSedutaDiLaurea());
			conn.setAutoCommit(false);
			// begin transaction
			stmt.executeUpdate();
			elaboratoDAO.update(StatoElaborato.CARICATO_STUD, caricamento); // caricamento eredita da elaborato
			studenteDAO.update(StatoStudente.PRONTO, caricamento.getStudente());
			// end transaction
			conn.commit();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				caricamento.setIdCaricamento(result.getInt("IDCARICAMENTO"));
			}
			caricamento.setDataAssegnazione(java.sql.Date.valueOf(LocalDate.now()));
			return caricamento;
		} catch (SQLException e) {
			throw new DAOException("Caricamento non riuscito");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new DAOException("Problemi con la connessione");
			}
		}
	}
	
	public Caricamento read(int idCaricamento) throws DAOException {
		AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
		SedutaDiLaureaDAO sedutaDiLaureaDAO = new SedutaDiLaureaDAO();
		Caricamento caricamento = null;
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM CARICAMENTO WHERE IDCARICAMENTO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idCaricamento);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				caricamento = new Caricamento(assegnazioneDAO.read(result.getInt("ASSEGNAZIONE")));
				caricamento.setDataCaricamento(result.getDate("DATACARICAMENTO"));
				caricamento.setSedutaDiLaurea(sedutaDiLaureaDAO.read(result.getInt("SEDUTADILAUREA")));
				caricamento.setIdCaricamento(result.getInt("IDCARICAMENTO"));
				return caricamento;
			} else {
				throw new DAOException("Impossibile leggere il caricamento: id non corretto");
			}
		} catch (SQLException e) {
			throw new DAOException("Impossibile leggere il caricamento");
		}
	}
	
	public Caricamento read(int idCaricamento, SedutaDiLaurea seduta) throws DAOException {
		AssegnazioneDAO assegnazioneDAO = new AssegnazioneDAO();
		Caricamento caricamento = null;
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM CARICAMENTO WHERE IDCARICAMENTO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idCaricamento);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				caricamento = new Caricamento(assegnazioneDAO.read(result.getInt("ASSEGNAZIONE")));
				caricamento.setDataCaricamento(result.getDate("DATACARICAMENTO"));
				caricamento.setSedutaDiLaurea(seduta);
				caricamento.setIdCaricamento(result.getInt("IDCARICAMENTO"));
				return caricamento;
			} else {
				throw new DAOException("Impossibile leggere il caricamento: id non corretto");
			}
		} catch (SQLException e) {
			throw new DAOException("Impossibile leggere il caricamento");
		}
	}
	
	public List<Caricamento> read(SedutaDiLaurea seduta) throws DAOException {
		ArrayList<Caricamento> listaCaricamenti = new ArrayList<Caricamento>();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM CARICAMENTO WHERE SEDUTADILAUREA = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, seduta.getIdSedutaDiLaurea());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				listaCaricamenti.add(this.read(result.getInt("IDCARICAMENTO"), seduta));
			}
			return listaCaricamenti;
		} catch (SQLException e) {
			throw new DAOException("Lettura caricamenti relativi alla seduta non riuscita");
		}
	}

	public void update() {}
	
	public boolean delete(Caricamento caricamento) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM CARICAMENTO WHERE IDCARICAMENTO = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, caricamento.getIdCaricamento());
			int deleted = stmt.executeUpdate();
			if(deleted > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile eliminare il caricamento");
		}
	}
	
}
