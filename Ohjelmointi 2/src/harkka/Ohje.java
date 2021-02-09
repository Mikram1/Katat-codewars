package harkka;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Luokka ohjetekstien luomiseen & käsittelyyn.
 */
public class Ohje {

	private int ohjeId;
	private int nimiId;
	private String ohje;
	private static int nextId = 1;
	
	/**
	 * @return ohje
	 */
	public String getOhje() {
		return ohje;
	}
	
	/**
	 * Muokkaa ohjetta.
	 * @param uusiOhje muokattu ohje. 
	 */
	public void setOhje(String uusiOhje) {
		this.ohje = uusiOhje;
	}
	/**
	 * @return nimiId, jotta voitaisiin saada kaikki tietyn reseptin ohjeet.
	 */
	public int getNimiId() {
		return this.nimiId;
	} 
	
	/**
	 * @return ohjeen ohjeid:n. Taitaa olla aika turha.
	 */
	public int getOhjeid() {
		return this.ohjeId;
	}
	
	/**
	 * konstruktori ilman parametrejä.
	 */
	public Ohje() {}
	
	/**
	 * Liittää ohjeen tiettyyn reseptiin sen nimiId:n perusteella.
	 * @param nimiId Reseptin nimi-id.
	 */
	public Ohje(int nimiId) {
		this.nimiId = nimiId;
	}
	
	/**
	 * Antaa ohjeelle seuraavan ohjeId:n
	 * @example
	 * <pre name="test">
	 * Ohje o1 = new Ohje();		Ohje o2 = new Ohje();
	 * o1.rekisteroi();				o2.rekisteroi();
	 * o1.getOhjeid() === 1;
	 * o2.getOhjeid() === 2;
	 * </pre>
	 */
	public void rekisteroi() {
		ohjeId = nextId;
		nextId++;
	}
	
	/**
	 * Antaa ohjeelle tekstiarvot.
	 */
	public void testiarvot() {
		ohje = "Kirjoita tähän ohje!";
	}
	
	/**
	 * Tulostaa ohjeen tiedot
	 * @param out Ohjeid, ohje.
	 */
	public void tulosta(PrintStream out) {
		//out.println(ohjeId);
		out.println(ohje);
	}
	
	@Override
	public String toString() {
		return "" + nimiId + "|" + ohje;
	}
	
	/**
	 * Erottaa yksittäiset tiedot palkein erotellusta merkkijonosta.
	 * @param rivi luettava rivi
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		nimiId = Mjonot.erota(sb, '|', nimiId);
		ohje = Mjonot.erota(sb, '|', ohje);
	}
	
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Ohje o1 = new Ohje();
		o1.rekisteroi();
		o1.testiarvot();
		o1.tulosta(System.out);
		Ohje o2 = new Ohje();
		o2.rekisteroi();
		o2.testiarvot();
		o2.tulosta(System.out);
	}
}
