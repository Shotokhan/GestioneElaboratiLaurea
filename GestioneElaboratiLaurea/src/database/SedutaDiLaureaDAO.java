package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import entity.Caricamento;
import entity.Docente;
import entity.SedutaDiLaurea;
import entity.Studente;

public class SedutaDiLaureaDAO {

	public SedutaDiLaurea create(Date scadenzaCaricamentoElaborati) throws DAOException {
		SedutaDiLaurea seduta = new SedutaDiLaurea();
		seduta.setScadenzaCaricamentoElaborati(scadenzaCaricamentoElaborati);
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO SEDUTADILAUREA (SCADENZACARICAMENTOELABORATI) VALUES(?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setDate(1, new java.sql.Date(scadenzaCaricamentoElaborati.getTime()));
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				seduta.setIdSedutaDiLaurea(result.getInt("IDSEDUTADILAUREA"));
			}
			return seduta;
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile creare la seduta");
		}
	}

	public SedutaDiLaurea read(int idSeduta) throws DAOException {
		CaricamentoDAO caricamentoDAO = new CaricamentoDAO();
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM SEDUTADILAUREA WHERE IDSEDUTADILAUREA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idSeduta);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				SedutaDiLaurea seduta = new SedutaDiLaurea();
				seduta.setIdSedutaDiLaurea(idSeduta);
				seduta.setScadenzaCaricamentoElaborati(result.getDate("SCADENZACARICAMENTOELABORATI"));
				ArrayList<Caricamento> listaCaricamentiSeduta = (ArrayList<Caricamento>) caricamentoDAO.read(seduta);
				TreeMap<Integer, Docente> treeMapDocenti = new TreeMap<Integer, Docente>();
				TreeMap<Integer, Studente> treeMapStudenti = new TreeMap<Integer, Studente>();
				for(Caricamento caricamento : listaCaricamentiSeduta) {
					treeMapDocenti.put(caricamento.getDocente().getIdDocente(), caricamento.getDocente());
					treeMapStudenti.put(caricamento.getStudente().getIdStudente(), caricamento.getStudente());
				}
				ArrayList<Docente> listaDocenti = new ArrayList<Docente>();
				listaDocenti.addAll(treeMapDocenti.values());
				ArrayList<Studente> listaStudenti = new ArrayList<Studente>();
				listaStudenti.addAll(treeMapStudenti.values());
				seduta.setListaDocenti(listaDocenti);
				seduta.setListaStudenti(listaStudenti);
				return seduta;
			} else {
				throw new DAOException("Non è stato possibile leggere la seduta: id non corretto");
			}
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile leggere la seduta");
		}
	}
	
	public List<SedutaDiLaurea> readAll() throws DAOException {
		List<SedutaDiLaurea> listaSedute = new ArrayList<SedutaDiLaurea>(); 
		try {
			Connection conn = DBManager.getConnection();
			String query = "SELECT * FROM SEDUTADILAUREA;";
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while(result.next()) {
				SedutaDiLaurea seduta = new SedutaDiLaurea();
				seduta.setIdSedutaDiLaurea(result.getInt("IDSEDUTADILAUREA"));
				seduta.setScadenzaCaricamentoElaborati(result.getDate("SCADENZACARICAMENTOELABORATI"));
				listaSedute.add(seduta);
			}
			return listaSedute;
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile ottenere l'elenco delle sedute");
		}
	}
	
	public SedutaDiLaurea update(SedutaDiLaurea seduta, Date nuovaData) throws DAOException {
		if(nuovaData.before(seduta.getScadenzaCaricamentoElaborati())) {
			throw new DAOException("Impossibile anticipare la seduta");
		}
		try {
			Connection conn = DBManager.getConnection();
			String query = "UPDATE SEDUTADILAUREA SET SCADENZACARICAMENTOELABORATI = ? "
					+ "WHERE IDSEDUTADILAUREA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDate(1, (java.sql.Date) nuovaData);
			stmt.setInt(2, seduta.getIdSedutaDiLaurea());
			int affected = stmt.executeUpdate();
			if(affected > 0) {
				seduta.setScadenzaCaricamentoElaborati(nuovaData);
				return seduta;
			} else {
				throw new DAOException("Non è stato possibile aggiornare la seduta: id non corretto");
			}
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile aggiornare la seduta");
		}	
	}
	
	public boolean delete(SedutaDiLaurea seduta) throws DAOException {
		try {
			Connection conn = DBManager.getConnection();
			String query = "DELETE FROM SEDUTADILAUREA WHERE IDSEDUTADILAUREA = ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, seduta.getIdSedutaDiLaurea());
			int deleted = stmt.executeUpdate();
			if(deleted > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Non è stato possibile eliminare la seduta");
		}
	}
}
