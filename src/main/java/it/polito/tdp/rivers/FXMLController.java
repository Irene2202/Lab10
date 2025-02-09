/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doSelezionaFiume(ActionEvent event) {
    	txtResult.clear();
    	River r=boxRiver.getValue();
    	model.selezionaFiume(r);
    	txtNumMeasurements.setText(""+r.getFlows().size());
    	txtStartDate.setText(""+r.getFlows().get(0).getDay());
    	txtEndDate.setText(""+r.getFlows().get(r.getFlows().size()-1).getDay());
    	String media=String.format("%.4f", r.getFlowAvg());
    	txtFMed.setText(media);
    }
    
    @FXML
    void doSimulazione(ActionEvent event) {
    	River r=boxRiver.getValue();
    	String kS=txtK.getText();
    	
    	if(r==null) {
    		txtResult.setText("Fiume non selezionato");
    		return;
    	}
    	else if(kS.isEmpty()) {
    		txtResult.setText("Fattore di scala, k, non inserito");
    		return;
    	}
    	
    	float k;
    	try {
    		k=Float.parseFloat(kS);
    	} catch (NumberFormatException e) {
    		txtResult.setText("Il fattore di scala inserito non ha un formato valido");
    		return;
    	}
    	
    	
    	model.init(k, r);
    	model.run();
 
    	txtResult.setText("Numero Giorni con flusso in uscita insufficiente: "+model.getNumGiorni());
    	String Cmax=String.format("%.4f", model.getCapienzaMax());
    	txtResult.appendText("\nCapienza Massima del bacino idrico: "+Cmax);
    	String Cmed=String.format("%.4f", model.getCMed());
    	txtResult.appendText("\nCapienza Media del bacino idrico: "+Cmed);
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxRiver.getItems().addAll(model.getAllRivers());
    }
}
