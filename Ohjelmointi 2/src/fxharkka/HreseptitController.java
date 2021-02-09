package fxharkka;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import harkka.Nimi;
import harkka.Reseptit;
import harkka.alkioException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Controller hakun‰kym‰n k‰sittelyn
 * @author Mipetoit
 * 16.2.2016
 */
public class HreseptitController implements Initializable {
	
	private ObservableList<String> lajitteleList = FXCollections.observableArrayList("Nimi", "Vaikeustaso", "Hinta", "Aika");
	private ObservableList<Nimi> nimiListaData = FXCollections.observableArrayList();
	private ListView<Nimi> nimiLista = new ListView<Nimi>(); 
	private String tiedNimi = "Reseptit";
	private Reseptit hreseptit = new Reseptit();
	private Nimi nimiKohdalla;					
	
	@FXML
	private ComboBox<String> lajitteleBox;
	
	@FXML
	private ListChooser chooserNimet;		// Nimet t‰h‰n
	
	@FXML
	private TextField textHaku;
	
	@FXML
	private CheckBox checkNimi;
	
	@FXML
	private CheckBox checkAinesosa;
	
	/**
	 * Tallentaa muokatun reseptin.
	 */
	 @FXML          
	 void handleTallenna() {
		tallenna();
	 }
	 
	 @FXML
	 void handleHae() {
		 haeTiedot(0);
	 }
	 
	 @FXML
	 void handlePaivita() {
		haeTiedot(0);
	}
	
	@FXML
	void handlePoista() {
		poistaResepti();
	}
	
	 /**
	  * Avaa uuden ikkunan, kun reseptilistasta klikataan resepti‰.
	  * @throws alkioException poikkeusluokka
	  */
	 @FXML
	 void handleValitse() throws alkioException {
		 naytaResepti();
	 }

	 /**
	  * N‰ytt‰‰ ohjeet.
	  */
	 @FXML
	 void handleOhjeet() {
		 naytaOhjeet();
	 }
	 
	 /**
	  * Lis‰‰ reseptin.
	  */
	 @FXML
	 void handleLisaaResepti() {
		 lisaaResepti();
	 }

	 /**
	  * Luokka reseptien n‰ytt‰miseen listassa. H‰peilem‰ttˆm‰sti kopioitu mallista.
	  */
	 public static class CellNimi extends ListCell<Nimi> {
		 @Override protected void updateItem(Nimi item, boolean empty) {
			 super.updateItem(item, empty);
			 setText(empty ? "" : item.getNimi() + " - " + item.getTekoaika() + " - " + item.getVaikeusaste() + " - " + 
			 item.getHinta());
		 }
	 }
	 
	 
	/**
	 * Tallenta muokatun reseptin.
	 */
	public void tallenna() {
		try {
			hreseptit.tallenna();
			Dialogs.showMessageDialog("Tallennettu!");
		} catch (alkioException e) {
			Dialogs.showMessageDialog("Tallennuksessa ongelmia: " + e.getMessage());
		}
	}
	
	/**
	 * Lis‰‰ listaan reseptin ja hakee sen tiedot.
	 */
	public void lisaaResepti() {
		Nimi uusiNimi = new Nimi();			
		uusiNimi.rekisteroi();		uusiNimi.testiarvot();
		try{
			hreseptit.lisaa(uusiNimi);
			hreseptit.tallenna();			//!!!
		}
		catch(alkioException e) {
			Dialogs.showMessageDialog("Virhe! " + e.getMessage() );
		}
		checkAinesosa.setSelected(false);
		haeTiedot(0);
		// ModalController.showModal(hreseptitController.class.getResource("UusiReseptiGUI.fxml"), "Uusi resepti", null, "");
	}
	
