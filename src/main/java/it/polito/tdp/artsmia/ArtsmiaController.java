package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi");
    	if (!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, creare prima il grafo");
    		return;
    	}
    	List<Adiacenza> elenco = model.getCoppieArtisti();
    	this.txtResult.appendText("Coppie di artisti: \n");
    	for (Adiacenza a : elenco) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    	if (!model.isGrafoCreato()) { //con variabile booleane all'interno del model
    		this.txtResult.setText("Prima crea il grafo!!!");
    		return;
    	}
    	String id= this.txtArtista.getText();
    	if (id.isEmpty()) {
    		this.txtResult.setText("Inserire un id di un artista");
    		return;
    	}
    	int x;
    	try {
    		x=Integer.parseInt(id);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inseire un valore numerico come id dell'artista");
    		return;
    	}
    	List<Artist> percorso= this.model.percorsoMax(x);
    	if(percorso==null) {
    		this.txtResult.setText("Errore, id dell'artista non valido");
    		return;
    	}
    	this.txtResult.appendText("Percorso trovato:\n");
    	for(Artist a : percorso) {
    		this.txtResult.appendText(a.getName()+" - "+a.getId()+"\n");
    	}
    	this.txtResult.appendText("con un totale di "+percorso.size()+" esposizioni");

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String ruolo= this.boxRuolo.getValue();
    	if (ruolo==null) {
    		this.txtResult.setText("Selezionare un ruolo");
    		return;
    	}
    	this.model.creaGrafo(ruolo);
    	this.txtResult.setText("GRAFO CREATO:\n");
    	this.txtResult.appendText("# Vertici: "+model.getNumVertici() );
    	this.txtResult.appendText("\n# Archi: "+model.getNumArchi() );
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
