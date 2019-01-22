package kata;

/**
 * Teht‰v‰nanto: https://www.codewars.com/kata/598106cb34e205e074000031
 * N‰ytt‰‰ merkkijonon, joka edustaa joukkoa hiiri‰ (O) sek‰ pillipiiparia (P). Teht‰v‰n‰ on selvitt‰‰, kuinka monta 
 * kuuroa hiirt‰ joukossa on - siis kuinka moni hiiri menee pillipiiparista p‰invastaiseen suuntaan. Menosuunnan m‰‰ritt‰‰ hiiren h‰nt‰ (~).
 * Esim. (~O~O~O~O P) => 0 kuuroa hiirt‰
 * 		 (P O~ O~ ~O O~) => 1 kuuro hiiri
 * 		 (~O~O~O~OP~O~OO~) => 2 kuuroa hiirt‰
 * @author Mikko
 * @version 23.9.2017
 *
 */
public class DeafMouse {

	/**
	 * p‰‰ohj.
	 * @param args ei k‰uy√∂
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
	 * Laskee merkkijonosta kuurot rotat. Oletetaan, ett‰ k‰yt√∂ss‰ on vain merkkej‰ 'O', 'P', ' ' ja '~'.
	 * @param town merkkijono
	 * @return kuurojen lukum‰‰r‰
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
