package harkka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Ainesosat-luokka ainesosien tallentamiseen taulukkoon.
 */
public class Ainesosat {
	private String tiedostonNimi = "ainesosat";									// Tiedoston nimi, johon tietoja tallennetaan.
	private Collection<Ainesosa> alkiot = new ArrayList<Ainesosa>();			// Ainesosataulukko.
	private boolean muutettu = false;											// Pit‰‰ kirjaa muutoksista => tarviiko tallentaa.
	
	/**
	 * Lis‰‰ ainesosan listaan alkiot.
	 * @param ainesosa lis‰tt‰v‰ ainesosa
	 * @example
	 * <pre name="test">
	 * Ainesosat ainesosat = new Ainesosat();
	 * Ainesosa a1 = new Ainesosa(1);		Ainesosa a2 = new Ainesosa(2);
	 * a1.testiarvot();						a2.testiarvot();
	 * ainesosat.lisaaAinesosa(a1);			ainesosat.lisaaAinesosa(a2);
	 * ainesosat.getLkm() === 2;
	 * </pre>
	 */
	public void lisaaAinesosa(Ainesosa ainesosa) {
		alkiot.add(ainesosa);
		muutettu = true;
	}
	
	/**
	 * Poistaa kaikki tietyll‰ id:ll‰ varustetut ainesosat.
	 * @param id nimi-id
	 * @return true, jos yksikin poistui, false, jos ei.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Ainesosat ainesosat = new Ainesosat();
	 * Ainesosa a1 = new Ainesosa(1);		Ainesosa a2 = new Ainesosa(1);		Ainesosa a3 = new Ainesosa(2);
	 * ainesosat.lisaaAinesosa(a1);			ainesosat.lisaaAinesosa(a2);		ainesosat.lisaaAinesosa(a3);
	 * 
	 * ainesosat.poistaAinesosat(2) === true;
	 * ainesosat.getLkm() === 2;
	 * ainesosat.poistaAinesosat(7) === false;
	 * ainesosat.poistaAinesosat(1) === true;
	 * ainesosat.getLkm() === 0;
	 * </pre>
	 */
	public boolean poistaAinesosat(int id) {
		int poistetut = 0;
		for(Iterator<Ainesosa> iterator = alkiot.iterator(); iterator.hasNext();) {
			Ainesosa a = iterator.next();
			if(a.getNimiId() == id) {
				iterator.remove();
				poistetut++;
			}
		}
		if(poistetut <= 0) return false;
		muutettu = true;
		return true;
	}
	
	/**
	 * Etsii resepteist‰ ainesosia, mitk‰ vastaavat hakua. Ottaa aina yhden ainesosan per resepti.
	 * @param haku Hakukentt‰‰n syˆtetty teksti.
	 * @return Kokoelma reseptien nimi-id:eit‰, jotka tulkitaan myˆhemmin listaksi nimi‰.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Ainesosat ainesosat = new Ainesosat();
	 * Ainesosa a1 = new Ainesosa(1);			Ainesosa a2 = new Ainesosa(2);
	 * Ainesosa a3 = new Ainesosa(1);			Ainesosa a4 = new Ainesosa(3);
	 * 
	 * a1.setAinesosa("300g Kana");			a2.setAinesosa("400g Jauheliha");
	 * a3.setAinesosa("Kanaa");				a4.setAinesosa("Vesi");
	 * 
	 * ainesosat.lisaaAinesosa(a1);		ainesosat.lisaaAinesosa(a2);		ainesosat.lisaaAinesosa(a3);		ainesosat.lisaaAinesosa(a4);
	 * List<Integer> tulokset = ainesosat.etsi("*kan*");
	 * tulokset.size() === 1;
	 * List<Integer> tulokset2 = ainesosat.etsi("*a*");
	 * tulokset2.size() === 2;
	 * </pre>
	 */
	public List<Integer> etsi(String haku) {
		String ehto = "*";
		if(haku != null && haku.length() > 0) ehto = haku;
		List<Integer> idLista = new ArrayList<Integer>();
		for(Ainesosa a:alkiot) {
			if(WildChars.onkoSamat(a.getAinesosa() + a.getMaara(), ehto) /**|| WildChars.onkoSamat(a.getMaara(), ehto) **/) {
				int nimiId = a.getNimiId();
				if(idLista.contains(nimiId)) continue;
				idLista.add(nimiId);
			}
		}
		return idLista;
	}
	
