package chatforum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InnleggLikesComparatorTest {

    InnleggLikesComparator innleggLikesComparator;
    Bruker bruker1;
    Bruker bruker2;
    Tema tema1;
    Tema tema2;

	@BeforeEach
	public void SetUp() {
        innleggLikesComparator=new InnleggLikesComparator();
        bruker1=new Bruker("bruker1", "bruker1@gmail.com", "Passord1!");
        bruker2=new Bruker("bruker2", "bruker2@gmail.com", "Passord1!");
		tema1=new Tema(bruker1, "overskrift", "tekst");
        tema2=new Tema(bruker1, "overskrift", "tekst");
	}

	@Test
	void testCompare() {
		tema1.likeTema(bruker2);
        assertTrue(innleggLikesComparator.compare(tema1, tema2)<0, "tema1 før tema2, tema1 har flere likes");
        assertTrue(innleggLikesComparator.compare(tema2, tema1)>0);
        tema2.likeTema(bruker2);
        assertEquals(0, innleggLikesComparator.compare(tema1, tema2), "tema1 og tema2 har like mange likes");
	}
}
