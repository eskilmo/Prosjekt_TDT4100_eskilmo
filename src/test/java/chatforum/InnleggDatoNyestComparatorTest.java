package chatforum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InnleggDatoNyestComparatorTest {

    InnleggDatoNyestComparator innleggDatoNyestComparator;
    Bruker bruker;
    Tema tema1;
    Tema tema2;
    Tema tema3;

	@BeforeEach
	public void SetUp() {
        innleggDatoNyestComparator=new InnleggDatoNyestComparator();
        bruker=new Bruker("bruker", "bruker@gmail.com", "Passord1!");
		tema1=new Tema(bruker, "overskrift", "tekst", 2022, 03, 25, 11, 10, 30, new ArrayList<>());
        tema2=new Tema(bruker, "overskrift", "tekst", 2022, 04, 26, 12, 30, 50, new ArrayList<>());
        tema3=new Tema(bruker, "overskrift", "tekst", 2022, 04, 26, 12, 30, 50, new ArrayList<>());
	}

	@Test
	void testCompare() {
        assertTrue(innleggDatoNyestComparator.compare(tema1, tema2)>0, "tema2 før tema1, tema2 er nyest");
        assertTrue(innleggDatoNyestComparator.compare(tema2, tema1)<0);
        assertEquals(0, innleggDatoNyestComparator.compare(tema2, tema3), "tema2 og tema3 ble opprettet samtidig");
    }
}
