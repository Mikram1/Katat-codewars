package kata;

/**
 * Tehtävänanto: https://www.codewars.com/kata/598106cb34e205e074000031
 * Näyttää merkkijonon, joka edustaa joukkoa hiiriä (O) sekä pillipiiparia (P). Tehtävänä on selvittää, kuinka monta 
 * kuuroa hiirtä joukossa on - siis kuinka moni hiiri menee pillipiiparista päinvastaiseen suuntaan. Menosuunnan määrittää hiiren häntä (~).
 * Esim. (~O~O~O~O P) => 0 kuuroa hiirtä
 * 		 (P O~ O~ ~O O~) => 1 kuuro hiiri
 * 		 (~O~O~O~OP~O~OO~) => 2 kuuroa hiirtä
 * @author Mikko
 * @version 23.9.2017
 *
 */
public class DeafMouse {

	/**
	 * pääohj.
	 * @param args ei käuyÃ¶
	 */
	public static void main(String[] args) {
		String esim1 = "~O~O~O~O P";
		String esim2 = "P O~ O~ ~O O~";
		String esim3 = "~O~O~O~OP~O~OO~";
		System.out.println(countDeafRats(esim1));
		System.out.println(countDeafRats(esim2));
		System.out.println(countDeafRats(esim3));
	}
	
	/**
	 * Laskee merkkijonosta kuurot rotat. Oletetaan, että käytÃ¶ssä on vain merkkejä 'O', 'P', ' ' ja '~'.
	 * @param town merkkijono
	 * @return kuurojen lukumäärä
	 */
	public static int countDeafRats(final String town) {
		boolean piperPlace = false;
		boolean partMouse = false;
		int deafRats = 0;
		String modTown = town.replaceAll(" ", "");
	    for(int i = 0; i < modTown.length(); i++) {
	    	char currentC = modTown.charAt(i);
	    	if((currentC == 'O' || currentC == '~') && partMouse == false) {
	    		partMouse = true;
	    		continue;
	    	}
	    	if(modTown.charAt(i) == 'P') {
	    		piperPlace = true;
	    		continue;
	    	}
	    	if(currentC == 'O' && partMouse == true) {			//The mouse is heading right!
	    		partMouse = false;
	    		if(piperPlace == true) deafRats++;
	    		continue;
	    	}
	    	if(currentC == '~' && partMouse == true) {			//The mouse is heading left!
	    		partMouse = false;
	    		if(piperPlace == false) deafRats++;
	    		continue;
	    	}
	    }
	    return deafRats;
	  }

}
