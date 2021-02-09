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
import java.util.Collections;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Luokka nimien tallettamista taulukkoon varten. Tallettaa reseptien tietoja.
 */
public class Nimet {
	private int MAX_RESEPTIT = 50;					// Ettei tule lista täyteen.
	private int nimetLkm = 0;								// Pitää kirjaa nykyisen taulukon koosta.
	private Nimi alkiot[] = new Nimi[MAX_RESEPTIT];			// Varsinainen nimitaulukko.
	private String oletusNimi = "nimet";					// Kansion oletusnimi tallennusta varten.
	private boolean muutettu = false;						// Pitää kirjaa siitä, onko tietoja muutettu => tarvitseeko tallentaa.
	
	/**
	 * Peruskonstruktori
	 */
	public Nimet() {}
	
	/**
	 * Muodostaja, jossa määritelty taulukon koko. (Testejä varten)
	 * @param koko taulukon koko
	 */
	public Nimet(int koko) {
		MAX_RESEPTIT = koko;
		alkiot = new Nimi[koko];
	}
	
	
	/**
	 * Lisää uuden reseptin nimen & tietoja taulukkoon.
	 * @param nimi Käsiteltävä nimi 
	 * @throws alkioException Poikkeusluokka
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Nimi n1 = new Nimi();	Nimi n2 = new Nimi();
	 * Nimet nimet = new Nimet();
	 * nimet.getLkm() === 0;
	 * nimet.lisaaNimi(n1);		nimet.getLkm() === 1;
	 * nimet.lisaaNimi(n2);		nimet.getLkm() === 2;
	 * </pre>
	 */
	public void lisaaNimi(Nimi nimi) throws alkioException {
		if(nimetLkm >= alkiot.length) {
			Dialogs.showMessageDialog("Tila loppu!");
			return;
		}
		alkiot[nimetLkm] = nimi;
		nimetLkm++;
		muutettu = true;
	}
	
	/**
	 * Poistaa valitun id:n reseptin
	 * @param id reseptin nimi-id
	 * @return true jos onnistui, false jos ei
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Nimet nimet = new Nimet(3);
	 * Nimi n1 = new Nimi();		n1.rekisteroi();
	 * Nimi n2 = new Nimi();		n2.rekisteroi();
	 * Nimi n3 = new Nimi();		n3.rekisteroi();
	 * nimet.lisaaNimi(n1);			nimet.lisaaNimi(n2);		nimet.lisaaNimi(n3);
	 * 
	 * int tokaInd = n2.getId();
	 * nimet.poistaNimi(tokaInd) === true;
	 * nimet.getLkm() === 2;
	 * </pre>
	 */
	public boolean poistaNimi(int id) {
		int paikka = etsiNimi(id);
		if(paikka == -1) return false;
		nimetLkm--;
		for(int i = paikka; i < nimetLkm; i++) {
			alkiot[i] = alkiot[(i + 1)];
		}
		alkiot[nimetLkm] = null;
		muutettu = true;
		return true;
	}
	
	/**
	 * Palauttaa listan nimiä, jotka vastaavat hakuehtoa.
	 * @param haku hakuehto
	 * @param cbEhto choiceBoxin ehto => minkä perusteella lajitellaan nimet.
	 * @return lista nimiä
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * #import java.util.List;
	 * Nimet nimet = new Nimet();
	 * Nimi n1 = new Nimi();		n1.parse("1|Kana & Riisi|20 min|3|3e");
	 * Nimi n2 = new Nimi();		n2.parse("2|Pinaattikeitto|10 min|2|2e");
	 * Nimi n3 = new Nimi();		n3.parse("3|Jauhelihakastike|30 min|3|3e");
	 * nimet.lisaaNimi(n1);			nimet.lisaaNimi(n2);		nimet.lisaaNimi(n3);
	 * 
	 * List<Nimi> löytyneet;
	 * löytyneet = (List<Nimi>)nimet.etsi("*pin*", "Nimi");
	 * löytyneet.size() === 1;
	 * löytyneet = (List<Nimi>)nimet.etsi("*kastike*", "Nimi");
	 * löytyneet.size() === 1;
	 * löytyneet = (List<Nimi>)nimet.etsi("*na*", "Nimi");
	 * löytyneet.size() === 2;
	 * </pre>
	 */
	public Collection<Nimi> etsi(String haku, String cbEhto) {
		String ehto = "*";
		if(haku != null && haku.length() > 0) ehto = haku;
		List<Nimi> löytyneet = new ArrayList<Nimi>();
		for(int i = 0; i < nimetLkm; i++) {
			if(WildChars.onkoSamat(alkiot[i].getNimi(), ehto)) löytyneet.add(alkiot[i]);
		}
		Collections.sort(löytyneet, new Nimi.Vertailija(cbEhto));
		return löytyneet;
	}
	
