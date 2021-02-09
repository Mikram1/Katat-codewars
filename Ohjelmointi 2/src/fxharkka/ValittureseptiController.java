package fxharkka;

import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.StringGrid;
import harkka.Ainesosa;
import harkka.Ainesosat;
import harkka.Nimet;
import harkka.Nimi;
import harkka.Ohje;
import harkka.Ohjeet;
import harkka.Reseptit;
import harkka.alkioException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller valitun reseptin n‰kym‰‰n.
 * @author Mipetoit
 * 16.2.2016
 */
public class ValittureseptiController implements ModalControllerInterface<Nimi> {
	private Reseptit hreseptit = new Reseptit();
	private Nimi muokattavaNimi;
	private Ohjeet ohjeet;
	private Ainesosat ainesosat;
	private Nimet nimet;
	private List<Ainesosa> ainesosaLista = new ArrayList<Ainesosa>();			// Pit‰‰ kirjaa oikean id:n ainesosista.
	private List<Ohje> ohjeLista  = new ArrayList<Ohje>();						// -||- ohjeista.
	
	@FXML
	private Label labelVirhe;
	
	@FXML
	private StringGrid<?> sgAinesosat;
	
	@FXML
	private StringGrid<?> sgOhjeet;
	
	@FXML
	private TextField textRNimi;			//Reseptin nimi
	
	@FXML
	private TextField textTekoaika;
	
	@FXML
	private TextField textVaikeusaste;
	
	@FXML
	private TextField textHinta;
	
	@FXML
	void lisaaRiviA() {
		lisaaRivia();
	}
	
	@FXML
	void lisaaRiviO() {
		lisaaRivio();
	}
	
	
	@FXML
	void handlePoista() {
		poistaResepti();
	}
	
	@FXML
	void handleOhjeet() {
		naytaOhjeet();
	}
	
	@FXML
	void handleTallenna() {
		tallenna();
	}
	
