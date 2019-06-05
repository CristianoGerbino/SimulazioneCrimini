package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Year> listAllYears () {
		
		String sql = "SELECT distinct Year(reported_date) as year FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Year> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
				list.add(Year.of(res.getInt("year")));
			}
			conn.close();
			return list ;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> listAllDistricts () {
		
		String sql = "SELECT distinct district_id FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
				list.add(res.getInt("district_id"));
			}
			conn.close();
			return list ;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Map<Integer, LatLng> listAllCords(Year year) {
		
		String sql = "SELECT distinct district_id, AVG(geo_lon), AVG(geo_lat) " + 
				"FROM EVENTS "+ 
				"WHERE YEAR(reported_date) = ? "+ 
				"GROUP BY district_id";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			Map<Integer, LatLng> result = new HashMap<Integer, LatLng>();
			
			st.setInt(1, year.getValue());
			
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
				result.put(res.getInt("district_id"), new LatLng(res.getDouble("AVG(geo_lat)"),res.getDouble("AVG(geo_lon)")));
			}
			conn.close();
			return result ;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer trovaDistretto(Year year) {
		
		String sql = "SELECT district_id, COUNT(*) as numEventi "+ 
				"FROM EVENTS "+ 
				"WHERE YEAR(reported_date) = ? "+ 
				"GROUP BY district_id "+ 
				"ORDER BY numEventi";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			int id = 0;
			
			
			st.setInt(1, year.getValue());
			
			ResultSet res = st.executeQuery() ;
			
			if (res.next()) {
				id = res.getInt("district_id");
			}
			conn.close();
			return id ;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}


