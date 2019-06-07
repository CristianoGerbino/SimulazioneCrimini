package it.polito.tdp.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements Comparable<Evento> {
	
	public enum TipoEvento {
		INIZIO_CRIMINE,
		FINE_CRIMINE;
	}

	private Event crimine;
	private TipoEvento tipo;
	private LocalDateTime oraInizio;
	private Duration durata;
	private int id_Agente;
	

	public Evento(Event crimine, TipoEvento tipo, Duration durata, int agente) {
		this.crimine = crimine;
		this.tipo = tipo;
		this.durata = durata;
		this.id_Agente = agente;
		this.oraInizio = crimine.getReported_date();
	}

	public Evento(Event crimine, TipoEvento tipo, LocalDateTime oraInizio, Duration durata, int agente) {
		this.crimine = crimine;
		this.tipo = tipo;
		this.durata = durata;
		this.id_Agente = agente;
		this.oraInizio = oraInizio;
	}



	public Event getCrimine() {
		return crimine;
	}





	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}





	public TipoEvento getTipo() {
		return tipo;
	}





	public void setTipo(TipoEvento tipo) {
		this.tipo = tipo;
	}





	public Duration getDurata() {
		return durata;
	}





	public void setDurata(Duration durata) {
		this.durata = durata;
	}
	
	public int getId_Agente() {
		return id_Agente;
	}





	public void setId_Agente(int id_Agente) {
		this.id_Agente = id_Agente;
	}


	public LocalDateTime getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(LocalDateTime oraInizio) {
		this.oraInizio = oraInizio;
	}



	@Override
	public int compareTo(Evento o) {
		if (this.oraInizio.isBefore(o.oraInizio))
			return -1;
		
		if (this.oraInizio.isAfter(o.oraInizio))
			return 1;
		
		return 0;
	}

	@Override
	public String toString() {
		return this.tipo+" "+this.oraInizio+" durata: "+this.durata.toHours()+" agente assegnato: "+this.id_Agente;
	}
	
	

}
