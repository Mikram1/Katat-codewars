package kata;


/**
 * Tehtävänanto: https://www.codewars.com/kata/help-the-bookseller/
 * Tallentaa eri kategorioihin kirjoja eri nimillä, ja etsii tietyn alkuiset kirjat kaikista kategorioista.
 * Palauttaa, montako tietyllä kirjaimella alkavaa kirjaa on per kategoria.
 * @author mikko
 * @version 18.9.2017
 */
public class BookSeller {

	/**
	 * Pääohjelma
	 * @param args ei käytÃ¶ssä
	 */
	public static void main(String[] args) {
		String esim[] = new String[]{"ABAR 200", "ACCC 1", "CDXE 500", "BKWR 250", "BTSQ 890", "DRTY 600"};
		String esimEkakirjain[] = new String[] {"A", "B", "X"};
		String esimEkakirjain2[] = null;
		String esimEkakirjain3[] = new String[] {"", " "};
		String esimEkakirjain4[] = new String[] {"E", "R"};
		System.out.println(stockSummary(esim, esimEkakirjain));
		System.out.println(stockSummary(esim, esimEkakirjain2));
		System.out.println(stockSummary(esim, esimEkakirjain3));
		System.out.println(stockSummary(esim, esimEkakirjain4));
	}
	
	/**
	 * Palauttaa, paljonko tietyn alkuista kirjaa on per kategoria. Plussaa ne siis yhteen kategorian sisällä.
	 * @param lstOfArt Taulukko, jossa kirjan nimi sekä lukumäärä
	 * @param lstOf1stLetter Haettavan kirjan ensimmäinen kirjain
	 * @return montako hakua vastaavaa kirjaa per kategoria
	 */
	public static String stockSummary(String[] lstOfArt, String[] lstOf1stLetter) {
		if(lstOfArt == null || lstOf1stLetter == null) return "";
		int zeroes = 0;
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < lstOf1stLetter.length; i++) {
			int numberOfRights = 0;
			if(lstOf1stLetter[i].isEmpty() || lstOf1stLetter[i].equals(" ")) continue;
			result.append("(" + lstOf1stLetter[i] + " : ");
			for(int j = 0; j < lstOfArt.length; j++) {
				if(lstOfArt[j].startsWith(lstOf1stLetter[i] )) {
					String[] bookAmount = lstOfArt[j].split(" ");
					int number = Integer.parseInt(bookAmount[1]);
					numberOfRights += number;
					}
				}
			result.append(Integer.toString(numberOfRights) + ")");
			if(numberOfRights == 0) zeroes++;
			if(i < lstOf1stLetter.length - 1) result.append(" - ");
			}
		if(zeroes == lstOf1stLetter.length) return "";
		return result.toString();
		}
	}
