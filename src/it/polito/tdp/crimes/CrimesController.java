/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.model.ArcoVicino;
import it.polito.tdp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Year> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	this.txtResult.clear();
    	Year year = this.boxAnno.getValue();
    	if (year == null) {
    		this.txtResult.appendText("Seleziona un anno!\n");
    		return;
    	}
    	model.creaGrafo(year);
    	this.txtResult.appendText("Creato grafo creato con "+model.getNVertici()+" vertici e "+model.getNArchi()+" archi!\n");
    	
    	List<Integer> vertici = new LinkedList<Integer>(model.getGrafo().vertexSet());
    	Collections.sort(vertici);
    	
    	for (Integer source: vertici) {
    		List<ArcoVicino> vicini = new LinkedList<ArcoVicino>();
    		vicini = model.getViciniConPeso(source);
    		Collections.sort(vicini);
    		this.txtResult.appendText("Vicini del distretto "+source+":\n");
    		for (ArcoVicino a : vicini)
    		this.txtResult.appendText("-"+a.toString()+"\n");
    	}
    	this.boxAnno.setDisable(true);
    	
    	
    }
    
    
    @FXML
    void doPopolaGiorni(ActionEvent event) {
    	Month month = this.boxMese.getValue();
    	
    	for (Integer i = 1; i<32; i++) {
    		if (month.equals(Month.FEBRUARY) && i == 29 ) 
    			break;
    		
    		
    		if ( (month.equals(Month.APRIL) || month.equals(Month.JUNE) || 
    			month.equals(Month.SEPTEMBER) || month.equals(Month.NOVEMBER)) && i == 31) 
    			break;
    		
    			this.boxGiorno.getItems().add(i);
    				
    		}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	if (model.getGrafo() == null) {
    		this.txtResult.appendText("Errore, devi creare prima una rete cittadina!\n");
    		return;
    	}
    	
    	Year year = this.boxAnno.getValue();
    	if (year == null) {
    		this.txtResult.appendText("Errore: seleziona un anno!\n");
    		return;
    	}
    	
    	Month month = this.boxMese.getValue();
    	if (month == null) {
    		this.txtResult.appendText("Errore: seleziona un mese!\n");
    		return;
    	}
    	
    	Integer day = this.boxGiorno.getValue();
    	if (day == null) {
    		this.txtResult.appendText("Errore: seleziona un giorno!\n");
    	}
    	String nAgenti = this.txtN.getText().trim();
    	if (!nAgenti.matches("^[1-9]$") && !nAgenti.matches("^10$")) {
    		this.txtResult.appendText("Errore: inserisci un numero da 1 a 10!\n");
    		return;
    	}
    	
    	int numAgenti = Integer.parseInt(nAgenti);
    	int distretto = model.trovaDistrettoMinCriminalita(year);
    	LocalDate data = LocalDate.of(year.getValue(), month.getValue(), day);
    	this.txtResult.appendText("SIMULAZIONE CRIMINI\n");
    	this.txtResult.appendText("Distretto "+distretto+" giorno "+data+" "+numAgenti+" agenti:\n");
    	this.txtResult.appendText("Numero eventi malgestiti: "+model.simula(year, month, day, numAgenti, distretto));
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(model.getYears());
    	
    	for (Month m : Month.values()) { 
    		this.boxMese.getItems().add(m);
    	}
    }
}