	/**
	 * @return Nykyisen alkiot-arraylistin koko.
	 * @example
	 * <pre name="test">
	 * Ainesosat ainesosat = new Ainesosat();
	 * ainesosat.getLkm() === 0;
	 * Ainesosa ao1 = new Ainesosa();					Ainesosa ao2 = new Ainesosa();
	 * ainesosat.lisaaAinesosa(ao1);					ainesosat.lisaaAinesosa(ao2);
	 * ainesosat.getLkm() === 2;
	 * </pre>
	 */
	public int getLkm() {
		return alkiot.size();
	}
	
	/**
	 * Lukee oletustiedostosta tiedot.
	 * @throws alkioException poikkeusluokka.
	 */
	public void lueTiedostosta() throws alkioException {
		lueTiedostosta(getTiedostonNimi());
	}
	
	/**
	 * Lukee tiedostosta ainesosia.
	 * @param hakemisto Mist‰ haetaan.
	 * @throws alkioException Poikkeusluokka
	 */
	public void lueTiedostosta(String hakemisto) throws alkioException {
		setOletusnimi(hakemisto);
		String rivi;
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			while((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ( "".equals(rivi)) continue;
				Ainesosa ainesosa = new Ainesosa();
				ainesosa.parse(rivi);
				lisaaAinesosa(ainesosa);
			}
			muutettu = false;
		} catch (FileNotFoundException e) {
			throw new alkioException("Tiedostoa " + getTiedostonNimi() + " ei lˆydy/ei aukea.");
		} catch (IOException e) {
			throw new alkioException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	     }
	
	/**
	 * Muokkaa vanhan ainesosan vastaamaan uusia tietoja.
	 * @param nimiId reseptin nimi-id
	 * @param indeksi mones ainesosa
	 * @param uusiTeksti uusi ainesosan teksti, johon p‰ivitet‰‰n. Sis‰lt‰‰ aineen & m‰‰r‰n.
	 * @example
	 * <pre name="test">
	 * Ainesosat ainesosat = new Ainesosat();
	 * Ainesosa a1 = new Ainesosa(2);		Ainesosa a2 = new Ainesosa(1);		Ainesosa a3 = new Ainesosa(1);	
	 * a1.testiarvot();						a2.testiarvot();					a3.testiarvot();
	 * ainesosat.lisaaAinesosa(a1);			ainesosat.lisaaAinesosa(a2);		ainesosat.lisaaAinesosa(a3);
	 * 
	 * String uusiTeksti = "Hampurilainen 1kpl";
	 * String[] uusiSplit = uusiTeksti.split(" ");
	 * 		
	 * ainesosat.paalleKirjoita(1, 1, uusiSplit);
	 * Ainesosa muokattu = ainesosat.annaAinesosa(2);
	 * String maara = muokattu.getMaara();
	 * maara.equals("1kpl") === true;
	 * muokattu.getAinesosa().equals("Hampurilainen") === true;
	 * </pre>
	 */
	public void paalleKirjoita(int nimiId, int indeksi, String[] uusiTeksti) {
		int monesOikea = 0;
		int monesYhteensa = 0;
		String m‰‰r‰ = "";
		StringBuilder aine = new StringBuilder();
		for(Ainesosa a:alkiot) {
			if(a.getNimiId() == nimiId) {
				if(monesOikea == indeksi) {
					for(int i = 0; i < uusiTeksti.length; i++) {
						if(i == uusiTeksti.length - 1) m‰‰r‰ = uusiTeksti[i];
						else aine.append(uusiTeksti[i]);
					}
					Ainesosa muokattuAinesosa = new Ainesosa(nimiId);
					muokattuAinesosa.setAinesosa(aine.toString());
					muokattuAinesosa.setMaara(m‰‰r‰);
					((ArrayList<Ainesosa>) alkiot).set(monesYhteensa, muokattuAinesosa);
					muutettu = true;
				}
				monesOikea++;
			}
			monesYhteensa++;
		}
	}
	
	/**
	 * @param indeksi mones taulukon ainesosa palautetaan
	 * @return m‰‰r‰tyn indeksin ainesosa
	 * @example
	 * <pre name="test">
	 * Ainesosat ainesosat = new Ainesosat();
	 * Ainesosa a1 = new Ainesosa(1);		Ainesosa a2 = new Ainesosa(2);		Ainesosa a3 = new Ainesosa(3);
	 * ainesosat.lisaaAinesosa(a1);			ainesosat.lisaaAinesosa(a2);		ainesosat.lisaaAinesosa(a3);
	 * 
	 * ainesosat.annaAinesosa(2).getNimiId() === 3;
	 * ainesosat.annaAinesosa(0).getNimiId() === 1;
	 * </pre>
	 */
	public Ainesosa annaAinesosa(int indeksi) {
		int alkioNro = 0;
		for(Ainesosa ao:alkiot) {
			if(alkioNro == indeksi) return ao;
			alkioNro++;
		}
		return null;
	}
	
	/**
	 * Palauttaa kaikki tietyn reseptin ainesosat sen id:n perusteella.
	 * @param id Ainesosan nimi-id
	 * @return Lista, jossa kaikki tietyn id:n ainesosat.
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
	 * Ainesosat ainesosat = new Ainesosat();
	 * 
	 * Ainesosa a1 = new Ainesosa(1);
	 * Ainesosa a2 = new Ainesosa(1);	
	 * Ainesosa a3 = new Ainesosa(2);		
	 * 
	 * ainesosat.lisaaAinesosa(a1);		ainesosat.lisaaAinesosa(a2);		ainesosat.lisaaAinesosa(a3);
	 * List<Ainesosa> tulos1;
	 * List<Ainesosa> tulos2;
	 * tulos1 = ainesosat.annaAinesosat(1);
	 * tulos2 = ainesosat.annaAinesosat(2);
	 * 
	 * tulos1.size() === 2;
	 * tulos2.size() === 1;
	 * </pre>
	 */
	public List<Ainesosa> annaAinesosat(int id) {
		List<Ainesosa> loydetyt = new ArrayList<Ainesosa>();
		for(Ainesosa ao:alkiot) {
			if(ao.getNimiId() == id) loydetyt.add(ao);
		}
		return loydetyt;
	}

	
	/**
	 * @return varmuuskopiona toimivan tiedoston nimi.
	 */
	public String getBakNimi() {
		return tiedostonNimi + ".bak";
	}
	
	
	/**
	 * @return tiedoston nimi.
	 */
	public String getTiedostonNimi() {
		return tiedostonNimi;
	}
	
	/**
	 * Asettaa tiedoston nimen.
	 * @param tNimi Miksi tiedosto nimet‰‰n.
	 */
	public void setOletusnimi(String tNimi) {
		tiedostonNimi = tNimi;
	}

	
	/**
	 * Tallentaa ainesosan.
	 * @throws alkioException poikkeusluokka
	 */
	public void tallenna() throws alkioException {
		if(!muutettu)return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try ( PrintStream fo = new PrintStream(new FileOutputStream(ftied)) ) {
			for(Ainesosa ao:alkiot) {
				fo.println(ao.toString());
			}
		} catch (IOException e) {
			throw new alkioException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia.");
		}
	}
	

	/**
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Ainesosat ainesosat = new Ainesosat();
		
		Ainesosa a1 = new Ainesosa(2);		Ainesosa a2 = new Ainesosa(1);
		a1.testiarvot(); 					a2.testiarvot();
		
		ainesosat.lisaaAinesosa(a1);		ainesosat.lisaaAinesosa(a2);
		List<Ainesosa> ainesosat1 = ainesosat.annaAinesosat(1);
		for(Ainesosa ao: ainesosat1) {
			ao.tulosta(System.out);
		}
	}
}
