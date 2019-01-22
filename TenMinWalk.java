package walk;

/**
 * Teht�v�nanto: https://www.codewars.com/kata/54da539698b8a2ad76000228
 * Annetaan char-taulukko ilmansuunnista (n,s,e,w), joista jokainen yksitt�inen ilmansuunta vie yhden minuutin. Palauttaa true tai false sen mukaan, viek� matka
 * tasan 10 min JA p��tyyk� takaisin aloituspisteeseen.
 * Esim. ['n','s','n','s','n','s','n','s','n','s'] => true
 * 		 ['n','s','e','w'] => false
 * 		 ['n','n','n','n','n','n','n','n','n','n'] => false
 * @author mikko_000
 */

public class TenMinWalk {

	public static void main(String[] args) {
		System.out.println(isValid(new char[] {'n','s','n','s','n','s','n','s','n','s'})); 				//10 min + aloituspiste
		System.out.println(isValid(new char[] {'w','e','w','e','w','e','w','e','w','e','w','e'}));		//>10 min + aloituspiste
		System.out.println(isValid(new char[] {'n','n','n','n','n','n','n','n','n','n'}));				//10 min - aloituspiste
		System.out.println(isValid(new char[] {'n','n','n','n','n','n','n'}));							//<10 min - aloituspiste
	}
	
	/**
	 * Selvitt��, viek� matka 10 min ja palaako k�velij� takaisin alkupisteeseens�.
	 * @param walk K�velt�v�t ilmansuunnat (oletetaan, ett� vaan [n,s,e,w] ovat k�yt�ss�)
	 * @return true, jos k�vely vie 10 min ja palaa takaisin alkupisteeseen. Muuten false.
	 */
	public static boolean isValid(char[] walk) {
	    if(!(walk.length == 10)) return false;
	    int n = 0;
	    int s = 0;
	    int w = 0;
	    int e = 0;
	    for (int i = 0; i < walk.length; i++) {
	    	switch (walk[i]) {
	    		case('n'): n++;
	    		continue;
	    		case('s'): s++;
	    		continue;
	    		case('e'): e++;
	    		continue;
	    		case('w'): w++;
	    		continue;
	    		default: continue;
	    	}
	    }
	    if(n-s != 0) return false;
	    if(w-e != 0) return false;
	    return true;
	}
}
