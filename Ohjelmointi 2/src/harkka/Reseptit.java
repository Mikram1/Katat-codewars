package harkka;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * P��luokka ohjelman k�ytt��n.
 */
public class Reseptit {
	private Nimet nimet = new Nimet();
	private Ainesosat ainesosat = new Ainesosat();
	private Ohjeet ohjeet = new Ohjeet();
	
	/**
	 * Lis�� nimen nimet-taulukkoon.
	 * @param nimi lis�tt�v� nimi
	 * @throws alkioException poikkeusluokka
	 */
	public void lisaa(Nimi nimi) throws alkioException {
		this.nimet.lisaaNimi(nimi);
	}
	
	/**
	 * Lis�� ainesosan ainesosat-taulukkoon.
	 * @param ainesosa lis�tt�v� ainesosa
	 */
	public void lisaa(Ainesosa ainesosa) {
		ainesosat.lisaaAinesosa(ainesosa);
	}
	
	/**
	 * @return reseptin nimitaulukon.
	 */
	public Nimet getNimet() {
		return this.nimet;
	}
	
	/**
	 * @return reseptin ohjetaulukon.
	 */
	public Ohjeet getOhjeet() {
		return this.ohjeet;
	}
	/**
	 * @return Montako nime� (=resepti�) nykyisess� listassa on.
	 */
	public int getNimetlkm() {
		return(nimet.getLkm());
	}
	/**
	 * Lis�� ohjeen ohjeet-taulukkoon.
	 * @param ohje lis�tt�v� ohje
	 * @throws alkioException poikkeusluokka
	 */
	public void lisaa(Ohje ohje) throws alkioException {
		ohjeet.lisaaOhje(ohje);
	}

	/**
	 * @param i Mones nimi
	 * @return Palauttaa nimet-taulukossa paikan i nimen. 
	 */
	public Nimi annaNimi(int i) {
		return nimet.annaNimi(i);
	}
	
	/**
	 * Antaa eri tiedostoille nimet niit� luettaessa.
	 * @param nimi miksi (p��)tiedosto nimet��n.
	 */
	public void setTiedosto(String nimi) {
		File hakemisto = new File(nimi);
		hakemisto.mkdirs();
		String hakemistonNimi = "";
		if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
		nimet.setOletusnimi(hakemistonNimi + "/nimet");
		ainesosat.setOletusnimi(hakemistonNimi + "/ainesosat");
		ohjeet.setTiedostonNimi(hakemistonNimi + "/ohjeet");
	}
	
	/**
	 * Lukee reseptien tiedot omista tiedostoistaan.
	 * @param nimi Tiedoston nimi.
	 * @throws alkioException poikkeusluokka.
	 */
	public void lueTiedostosta(String nimi) throws alkioException {
		setTiedosto(nimi);
		
		nimet = new Nimet();
		ainesosat = new Ainesosat();
		ohjeet = new Ohjeet();
		
		nimet.lueTiedostosta();
		ainesosat.lueTiedostosta();
		ohjeet.lueTiedostosta();
	}
	
	/**
	 * P��llekirjoittaa muokatun ohjeen oikeilla tiedoilla. Toteutus ohjeissa.
	 * @param nimiId reseptin nimi-id
	 * @param indeksi mones ohje muokataan
	 * @param uusiTeksti ohjeen uusi teksti
	 */
	public void paalleKirjoitaOhje(int nimiId, int indeksi, String uusiTeksti) {
		this.ohjeet.paalleKirjoita(nimiId, indeksi, uusiTeksti);
	}
	
	/**
	 * P��llekirjoittaa muokatun ainesosan oikeilla tiedoilla. Toteutus aineosissa.
	 * @param nimiId reseptin nimi-id
	 * @param indeksi mones ainesosa muokataan
	 * @param uusiTeksti ainesosan uudet tekstit(aine + m��r�)
	 */
	public void paalleKirjoitaAinesosa(int nimiId, int indeksi, String[] uusiTeksti) {
		this.ainesosat.paalleKirjoita(nimiId, indeksi, uusiTeksti);
	}

	/**
	 * P��llekirjoittaa muokatut reseptin tiedot. Toteutus nimiss�.
	 * @param NimiId Reseptin nimi-id
	 * @param resNimi Reseptin nimi
	 * @param tekoaika tekoaika
	 * @param vaikeusaste vaikeusaste 
	 * @param hinta hinta
	 */
	public void paalleKirjoitaNimet(int NimiId, String resNimi, String tekoaika, String vaikeusaste, String hinta) {
		this.nimet.paalleKirjoita(NimiId, resNimi, tekoaika, vaikeusaste, hinta);
	}
	
	/**
	 * Poistaa kaikki valitun reseptin tiedot
	 * @param nimiId Poistettavan nimi-id.
	 */
	public void poistaResepti(int nimiId) {
		this.nimet.poistaNimi(nimiId);
		this.ohjeet.poistaOhjeet(nimiId);
		this.ainesosat.poistaAinesosat(nimiId);
	}
	
	/**
	 * Hakee hakua vastaavan listan reseptej�.
	 * @param haku hakukent�n teksti
	 * @param cbEhto choiceBoxin ehto => mink� mukaan lajitellaan reseptit.
	 * @return hakua vastaava lista reseptej�.
	 */
	public Collection<Nimi> haeNimista(String haku, String cbEhto) {
		return this.nimet.etsi(haku, cbEhto);
	}
	
	/**
	 * Hakee ensin ainesosia, jotka vastaavat hakua, ja palauttaa listan ainesosien resepteist�.
	 * @param haku Hakukentt��n sy�tetty teksti
	 * @param cbEhto ehto, jolla saadut tulokset lajitellaan.
	 * @return lista nimi�, jotka vastaavat hakua.
	 */
	public Collection<Nimi> haeAinesosista(String haku, String cbEhto) {
		List<Integer>idLista = this.ainesosat.etsi(haku);
		return this.nimet.haeNimet(idLista, cbEhto);
	}
	
	/**
	 * Tallentaa tiedot.
	 * @throws alkioException poikkeusluokka.
	 */
	public void tallenna() throws alkioException {
		StringBuilder virhe = new StringBuilder("");
		try {
			nimet.tallenna();
		} catch(alkioException e) {
			virhe.append(e);
		}
		try {
			ainesosat.tallenna();
		} catch(alkioException e) {
			virhe.append(e);
		}
		try {
			ohjeet.tallenna();
		} catch(alkioException e) {
			virhe.append(e);
		}
		String svirhe = virhe.toString();
		if(!"".equals(svirhe)) throw new alkioException(svirhe);
	}
	
	/**
	 * @param args ei k�yt�ss�
	 */
	public static void main(String[] args) {
		Reseptit reseptit = new Reseptit();
		Nimi nimi1 = new Nimi();		Nimi nimi2 = new Nimi();
		nimi1.rekisteroi();				nimi1.testiarvot();
		nimi2.rekisteroi();				nimi2.testiarvot();
		
		Ainesosa a1 = new Ainesosa(1);		a1.testiarvot();
		Ainesosa a2 = new Ainesosa(1);		a2.testiarvot();
		
		Ohje o1 = new Ohje(nimi1.getId());				o1.testiarvot();
		Ohje o2 = new Ohje(nimi1.getId());				o2.testiarvot();
		try {
			reseptit.lisaa(nimi1);
			reseptit.lisaa(nimi2);
			reseptit.lisaa(a1);
			reseptit.lisaa(a2);
			reseptit.lisaa(o1);
			reseptit.lisaa(o2);
		} catch (alkioException e) {
			System.out.println(e.getMessage());
		}
	}
}
