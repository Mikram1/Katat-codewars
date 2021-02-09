package harkka;

/**
 * Poikkeus, joka sattuu, kun alkioita yritet‰‰n tehd‰ liikaa. 
 */
public class alkioException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param viesti Mink‰lainen viesti virheest‰ tulee.
	 */
	public alkioException(String viesti) {
		super(viesti);
	}

}
