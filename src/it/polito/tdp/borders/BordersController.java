package it.polito.tdp.borders;

import java.io.DataOutput;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {
	
    Model model;
	
	public void setModel(Model model) {
		this.model=model;
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAnno;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Country> comboBox;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	
    	try {
			
    		//Creo grafo
    		model.creaGrafo(Integer.parseInt(txtAnno.getText()));
    		
    		//Pulisco il campo risutati
    		txtResult.clear();
    		
    		//Stampo risultati
    		txtResult.appendText("Numero componenti connesse: "+model.getNumComponentiConnesse()+"\n\n");
    		
    		for (Country stato : model.getGrafo().vertexSet()) {	
    			txtResult.appendText(stato.toString()+" -> "+model.getGrafo().degreeOf(stato)+"\n");
    		}
    		
    		//Popolo comboBox
    		comboBox.getItems().addAll(model.getGrafo().vertexSet());
    		
    		}catch(NumberFormatException nbe) {
    			txtResult.appendText("Inserire un formato di anno corretto!+\n");
    		}

    }

    @FXML
    void doTrovaVicini(ActionEvent event) {
    	
    	List<Country> raggiungibili = model.trovaVicini1(comboBox.getValue());
    	
    	txtResult.clear();
    	
    	txtResult.appendText("#STATI RAGGIUNGIBILI = "+raggiungibili.size()+"\n\n");
    	for (Country stato : raggiungibili) {
    		txtResult.appendText(stato.toString()+"\n");
    	}

    }

    @FXML
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
        assert comboBox != null : "fx:id=\"comboBox\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Borders.fxml'.";

    }
}
