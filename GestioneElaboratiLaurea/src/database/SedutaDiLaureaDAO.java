package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import entity.SedutaDiLaurea;

public class SedutaDiLaureaDAO {

	public SedutaDiLaurea create(Date scadenzaCaricamentoElaborati) throws DAOException {
		SedutaDiLaurea seduta = new SedutaDiLaurea();
		seduta.setScadenzaCaricamentoElaborati(scadenzaCaricamentoElaborati);
		try {
			Connection conn = DBManager.getConnection();
			String query = "INSERT INTO SEDUTADILAUREA (SCADENZACARICAMENTOELABORATI) VALUES(?);";
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setDate(1, (java.sql.Date) scadenzaCaricamentoElaborati);
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

	public SedutaDiLaurea read(SedutaDiLaurea seduta) {
		return seduta;
	}
	
	public SedutaDiLaurea update(SedutaDiLaurea seduta, Date nuovaData) {
		seduta.setScadenzaCaricamentoElaborati(nuovaData);
		return seduta;
	}
	
	public boolean delete(SedutaDiLaurea seduta) {
		return false;
	}
}
