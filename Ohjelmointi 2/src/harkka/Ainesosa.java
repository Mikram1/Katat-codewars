package harkka;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Luokka ainesosien lis��miselle yms.
 */
public class Ainesosa {

	private int aineId;
	private int nimiId;
	private String ainesosa;
	private String maara;
	private static int nextId = 1;
	
	/**
	 * Liitt�� ohjeen tiettyyn reseptiin sen nimiId:n perusteella.
	 * @param nimiId Reseptin nimi-id
	 */
	public Ainesosa(int nimiId) {
		this.nimiId = nimiId;
	}
	
	/**
	 * Konstruktori ilman parametrej�.
	 */
	public Ainesosa() {}
	
	/**
	 * Palauttaa nykyisen reseptin ainesosan.
	 * @return ainesosa
	 */
	public String getAinesosa() {
		return ainesosa;
	}
	
	/**
	 * @param uusi muokattu ainesosa.
	 */
	public void setAinesosa(String uusi) {
		this.ainesosa = uusi;
	}
	
	/**
	 * @return m��r� ainesosaa
	 */
	public String getMaara() {
		return maara;
	}
	
	/**
	 * @param maara asetettava m��r�
	 */
	public void setMaara(String maara) {
		this.maara = maara;
	}
	/**
	 * @return nimiId, jotta voitaisiin saada kaikki tietyn reseptin ainesosat.
	 */
	public int getNimiId() {
		return this.nimiId;
	}
	
	/**
	 * Asettaa k�sitelt�v�lle ainesosalle oikean id:n. K�ytet��n tiedostoa luettaessa.
	 * @param nro Asetettava id.
	 */
	public void setNimiId(int nro) {
		nimiId = nro;
		if(nimiId >= nextId) nextId = nimiId + 1;
	}
	
	/**
	 * Muuntaa ainesosan merkkijonoksi tallettamista varten.
	 */
	@Override
	public String toString() {
		return "" + nimiId + "|" + ainesosa + "|" + maara;
	}
	
	/**
	 * Erottaa ainesosan tiedot palkein erotellusta merkkijonosta.
	 * @param rivi (Tiedoston) rivi, jota tulkitaan.
	 * @example
	 * <pre name="test">
	 * Ainesosa a1 = new Ainesosa();		a1.rekisteroi();		a1.testiarvot();
	 * String s = a1.toString();
	 * Ainesosa a2 = new Ainesosa();
	 * a2.parse(s);
	 * a2.getAinesosa().equals(a1.getAinesosa());			a2.getNimiId() === a1.getNimiId();
	 * </pre>
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setNimiId(Mjonot.erota(sb, '|', getNimiId()));
		ainesosa = Mjonot.erota(sb, '|', ainesosa);
		maara = Mjonot.erota(sb, '|', maara);
	}
	
	/**
	 * Antaa ainesosalle seuraavan aineId:n. (TODO: Tarvitaanko t�t�??)
	 * @return aineId
	 * @example
	 * <pre name="test">
	 * Ainesosa a1 = new Ainesosa();		Ainesosa a2 = new Ainesosa();
	 * int eka = a1.rekisteroi();
	 * a2.rekisteroi() === (eka + 1); 
	 * </pre>
	 */
	public int rekisteroi() {
		aineId = nextId;
		nextId++;
		return aineId;
	}
	
	/**
	 * Antaa testiarvot ainesosalle.
	 */
	public void testiarvot() {
		ainesosa = "Kana";
		maara = "300g";
	}
	
	/**
	 * Tulostaa ainesosan tiedot (id, nimi, m��r�)
	 * @param out tiedot
	 */
	public void tulosta(PrintStream out) {
		out.println(nimiId);
		out.println(ainesosa);
		out.println(maara);
		out.println();
	}
	
	/**
	 * P��ohjelma
	 * @param args ei k�yt�ss�
	 */
	public static void main(String[] args) {
		Ainesosa a1 = new Ainesosa();
		a1.rekisteroi();
		a1.testiarvot();
		a1.tulosta(System.out);
		System.out.println("Tallettamismuodossa: " + a1);
	}
}
