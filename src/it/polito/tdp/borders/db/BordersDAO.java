package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries(Map<Integer, Country> idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country stato = new Country(rs.getInt("ccode"), rs.getString("StateNme"), rs.getString("StateAbb"));
				result.add(stato);
				idMap.put(stato.getId(), stato);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno) {

		final String sql="SELECT state1no AS id1, state2no AS id2 FROM contiguity WHERE YEAR<=? AND conttype=1 AND state1no>state2no";
		List<Border> confini = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			//Setto parametro
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {

			Border confine = new Border(rs.getInt("id1"), rs.getInt("id2"));
			confini.add(confine);
			}
			
			conn.close();
			return confini;
		
	        } catch (SQLException e) {
		    e.printStackTrace();
		    System.out.println("Errore connessione al database");
		    throw new RuntimeException("Error Connection Database");
	        }
		}
}
