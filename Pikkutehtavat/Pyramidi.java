package kata;

/**
 * Tehtävänanto: https://www.codewars.com/kata/5797d1a9c38ec2de1f00017b
 * Tekee annetusta merkkijonosta pyramidin (jokaiselle merkille oma kerros) ja näyttää sen sivulta & päältä sekä
 * ilmoittaa käytettyjen "palikoiden" kokonaismäärän ja nähtävän määrän.
 * 6 KYU!! Sain tällä rankin :)
 * @author Maikki
 * @version 12.4.2017
 */
public class Pyramidi {

	/**
	 * Pääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		System.out.println(watchPyramidFromTheSide("abcde"));
		System.out.println();
		System.out.println(watchPyramidFromAbove("abcde"));
		System.out.println();
		System.out.println("Näkyvät palikat: " + countVisibleCharactersOfThePyramid("abcde"));
		System.out.println("Palikoita yhteensä: " + countAllCharactersOfThePyramid("abcde"));
		System.out.println();
		System.out.println(watchPyramidFromTheSide("*#"));
		System.out.println(watchPyramidFromTheSide(null));
		System.out.println(watchPyramidFromAbove("0123456789"));
		System.out.println(watchPyramidFromTheSide("0123456789"));
	}

	/**
	 * Näyttää pyramidin sivulta. 
	 * Esim. "abc" =>     c
	 * 					 bbb
	 *                  aaaaa
	 * @param characters merkkijono, josta tehdään pyramidi
	 * @return pyramidi sivulta
	 */
	public static String watchPyramidFromTheSide(String characters) {
	    if(characters == null || characters.isEmpty()) return characters;	
		int count = 1;
	    int insertPoint = characters.length() - 1; 
	    StringBuilder result = new StringBuilder();
		for(int i = characters.length() - 1; i >= 0; i--) {
	    	for(int p = 0; p != insertPoint; p++) result.append(" ");
	    	for(int j = 0; j < count; j++) result.insert(result.length(), characters.charAt(i));
	    	for(int p = 0; p!= insertPoint; p++) result.append(" ");
	    	count += 2;
	    	insertPoint -= 1;
	    	if(i != 0)result.append("\n");
	    }
		return result.toString();
	  }

	/**
	 * Näyttää pyramidin ylhäältäpäin.
	 * Esim. abc =>    aaaaa
	 * 				   abbba
	 * 				   abcba
	 * 				   abbba
	 * 				   aaaaa
	 * @param characters merkkijono, josta tehdään pyramidi
	 * @return pyramidi sivulta
	 */
	 public static String watchPyramidFromAbove(String characters) {
	   if(characters == null || characters.isEmpty()) return characters; 	
	   int widthHeight = (characters.length() *2) - 1;
	   int differentCount = 0;
	   StringBuilder result = new StringBuilder();
	   for(int i = 0; i < (widthHeight / 2) + 1; i++) {
		   for(int p = 0; p < differentCount; p++) result.insert(result.length(), characters.charAt(p));
		   for(int j = 0; j < widthHeight - differentCount * 2; j++)result.insert(result.length(), characters.charAt(i));
		   for(int p = differentCount - 1; p >= 0; p--) result.insert(result.length(), characters.charAt(p));
		   differentCount++;
		   result.append("\n");
	   }
	   differentCount--;
	   for(int i = 0; i < (widthHeight / 2); i++) {
		   for(int p = 0; p < differentCount; p++) result.insert(result.length(), characters.charAt(p));
		   for(int j = 0; j < widthHeight - differentCount * 2; j++)result.insert(result.length(), characters.charAt(differentCount - 1));
		   for(int p = differentCount - 1; p >= 0; p--) result.insert(result.length(), characters.charAt(p));
		   differentCount--;
		   if(i != (widthHeight / 2) - 1)result.append("\n");
	   }
		 return result.toString();
	  }
	  
	 /**
	  * Palauttaa pyramidin näkyvät palikat. (Ylhäältäpäin. Suorakulmio => leveys*2)
	  * @param characters merkkijono, josta tehdään pyramidi
	  * @return näkyvien palikoiden lukumäärä
	  */
	  public static int countVisibleCharactersOfThePyramid(String characters) {
		  if(characters == null || characters.isEmpty()) return -1;
		int width = (characters.length() * 2) - 1;
	    return (width*width);
	  }

	  /**
	   * Palauttaa kaikki pyramidissa käytettyjen palikoiden lukumäärän.
	   * @param characters merkkijono, josta tehdään pyramidi
	   * @return palikoiden lukumäärä
	   */
	  public static int countAllCharactersOfThePyramid(String characters) {
		if(characters == null || characters.isEmpty()) return -1;
		int result = 0;
	    int width = (characters.length() * 2) - 1;
		for(int i = 0; i < characters.length(); i++) {
	    	result += (width*width);
	    	width -= 2;
	    }
		return result;
	  }
}
