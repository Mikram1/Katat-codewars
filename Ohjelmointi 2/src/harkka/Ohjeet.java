package harkka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Luokka ohjeiden taulukkoon lis‰‰mist‰ varten.
 */
public class Ohjeet {
	private static final int MAX_OHJEET = 80;					
	private Ohje[] alkiot = new Ohje[MAX_OHJEET];				// Varsinainen ohjetaulukko
	private String tiedostonNimi = "ohjeet";					// Tallennettavan tiedoston oletusnimi.
	private int ohjeLkm = 0;									// Pit‰‰ kirjaa ohjeet-taulukon ohjeista.
	private boolean muutettu = false;							// Pit‰‰ kirjaa siit‰, onko ohjetta muutettu. (Tallennus)
	
	
	/**
	 * @return Montako ohjetta nykyisess‰ ohjeet-taulukossa on.
	 */
	public int getLkm() {
		return ohjeLkm;
	}
	
	/**
	 * @param i indeksi alkiot-taulukossa.
	 * @return Palauttaa alkiot-taulukossa paikassa [i] olevan ohjeen.
	 */
	public Ohje annaOhje(int i) {
		return alkiot[i];
	}
	
	/**
	 * Lis‰‰ yhden ohjeen ohjeet-taulukkoon. Jos enemm‰n ohjeita kun tilaa, heitt‰‰ poikkeuksen.
	 * @param ohje lis‰tt‰v‰ ohje
	 * @throws alkioException poikkeusluokka
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Ohjeet ohjeet1 = new Ohjeet();
	 * 
	 * Ohje o1 = new Ohje(1);		Ohje o2 = new Ohje(2);		Ohje o3 = new Ohje(3);
	 * ohjeet1.lisaaOhje(o1);		ohjeet1.lisaaOhje(o2);		ohjeet1.lisaaOhje(o3);
	 * 
	 * ohjeet1.annaOhje(0) === o1;
	 * ohjeet1.annaOhje(2) === o3;
	 * </pre>
	 */
	public void lisaaOhje(Ohje ohje) throws alkioException {
		if(ohjeLkm >= alkiot.length) throw new alkioException("Tila loppu!");
		alkiot[ohjeLkm] = ohje;
		ohjeLkm++;
		muutettu = true;
	}
	
	/**
	 * Poistaa kaikki saman id:n ohjeet. Tehd‰‰n reseptin poiston yhteydess‰.
	 * @param id nimi-id
	 * @return true, jos alkioita poistui, false jos ei.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Ohjeet ohjeet = new Ohjeet();
	 * Ohje o1 = new Ohje(1);			Ohje o2 = new Ohje(1);			Ohje o3 = new Ohje(2);
	 * ohjeet.lisaaOhje(o1);			ohjeet.lisaaOhje(o2);			ohjeet.lisaaOhje(o3);
	 * 
	 * ohjeet.poistaOhjeet(1) === true;
	 * ohjeet.getLkm() === 1;
	 * </pre>
	 */
	public boolean poistaOhjeet(int id) {
		int muutetut = 0;
		for(int i = 0; i < ohjeLkm; i++) {
			if(alkiot[i].getNimiId() == id) {
				alkiot[i] = null;
				ohjeLkm--;
				muutetut++;
			}
		}
		if(muutetut <= 0) return false;
		this.muutettu = true;
		return true;
	}
	
	
	/**
	 * Palauttaa (listana) kaikki tietyn reseptin ohjeet sen nimi-id:n perusteella.
	 * @param id Nimi-id
	 * @return Lista lˆydetyist‰ ohjeista.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * #import java.util.*; 
	 * Ohjeet ohjeet2 = new Ohjeet();
	 * 
	 * Ohje o1 = new Ohje(1);		Ohje o2 = new Ohje(2);		Ohje o3 = new Ohje(1);
	 * o1.testiarvot();				o2.testiarvot();			o3.testiarvot();
	 * ohjeet2.lisaaOhje(o1);		ohjeet2.lisaaOhje(o2);		ohjeet2.lisaaOhje(o3);
	 * 
	 * List<Ohje> tulos1;					List<Ohje> tulos2;
	 * tulos1 = ohjeet2.annaOhjeet(1);		
	 * tulos2 = ohjeet2.annaOhjeet(2);
	 * tulos1.size() === 2;
	 * tulos2.size() === 1;
	 * </pre>
	 */
	public List<Ohje> annaOhjeet(int id) {
		List<Ohje> loydetyt = new ArrayList<Ohje>();
		for(int i = 0; i < alkiot.length; i++) {
			if(annaOhje(i) != null) {
				Ohje nykOhje = annaOhje(i);
				if(nykOhje.getNimiId() == id) loydetyt.add(nykOhje);
			}
		}
		return loydetyt;
	}
	
