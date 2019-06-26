package it.polito.tdp.model;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	
	
	public Model () {
		dao = new EventsDao();
	}
	
	public void creaGrafo(Year year) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listAllDistricts()); 
		//Sarebbe stato meglio selezionare i distretti in relazione all'anno passato dall'utente
		
		Map<Integer, LatLng> map = new HashMap<Integer, LatLng>(dao.listAllCords(year));
		
		for (Integer i : map.keySet()) {
			for (Integer o : map.keySet()) {
				if (!i.equals(o)) {//Usare equals per Integer!!!! 
					//meglio controllare anche se l'arco fosse già esistente
					if (grafo.getEdge(i, o) == null) {
				double distanza = LatLngTool.distance(map.get(i), map.get(o), LengthUnit.KILOMETER);
				System.out.println(distanza);
				Graphs.addEdge(grafo, i, o, distanza);
				}
			}
		}	
	}
		
	
}

	
	
	public List<Year> getYears () {
		List<Year> list = new LinkedList<Year>(dao.listAllYears());
		Collections.sort(list);
		return list;
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<ArcoVicino> getViciniConPeso (Integer source) {
		
		List<ArcoVicino> vicini = new ArrayList<ArcoVicino>();
		for (Integer target : Graphs.neighborListOf(grafo, source)) {
			double peso = grafo.getEdgeWeight(grafo.getEdge(source, target));
			vicini.add(new ArcoVicino(source, target, peso));
		}
		return vicini;
	}
	
	public int trovaDistrettoMinCriminalita(Year year) {
		return this.dao.trovaDistrettoMinCriminalita(year);
	}
	
	public Integer simula(Year year, Month month, int day, int numAgenti, int distr) {
		Simulatore sim = new Simulatore();
		sim.init(grafo, dao.loadEventsByDay(year, month, day), numAgenti, distr);
		return sim.simula();
		
	}

	public Graph<Integer, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	
	
}