	/**
	 * Palauttaa nimitaulukon, joka koostuu annettujen id:ien omaavista resepteistä.
	 * @param idLista lista selvitettäviä id:tä
	 * @param cbEhto ehto, jolla lista lajitellaan
	 * @return lista nimiä, jotka vastaavat hakua
	 */
	public Collection<Nimi> haeNimet(List<Integer> idLista, String cbEhto) {
		List<Nimi> löytyneet = new ArrayList<Nimi>();
		for(int id:idLista) {
			int paikka = etsiNimi(id);
			löytyneet.add(alkiot[paikka]);
		}
		Collections.sort(löytyneet, new Nimi.Vertailija(cbEhto));
		return löytyneet;
	}
	
	
	/**
	 * Etsii tietyn reseptin paikan taulukossa sen id:n avulla.
	 * @param nimiId reseptin id
	 * @return reseptin paikka.
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Nimet nimet = new Nimet(4);
	 * Nimi n1 = new Nimi();		n1.rekisteroi();
	 * Nimi n2 = new Nimi();		n2.rekisteroi();
	 * Nimi n3 = new Nimi();		n3.rekisteroi();
	 * nimet.lisaaNimi(n1);			nimet.lisaaNimi(n2);		nimet.lisaaNimi(n3);
	 * 
	 * int ekaInd = n1.getId();
	 * nimet.etsiNimi(ekaInd + 1) === 1;					
	 * nimet.etsiNimi(ekaInd + 2) === 2;
	 * </pre>
	 */
	public int etsiNimi(int nimiId) {
		for(int i = 0; i < nimetLkm; i++) {
			if(alkiot[i].getId() == nimiId) return i;
		}
		return -1;
	}
	
	/**
	 * Palauttaa alkioiden lukumäärän nimet-oliotaulukossa.
	 * @return alkioiden lukumäärä
	 */
	public int getLkm()  {
		return this.nimetLkm;
	}
	
	/**
	 * Lukee oletustiedostosta tiedot.
	 * @throws alkioException poikkeusluokka
	 */
	public void lueTiedostosta() throws alkioException {
		lueTiedostosta(getTiedostonNimi());
	}
	