	/**
	 * Poistaa valitun reseptin.
	 */
	public void poistaResepti() {
		// Dialogs.showMessageDialog("Ei osata viel‰ poistaa.");
		boolean vastaus = Dialogs.showQuestionDialog("Poista Resepti", "Haluatko poistaa reseptin " + nimiKohdalla.getNimi() + "?",
				"Kyll‰", "En");
		if(vastaus) {
			try {
				hreseptit.poistaResepti(nimiKohdalla.getId());
				hreseptit.tallenna();
				haeTiedot(0);
				Dialogs.showMessageDialog("Poistettu!");
			} catch(alkioException e) {
				Dialogs.showMessageDialog("Virhe: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Hakee reseptilistaan reseptin nimet & tiedot.
	 * @param id Reseptin id.
	 */
	public void haeTiedot(int id) {
		nimiListaData.clear();
		int index = 0;
		String cbEhto = lajitteleBox.getValue();
		Collection<Nimi> haetutNimet = new ArrayList<Nimi>();
		String haku = textHaku.getText();
		if(haku.indexOf('*') < 0) haku = "*" + haku + "*";
		if(checkNimi.isSelected() && !checkAinesosa.isSelected()) {
			haetutNimet = hreseptit.haeNimista(haku, cbEhto);
		}
		if(checkAinesosa.isSelected() && !checkNimi.isSelected()) {
			if(haku.equals("**")) haetutNimet = hreseptit.haeNimista(haku, cbEhto);
			else haetutNimet = hreseptit.haeAinesosista(haku, cbEhto);
		}
		if((checkAinesosa.isSelected() && checkNimi.isSelected()) || (!checkAinesosa.isSelected() && !checkNimi.isSelected())) {
			for(int i = 0; i < hreseptit.getNimetlkm(); i++) {
				haetutNimet = hreseptit.haeNimista("", cbEhto);
			}
		}
		int i = 0;
		for(Nimi n : haetutNimet) {
			if(n.getId() == id) index = i;
			nimiListaData.add(n);
		}
		nimiLista.setItems(nimiListaData);
		nimiLista.getSelectionModel().select(index);
	}
	
	/**
	 * Laittaa lajitteleBoxiin oikeat arvot. Kytketty initializeen.
	 */
	protected void alusta() {
		lajitteleBox.setValue("Nimi");
		lajitteleBox.setItems(lajitteleList);
		BorderPane parent = (BorderPane)chooserNimet.getParent();
		parent.setCenter(nimiLista);
		nimiLista.setCellFactory( p -> new CellNimi() );
		nimiLista.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {naytaTiedot();});
	}
	
	/**
	 * N‰ytt‰‰ ohjeet alkun‰ytˆss‰.
	 */
	public void naytaOhjeet() {
		Dialogs.showMessageDialog("Kirjoita tekstikentt‰‰n haku, ja valitse alta mit‰ sill‰ haet. Klikkaa resepti‰ ja 'valitse'"
				+ " valitaksesi reseptin.");
	}
	
	/**
	 * N‰ytt‰‰ listasta valitun reseptin, kun painetaan 'valitse'-n‰pp‰int‰.
	 */
	public void naytaResepti() {
		if(nimiKohdalla == null) return;
		ModalController.<Nimi, ValittureseptiController>showModal(HreseptitController.class.getResource("ValittureseptiGUI.fxml"),
				nimiKohdalla.getNimi(), null, nimiKohdalla, ctrl -> ctrl.setReseptit(hreseptit, nimiKohdalla));
		haeTiedot(0);
	}
	
	/**
	 * N‰ytt‰‰ reseptin tiedot listChooserissa.
	 */
	public void naytaTiedot() {
		nimiKohdalla = nimiLista.getSelectionModel().getSelectedItem();
		if (nimiKohdalla == null) return;
	}

	/**
	 * Asettaa k‰ynnistyksen yhteydess‰ ohjelmalle oman Reseptit-luokan
	 * @param reseptit Reseptit-luokka
	 * @throws alkioException poikkeusluokka
	 */
	public void setReseptit(Reseptit reseptit) throws alkioException {
		this.hreseptit = reseptit;
		naytaTiedot();
	}

	/**
	 * Lukee ohjelman tallennetut reseptit tiedostosta.
	 */
	public void lueTiedostosta() {
		String reseptitTied = tiedNimi;							//tiedNimi == "Reseptit"
		try {
			hreseptit.lueTiedostosta(reseptitTied);
			haeTiedot(0);
		} catch (alkioException e) {
			Dialogs.showMessageDialog("Tiedostoa ei lˆydy...");
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
}