	@FXML
	void handleTarkista() {
		haeKentta();
	}

	
	/**
	 * Poistaa valitun reseptin.
	 */
	public void poistaResepti() {
		// Dialogs.showMessageDialog("Ei osata viel‰ poistaa.");
		boolean vastaus = Dialogs.showQuestionDialog("Poista Resepti", "Haluatko poistaa reseptin " + muokattavaNimi.getNimi() + "?",
				"Kyll‰", "En");
		if(vastaus) {
			try {
				hreseptit.poistaResepti(muokattavaNimi.getId());
				hreseptit.tallenna();
				Dialogs.showMessageDialog("Poistettu!");
			} catch(alkioException e) {
				Dialogs.showMessageDialog("Virhe: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Muokkaa valittua resepti‰.
	 */
	public void muokkaaReseptia() {
		Dialogs.showMessageDialog("Ei osata viel‰ muokata.");
	}
	
	/**
	 * N‰ytt‰‰ ohjeet muokkausikkunan k‰yttˆˆn.
	 */
	public void naytaOhjeet() {
		Dialogs.showMessageDialog("Valitse yl‰palkista muokkaa... -> muokkaa resepti‰ muuttaaksesi sen tietoja. Paina alanurkasta <Tallenna> tallentaaksesi muokatut tiedot.");
	}
	
	/**
	 * Tallentaa muokatun reseptin.
	 */
	public void tallenna() {
		//Dialogs.showMessageDialog("Ei osata viel‰ tallentaa.");
		
		String resNimi = textRNimi.getText();
		String tekoaika = textTekoaika.getText();
		String vaikeusaste = textVaikeusaste.getText();
		String hinta = textHinta.getText();
		hreseptit.paalleKirjoitaNimet(muokattavaNimi.getId(), resNimi, tekoaika, vaikeusaste, hinta);
		
		for(int i = 0; i < sgOhjeet.getItems().size(); i++) {
			String uusiTeksti = sgOhjeet.get(i, 0);
			hreseptit.paalleKirjoitaOhje(muokattavaNimi.getId(), i, uusiTeksti);
		}
		for(int i = 0; i < sgAinesosat.getItems().size(); i++) {
			String[] uusiteksti = sgAinesosat.get(i, 0).split(" ");
			hreseptit.paalleKirjoitaAinesosa(muokattavaNimi.getId(), i, uusiteksti);
		}
		try {
			hreseptit.tallenna();
			Dialogs.showMessageDialog("Tallennettu!");
		} catch (alkioException e) {
			Dialogs.showMessageDialog("Tallennuksessa ongelmia: " + e.getMessage());
		}
		
	}

	/**
	 * Tarkistaa syˆtettyjen tietojen oikeellisuuden.
	 */
	public void haeKentta() {
		String syˆte = "";
		String ehto = "";
		TextField valittu = new TextField();
		if(textRNimi.isFocused()) {
			syˆte = textRNimi.getText();
			valittu = textRNimi;
		}
		if(textHinta.isFocused()) {
			syˆte = textHinta.getText();
			valittu = textHinta;
		}
		if(textTekoaika.isFocused()) {
			syˆte = textTekoaika.getText();
			valittu = textTekoaika;
		}
		if(textVaikeusaste.isFocused()) {
			syˆte = textVaikeusaste.getText();
			ehto = "Vaikeusaste";
			valittu = textVaikeusaste;
		}
		if(syˆte.equals("")) return;
		String virhe = tarkistaKentta(syˆte, ehto);
		if(virhe == null) {
			Dialogs.setToolTipText(valittu, "");
			valittu.getStyleClass().removeAll("virhe");
			naytaVirhe("");
		}
		else {
			Dialogs.setToolTipText(valittu, virhe);
			valittu.getStyleClass().add("virhe");
			naytaVirhe(virhe);
		}
		
	}
	
	/**
	 * Asettaa virheen n‰kyviin ruudun alalaitaan.
	 * @param virhe aiheutunut virhe.
	 */
	public void naytaVirhe(String virhe) {
		if(virhe.equals(null) || virhe.isEmpty()) {
			labelVirhe.setText("");
			labelVirhe.getStyleClass().removeAll("virhe");
			return;
		}
		labelVirhe.setText(virhe);
		labelVirhe.getStyleClass().add("virhe");
	}
	
	/**
	 * Tarkistaa, onko kent‰ss‰ sopivat tiedot.
	 * @param syˆte Kentt‰‰n syˆtetty merkkijono
	 * @param ehto Ehto, joka m‰‰r‰ytyy kent‰n mukaan -> mit‰ kentt‰‰n kelpaa.
	 * @return virhe tai null, jos onnistuu.
	 * @example
	 * <pre name="test">
	 * 
	 * </pre>
	 */
	public String tarkistaKentta(String syˆte, String ehto) {
		if(syˆte.contains("|")) {
			return "Kentt‰ ei voi saada arvoa '|'!";
		}
		if(ehto.equals("Vaikeusaste")) {
			if(!tryParse(syˆte)) return "Kentt‰ t‰ytyy olla numerona!";
		}
		return null;
	}
	
	/**
	 * Testaa, voiko kent‰n tietoja tulkita kokonaisluvuksi.
	 * @param s Merkkijono kent‰n tiedoista
	 * @return true, jos muunnos kokonaisluvuksi onnistuu, muuten false.
	 */
	public boolean tryParse(String s) {
		try{
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Lis‰‰ rivin ainesosiin.
	 */
	public void lisaaRivia() {
		Ainesosa ainesosa = new Ainesosa(muokattavaNimi.getId());
		ainesosa.testiarvot();
		hreseptit.lisaa(ainesosa);
		sgAinesosat.add(ainesosa.getAinesosa());
	}
	
	/**
	 * Lis‰‰ rivin ohjeisiin.
	 */
	public void lisaaRivio() {
		Ohje ohje = new Ohje(muokattavaNimi.getId());
		ohje.testiarvot();
		try {
			hreseptit.lisaa(ohje);
			sgOhjeet.add(ohje.getOhje());
		} catch (alkioException e) {
			Dialogs.showMessageDialog("Virhe! " + e.getMessage() );
		}
	}
	
	/**
	 * Antaa muokattavalle reseptille sen nimen. Tehd‰‰n valinnan yhteydess‰.
	 * @param reseptit reseptit
	 * @param nimi muokattava nimi
	 */
	public void setReseptit(Reseptit reseptit, Nimi nimi) {
		this.hreseptit = reseptit;
		this.muokattavaNimi = nimi;
	}
	
	/**
	 * "T‰ytt‰‰" valitun resepti-ikkunan reseptin oikeilla tiedoilla sen nimi-id:n mukaan
	 * @param id reseptin nimi-id
	 */
	public void annaTiedot(int id) {
		ainesosat = new Ainesosat();
		ohjeet = new Ohjeet();
		nimet = new Nimet();
		try {
			nimet.lueTiedostosta();
			ainesosat.lueTiedostosta();
			ohjeet.lueTiedostosta();
		} catch(alkioException e) {
			Dialogs.showMessageDialog("VIRHE");
		}
		ainesosaLista = ainesosat.annaAinesosat(id);
		ohjeLista = ohjeet.annaOhjeet(id);
		for(Ainesosa ao:ainesosaLista) {
			sgAinesosat.add(ao.getAinesosa() + " " + ao.getMaara());
		}
		for(Ohje o:ohjeLista) {
			sgOhjeet.add(o.getOhje());
		}
		textRNimi.setText(muokattavaNimi.getNimi());
		textVaikeusaste.setText(muokattavaNimi.getVaikeusaste());
		textTekoaika.setText(muokattavaNimi.getTekoaika());
		textHinta.setText(muokattavaNimi.getHinta());
	}
	
	@Override
	public void handleShown() {
		annaTiedot(muokattavaNimi.getId());
	}

	@Override
	public Nimi getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefault(Nimi arg0) {
		// TODO Auto-generated method stub
		
	}
}
