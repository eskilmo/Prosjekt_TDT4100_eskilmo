package chatforum;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrukerTest {
    private Bruker bruker;

    @BeforeEach
    public void setup() {
        bruker=new Bruker("brukernavn", "brukernavn@gmail.com", "123abcA!");
    }

    @Test
    public void testKonstruktor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("es", "brukernavn@gmail.com", "123abcA!");
        }, "Brukernavn må være minst 3 tegn.");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("brukernavn er kul?", "brukernavn@gmail.com", "123abcA!");
        }, "Brukernavn kan ikke inneholde mellomrom, og andre tegn enn understrek og tall.");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("etveldiglangtbrukernavnikkelovlig", "brukernavn@gmail.com", "123abcA!");
        }, "Brukernavn kan være maks 22 tegn.");

        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("brukernavn", "brukernavn#gmail!com", "123abcA!");
        }, "Email må være av format navn@domain.com (navn kan inneholde bindestrek og punktum)");

        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("brukernavn", "brukernavn@gmail.com", "123abcA! feil");
        }, "Passord må være mellom 8 og 22 tegn, inneholde minst en stor og liten bokstav, et tall, et annet tegn og ingen mellomrom.");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("brukernavn", "brukernavn@gmail.com", "123");
        }, "Passord må være mellom 8 og 22 tegn, inneholde minst en stor og liten bokstav, et tall, et annet tegn og ingen mellomrom.");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bruker("brukernavn", "brukernavn@gmail.com", "123abcFEIL");
        }, "Passord må være mellom 8 og 22 tegn, inneholde minst en stor og liten bokstav, et tall, et annet tegn og ingen mellomrom.");

        new Bruker("brukernavn_1_her", "brukernavn@gmail.com", "123abcA!");
        new Bruker("brukernavn", "brukernavn@stud.ntnu.no", "123abcA!-w2&jh>s");
    }

    @Test
    public void testTestRiktigPassord() {
        assertDoesNotThrow(() -> bruker.testRiktigPassord("123abcA!"));
        assertThrows(IllegalArgumentException.class, () -> bruker.testRiktigPassord("1234"));
    }

    @Test
    public void testEndreBrukernavn() {
        assertThrows(IllegalArgumentException.class, () -> bruker.endreBrukeravn(""));
        assertThrows(IllegalArgumentException.class, () -> bruker.endreBrukeravn("brukernavn- 7?"));
        bruker.endreBrukeravn("peter");
        assertEquals("peter", bruker.getBrukernavn());
    }

    @Test
    public void testEndrePassord() {
        assertThrows(IllegalArgumentException.class, () -> bruker.endrePassord("1234","54321Aa!"));
        bruker.endrePassord("123abcA!","54321Aa!");
        assertEquals("54321Aa!", bruker.getPassord());
    }
}
