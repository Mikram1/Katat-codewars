package harkka;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2016.04.19 18:12:51 // Generated by ComTest
 *
 */
public class NimiTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi160 */
  @Test
  public void testRekisteroi160() {    // Nimi: 160
    Nimi n1 = new Nimi(); 	Nimi n2 = new Nimi(); 
    n1.rekisteroi(); 			n2.rekisteroi(); 
    int ekaind = n1.getId(); 
    assertEquals("From: Nimi line: 164", (n1.getId() + 1), n2.getId()); 
    n1.rekisteroi(); 
    assertEquals("From: Nimi line: 166", ekaind, n1.getId()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testToString179 */
  @Test
  public void testToString179() {    // Nimi: 179
    Nimi nimi1 = new Nimi(); 
    nimi1.parse("2 | Lihakastike| 25min |3| 4e"); 
    nimi1.getNimi().equals("Lihakastike"); 
    assertEquals("From: Nimi line: 183", 2, nimi1.getId()); 
  } // Generated by ComTest END
}