	/**
	 * Lukee tiedostosta nimiä. Ohjelma toimii, mutta testit ei...
	 * @param hakemisto tiedosto, johon tallennetaan.
	 * @throws alkioException poikkeusluokka
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * #import java.io.File;
	 * 
	 * String hakemisto = "testihakemistoN";
	 * String tiedosto = hakemisto + "/nimetTesti";
	 * File nimiTiedosto = new File(tiedosto + ".dat");
	 * File dir = new File(hakemisto);
	 * dir.mkdir();
	 * nimiTiedosto.delete();
	 * 
	 * Nimet nimet = new Nimet(2);
	 * Nimi n1 = new Nimi();			n1.rekisteroi();		n1.testiarvot();
	 * Nimi n2 = new Nimi();			n2.rekisteroi();		n2.testiarvot();
	 * nimet.lisaaNimi(n1);				nimet.lisaaNimi(n2);
	 * nimet.setOletusnimi(tiedosto);
	 * nimet.tallenna();
	 * 
	 * nimet = new Nimet();
	 * nimet.lueTiedostosta(tiedosto);
	 * Nimi nimi1 = nimet.annaNimi(0);
	 * nimi1.getId() === n1.getId();
	 * </pre>
	 */
	public void lueTiedostosta(String hakemisto) throws alkioException {
		setOletusnimi(hakemisto);
		String rivi;
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			while((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ( "".equals(rivi)) continue;
				Nimi nimi = new Nimi();
				nimi.parse(rivi);
				lisaaNimi(nimi);
			}
			muutettu = false;
		} catch (FileNotFoundException e) {
			throw new alkioException("Tiedostoa " + getTiedostonNimi() + " ei löydy/ei aukea.");
		} catch (IOException e) {
			throw new alkioException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}
	
	/**
	 * Tallentaa nimet oletusnimitiedostoon.
	 * @throws alkioException poikkeusluokka
	 */
	public void tallenna() throws alkioException {
		if(!muutettu) return;								// Jos tiedostoa ei ole muutettu, ei tallenneta.
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		if(fbak.exists() == true) System.out.println("Tiedoston poistaminen ei onnistunut.");
		ftied.renameTo(fbak);
		
		try(PrintStream fo = new PrintStream(new FileOutputStream(ftied))) {
			for(int i = 0; i < this.getLkm(); i++) {
				Nimi nimi = this.alkiot[i];
				fo.println(nimi.toString());
			}
		} catch (IOException e) {
			throw new alkioException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia.");
		}
	}

	/**
	 * @return varmuuskopiona toimivan tiedoston nimi.
	 */
	public String getBakNimi() {
		return oletusNimi + ".bak";
	}
	
	/**
	 * @return tiedoston nimi. (oletusNimi)
	 */
	private String getTiedostonNimi() {
		return oletusNimi;
	}

	/**
	 * Asettaa tiedoston perusnimen.
	 * @param tNimi tiedoston perusnimi (ei tarkenninta)
	 */
	public void setOletusnimi(String tNimi) {
		oletusNimi = tNimi;
	}
	
	/**
	 * Palauttaa nimet-taulukossa paikan i nimen.
	 * @param i Mones nimi
	 * @return nimi
	 * @example
	 * <pre name="test">
	 * #THROWS alkioException
	 * Nimet nimet = new Nimet(2);
	 * Nimi nimi1 = new Nimi();
	 * nimi1.rekisteroi();			nimi1.testiarvot();
	 * nimet.lisaaNimi(nimi1);
	 * nimet.annaNimi(0) === nimi1;
	 * Nimi nimi2 = new Nimi();		nimi2.rekisteroi();		nimi2.testiarvot();
	 * nimet.lisaaNimi(nimi2);
	 * nimet.annaNimi(1) === nimi2;
	 * </pre>
	 */
	public Nimi annaNimi(int i) {
		return alkiot[i];
	}
	
	/**
	 * Päällekirjoittaa oikean reseptin tiedot muokatuilla tiedoilla.
	 * @param NimiId Muokattavan reseptin tunnus
	 * @param resNimi Uusi reseptin nimi
	 * @param vaikeusaste Uusi vaikeusaste
	 * @param tekoAika Uusi tekoaika
	 * @param hinta Uusi hinta
	 */
	public void paalleKirjoita(int NimiId, String resNimi, String tekoAika, String vaikeusaste, String hinta) {
		Nimi nimi;
		for(int i = 0; i < nimetLkm; i++) {
			nimi = alkiot[i];
			if(nimi.getId() == NimiId) {
				if(!resNimi.contains("|"))nimi.setNimi(resNimi);
				if(!vaikeusaste.contains("|") && tryParse(vaikeusaste))nimi.setVaikeusaste(vaikeusaste);
				if(!tekoAika.contains("|"))nimi.setTekoaika(tekoAika);
				if(!hinta.contains("|"))nimi.setHinta(hinta);
				alkiot[i] = nimi;
				muutettu = true;
				return;
			}
		}
	}
	
	/**
	 * Testaa, voiko kentän tietoja tulkita kokonaisluvuksi.
	 * @param s Merkkijono kentän tiedoista
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
	 * Tulostaa nimet-taulukon jäsenet.
	 */
	public void tulosta() {
		for(int i = 0; i < this.getLkm(); i++) {
			Nimi testi = this.annaNimi(i);
			testi.tulosta(System.out);
		}
	}
	
	/**
	 * @param args Ei käytössä.
	 */
	public static void main(String[] args) {
		Nimet nimet = new Nimet();		
		Nimi res1 = new Nimi();			Nimi res2 = new Nimi();
		res1.rekisteroi();		res1.testiarvot();
		res2.rekisteroi();		res2.testiarvot();
		try {
			nimet.lisaaNimi(res1);
			nimet.lisaaNimi(res2);
			nimet.tulosta();
		} catch (alkioException e) {
			System.out.println(e.getMessage());
		} 
	}	
}