	/**
	 * Muuttaa ohjetiedoston ohjeen sit‰ muokattaessa.
	 * @param nimiId p‰‰llekirjoitettavan nimi-id
	 * @param indeksi indeksi, jossa ohje sijaitsee. (Esim. nimi-id 2, indeksi 2 -> toinen ohje, jonka nimi-id on 2.
	 * @param uusiTeksti miksi ohjeen teksti muuttuu.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Ohjeet ohjeet = new Ohjeet();
	 * Ohje o1 = new Ohje(1);		Ohje o2 = new Ohje(1);		Ohje o3 = new Ohje(2);
	 * o1.testiarvot();				o2.testiarvot();			o3.testiarvot();
	 * ohjeet.lisaaOhje(o1);		ohjeet.lisaaOhje(o2);		ohjeet.lisaaOhje(o3);
	 * 
	 * ohjeet.paalleKirjoita(1, 1, "Laita pannu tulille.");
	 * Ohje muokattuO2 = ohjeet.annaOhje(1);
	 * muokattuO2.getOhje().equals("Laita pannu tulille.") === true;
	 * </pre>
	 */
	public void paalleKirjoita(int nimiId, int indeksi, String uusiTeksti) {
		int monesOhje = 0;
		for(int i = 0; i < ohjeLkm; i++) {
			if(alkiot[i].getNimiId() == nimiId) {
				if(monesOhje == indeksi) {
					 Ohje muokattuOhje= new Ohje(nimiId);
					 muokattuOhje.setOhje(uusiTeksti);
					 alkiot[i] = muokattuOhje;
					 muutettu = true;
				}
				monesOhje++;
			}
		}
	}
	
	/**
	 * Tallentaa ohjeet tiedostoon.
	 * @throws alkioException poikkeusluokka
	 */
	public void tallenna() throws alkioException { 
		if(!muutettu)return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try ( PrintStream fo = new PrintStream(new FileOutputStream(ftied)) ) {
			for(int i = 0; i < this.getLkm(); i++) {
				Ohje o = this.alkiot[i];
				fo.println(o);
			}
		} catch (IOException e) {
			throw new alkioException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia.");
		}
	}
	
	/**
	 * Lukee tietoja oletustiedostosta.
	 * @throws alkioException poikkeusluokka
	 */
	public void lueTiedostosta() throws alkioException {
		lueTiedostosta(getTiedostonNimi());
	}
	
	/**
	 * Lukee tiedostosta ohjeet & asettaa ne.
	 * @param hakemisto Mist‰ ohjeet haetaan.
	 * @throws alkioException poikkeusluokka.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * #import java.io.File;
	 * 
	 * String hakemisto = "testihakemistoO";
	 * String tiedosto = hakemisto + "/ohjeetTesti";
	 * File ohjeTiedosto = new File(tiedosto + ".dat");
	 * File dir = new File(hakemisto);
	 * dir.mkdir();
	 * ohjeTiedosto.delete();
	 * 
	 * Ohjeet ohjeet = new Ohjeet();
	 * Ohje o1 = new Ohje();			o1.testiarvot();
	 * Ohje o2 = new Ohje();			o2.testiarvot();
	 * ohjeet.lisaaOhje(o1);			ohjeet.lisaaOhje(o2);
	 * ohjeet.setTiedostonNimi(tiedosto);
	 * ohjeet.tallenna();
	 * 
	 * ohjeet = new Ohjeet();
	 * ohjeet.lueTiedostosta(tiedosto);
	 * Ohje ohje1 = ohjeet.annaOhje(0);
	 * ohje1.getOhje().equals(o1.getOhje());
	 * </pre>
	 */
	public void lueTiedostosta(String hakemisto) throws alkioException {
		setTiedostonNimi(hakemisto);
		String rivi;
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			while((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ( "".equals(rivi)) continue;
				Ohje ohje = new Ohje();
				ohje.parse(rivi);
				lisaaOhje(ohje);
			}
			muutettu = false;
		} catch (FileNotFoundException e) {
			throw new alkioException("Tiedostoa " + getTiedostonNimi() + " ei lˆydy/ei aukea.");
		} catch (IOException e) {
			throw new alkioException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
		
	}
	
	/**
	 * @return tiedoston nimi, mihin tallennetaan ohjeet.
	 */
	public String getTiedostonNimi() {
		return tiedostonNimi;
	}
	
	/**
	 * Asettaa tallennettavan tiedoston nimeksi annetun merkkijonon.
	 * @param nimi Tiedoston (tuleva) nimi.
	 */
	public void setTiedostonNimi(String nimi) {
		this.tiedostonNimi = nimi;
	}
	
	/**
	 * @return varmuuskopiona toimivan tiedoston nimi.
	 */
	public String getBakNimi() {
		return tiedostonNimi + ".bak";
	}
	
	/**
	 * Tulostaa ohjeet-taulukon j‰senet.
	 */
	public void tulosta() {
		for(int i = 0; i < this.getLkm(); i++) {
			Ohje testi = this.annaOhje(i);
			testi.tulosta(System.out);
		}
	}
	
	/**
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Ohjeet ohjeet = new Ohjeet();
		Ohje o1 = new Ohje();		o1.rekisteroi();		o1.testiarvot();
		Ohje o2 = new Ohje();		o2.rekisteroi();		o2.testiarvot();
		try {
			ohjeet.lisaaOhje(o1);
			ohjeet.lisaaOhje(o2);
			ohjeet.tulosta();
		} catch (alkioException e){
			System.out.println(e.getMessage());
		}
	}
}	
