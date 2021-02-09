package harkka;

import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Mipetoit
 * @version 1.4.2016
 * Luokka nimien k‰sittelyyn
 */
public class Nimi {

	private int nimiId;					//Id nimelle. Linkittyy muihin luokkiin.
	private String nimi;				
	private String tekoaika;
	private int vaikeusaste;
	private String hinta;
	private static int nextId = 1;		//Pit‰‰ kirjaa seuraavan lis‰tt‰v‰n nimen id:st‰.
	
	/**
	 * Luokka nimien vertailuun & lajitteluun eri ehtojen mukaan.
	 * @author Mipetoit
	 * @version 17.4.2016
	 */
	public static class Vertailija implements Comparator<Nimi> {
		private final String ehto;
		
		/**
		 * Konstruktori.
		 * @param ehto vertailtava ehto. (Vaikeusaste, hinta, nimi, tekoaika.)
		 */
		public Vertailija(String ehto) {
			this.ehto = ehto;
		}
		
		@Override
		public int compare(Nimi nimi1, Nimi nimi2) {
			if(ehto.equals("Nimi")) {
				String s1 = nimi1.getNimi();
				String s2 = nimi2.getNimi();
				return s1.compareTo(s2);
			}
			if(ehto.equals("Vaikeustaso")) {
				String s1 = nimi1.getVaikeusaste();
				String s2 = nimi2.getVaikeusaste();
				return s1.compareTo(s2);
			}
			if(ehto.equals("Hinta")) {
				String s1 = nimi1.getHinta();
				String s2 = nimi2.getHinta();
				return s1.compareTo(s2);
			}
			if(ehto.equals("Aika")) {
				String s1 = nimi1.getTekoaika();
				String s2 = nimi2.getTekoaika();
				return s1.compareTo(s2);
			}
			return 0;
		}
		
	}
	
	
	/**
	 * @return Reseptin nimi
	 */
	public String getNimi() {
		return nimi;
	}
	
	/**
	 * Asettaa uuden arvon nimelle.
 	 * @param uusi Uusi arvo
	 */
	public void setNimi(String uusi) {
		this.nimi = uusi;
	}
	
	/**
	 * @return nimen nimi-id.
	 */
	public int getId() {
		return nimiId;
	}
	
	/**
	 * Tallettaa uuden vaikeusasteen muokatun tekstin mukaan. TODO: virheenk‰sittely.
	 * @param uusi uusi vaikeusaste
	 */
	public void setVaikeusaste(String uusi) {
		int uusiV = Integer.parseInt(uusi);
		this.vaikeusaste = uusiV;
	}
	
	/**
	 * @return vaikeusaste
	 */
	public String getVaikeusaste() {
		StringBuilder vaikeusAste = new StringBuilder();
		vaikeusAste.append("");	
		vaikeusAste.append(this.vaikeusaste);
		return vaikeusAste.toString();
	}
	
	/**
	 * Tallettaa uuden tekoajan muokatun tekstin mukaan.
	 * @param uusi uusi tekoaika
	 */
	public void setTekoaika(String uusi) {
		this.tekoaika = uusi;
	}
	
	/**
	 * @return tekoaika
	 */
	public String getTekoaika() {
		return this.tekoaika;
	}
	
	/**
	 * Tallettaa uuden hinnan muokatun tekstin mukaan.
	 * @param uusi uusi hinta
	 */
	public void setHinta(String uusi) {
		this.hinta = uusi;
	}
	
	/**
	 * @return hinta
	 */
	public String getHinta() {
		return this.hinta;
	}
	
	/**
	 * Antaa reseptille testiarvot.
	 */
	public void testiarvot() {
		nimi = "Uusi Resepti";
		tekoaika = "5-15 min";
		vaikeusaste = 3;
		hinta = "2-4e";
	}
	
	/**
	 * Asettaa nimi-id:n ja varmistaa, ett‰ seuraava id on suurempi.
	 * K‰ytet‰‰n tiedoston lukemisessa.
	 * @param nro id, joka asetetaan.
	 */
	public void setId(int nro) {
		nimiId = nro;
		if(nimiId >= nextId) nextId = nimiId + 1;
	}
	
	/**
	 * Antaa reseptille seuraavan nimi-id:n. Jos reseptill‰ on jo nimi-id, ei tee mit‰‰n.
	 * @example
	 * <pre name="test">
	 * Nimi n1 = new Nimi();	Nimi n2 = new Nimi();	
	 * n1.rekisteroi();			n2.rekisteroi();
	 * int ekaind = n1.getId();
	 * n2.getId() === (n1.getId() + 1);
	 * n1.rekisteroi();
	 * n1.getId() === ekaind;
	 * </pre>
	 */
	public void rekisteroi() {
		if(this.nimiId > 0) return;
		nimiId = nextId;
		nextId++;
	}
	
	
	/**
	 * Palauttaa nimen tiedot |-erotettuna merkkijonona.
	 * @example
	 * <pre name="test">
	 * Nimi nimi1 = new Nimi();
	 * nimi1.parse("2 | Lihakastike| 25min |3| 4e");
	 * nimi1.getNimi().equals("Lihakastike");
	 * nimi1.getId() === 2;  
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + getId() + "|" + nimi + "|" + tekoaika + "|" + vaikeusaste + "|" + hinta;
	}
	
	/**
	 * Erottaa yksitt‰iset tiedot palkein erotellusta merkkijonosta.
	 * @param rivi Rivi, josta selvitet‰‰n tiedot.
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setId(Mjonot.erota(sb, '|', getId()));
		nimi = Mjonot.erota(sb, '|', nimi);
		tekoaika = Mjonot.erota(sb, '|', tekoaika);
		vaikeusaste = Mjonot.erota(sb, '|', vaikeusaste);
		hinta = Mjonot.erota(sb, '|', hinta);
	}
	
	/**
	 * Tulostaa reseptin tiedot.
	 * @param out tietovirta
	 */
	public void tulosta(PrintStream out) {
		out.println(nimiId + " " + nimi);
		out.println("Tekoaika: " + tekoaika);
		out.println("Vaikeusaste: " + vaikeusaste);
		out.println("Hinta: " + hinta);
		out.println();
	}
	
	/**
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Nimi r1 = new Nimi();
		r1.rekisteroi();
		r1.testiarvot();
		r1.tulosta(System.out);
		
		Nimi r2 = new Nimi();
		r2.rekisteroi();
		r2.testiarvot();
		r2.tulosta(System.out);
		
		System.out.println("Tallettamismuodossa: " + r1);
		System.out.println("Tallettamismuodossa: " + r2);
	}

}
