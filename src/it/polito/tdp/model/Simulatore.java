package it.polito.tdp.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.model.Evento.TipoEvento;

public class Simulatore {
	
	Queue<Evento> queue;
	
	//Stato del mondo 
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Map<Integer, Integer> mappaAgenti; //La chiave è l'id Agente, che parte da 1 ed il valore è il distretto
	
	//Parametri della simulazione
	private int numAgenti;
	private int distrettoPartenza;
	private final double VELOCITA_AGENTE = 60.0;
	
	//Variabili interne
	private Random rand = new Random();
	private int nEventiTotali;
	
	//Parametri in output
	private int nEventiMalGestiti;
	
	
	
	public void init (Graph<Integer, DefaultWeightedEdge> grafo, List<Event> eventi, int numAgenti, int distr) {
		
		queue = new PriorityQueue<>();
		this.mappaAgenti = new HashMap<>();
		
		//Impostiamo i parametri di partenza
		this.grafo = grafo;
		this.numAgenti = numAgenti;
		this.distrettoPartenza = distr;
		
		this.nEventiMalGestiti =0;
		this.nEventiTotali = eventi.size();
		
		//inizializziamo la mappa degli agenti, inizialmente tutti nello stesso distretto
		for (int i = 1; i<= numAgenti; i++) {
			this.mappaAgenti.put(i, this.distrettoPartenza);
		}
		
		//Aggiungiamo tutti gli eventi alla coda assegnandone la durata
		for (Event e : eventi) {
			
			if ( e.getOffense_category_id().equals("all-other-crimes")) {
				float probabilita = rand.nextFloat();
				if (probabilita < 0.5) {
					queue.add(new Evento (e, TipoEvento.INIZIO_CRIMINE, Duration.ofHours(1), 0));
				} else {
					queue.add(new Evento (e, TipoEvento.INIZIO_CRIMINE, Duration.ofHours(2), 0));
				}
			} else {
				
			queue.add(new Evento (e, TipoEvento.INIZIO_CRIMINE, Duration.ofHours(2), 0));
			}
		}
		
	}
	
	
	public int simula() {
		
		while (!queue.isEmpty()) {
			Evento e = queue.poll();
			
			switch (e.getTipo()) {
			
			case INIZIO_CRIMINE:
				//Devo selezionare l'agente più vicino che possa raggiungere il distretto dell'evento
				Double distanza = this.trovaDistanzaAgentePiuVicino(e);
				
				if (distanza == null) {
					this.nEventiMalGestiti++;
					break;
				}
				
				//Calcoliamo il tempo che ci impiega ad arrivare
				double tempoArrivo = distanza/VELOCITA_AGENTE;
				
				//Stabiliamo se l'agente riesce ad arrivare in tempo
				
				if (Duration.ofHours((long)tempoArrivo).toMinutes() >15) {
					nEventiMalGestiti++;
				}
				
				//Assegnamo comunque l'agente all'evento, settando il distretto a 0
				mappaAgenti.put(e.getId_Agente(), 0);
				
				//Scheduliamo l'evento di fine crimine
				queue.add(new Evento(e.getCrimine(), TipoEvento.FINE_CRIMINE, e.getOraInizio().plus(e.getDurata()), Duration.ofHours(0), e.getId_Agente()));
				
			
				break;
				
			case FINE_CRIMINE:
				//Devo liberare l'agente impegnato ad occuparsi del crimine
				mappaAgenti.put(e.getId_Agente(), e.getCrimine().getDistrict_id());
				break;
			}
			System.out.println(e.toString());
		}
		System.out.println("Numero di eventi totali: "+this.nEventiTotali);
		System.out.println("Numero eventi malgestiti: "+this.nEventiMalGestiti);
		return this.nEventiMalGestiti;
	}
	
	
	public Double trovaDistanzaAgentePiuVicino(Evento e) {
		
		double best = Double.MAX_VALUE;
		Integer agente_id = null;
		
		for (Integer i : mappaAgenti.keySet()) {
			
			//Devo calcolare la distanza tra l'evento e gli agenti che non sono impegnati (!=0)
			if (this.mappaAgenti.get(i) !=0) {
				//Se trovo un evento nello stesso distretto, non esiste l'arco e la distanza è zero
				if (mappaAgenti.get(i) == e.getCrimine().getDistrict_id()) {
					e.setId_Agente(i);
					return 0.0;
				}
				
				double distanza = grafo.getEdgeWeight(grafo.getEdge(mappaAgenti.get(i), e.getCrimine().getDistrict_id()));
				if (distanza < best) {
					best = distanza;
					agente_id = i;
				}
			}	
		}
		if (agente_id == null) {
			e.setId_Agente(0);
			return null;
		}
		
		e.setId_Agente(agente_id);
		return best;
	}
}
