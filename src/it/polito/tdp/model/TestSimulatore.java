package it.polito.tdp.model;

import java.time.Month;
import java.time.Year;

import it.polito.tdp.db.EventsDao;

public class TestSimulatore {

	public static void main(String[] args) {
		
		Simulatore sim = new Simulatore();
		Model model = new Model();
		Year year = Year.of(2016);
		
		model.creaGrafo(year);
		EventsDao dao = new EventsDao();
		
		sim.init(model.getGrafo(), dao.loadEventsByDay(year, Month.AUGUST, 23),  7, 6);
		sim.simula();
	}

}
