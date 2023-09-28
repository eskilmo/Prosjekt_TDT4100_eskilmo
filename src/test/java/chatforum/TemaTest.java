package chatforum;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TemaTest {
    
    private Chat chat;
    private Bruker bruker;
    private Bruker bruker2;
    private Tema tema;

    @BeforeEach
    public void setup() {
        bruker=new Bruker("brukernavn", "brukernavn@gmail.com", "12345Aa!");
        bruker2=new Bruker("brukernavn2", "brukernavn@gmail.com", "12345Aa!");
        tema=new Tema(bruker2, "overskrift", "tekst", 2022, 04, 26, 12, 20, 30, new ArrayList<>());
        chat=new Chat(tema, bruker2, "tekst", 2022, 04, 26, 12, 30, 15, new ArrayList<>());
        
    }

    @Test
    public void testKonstruktor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tema(bruker, "", "tekst");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Tema(bruker, "overskrift", " \n   \t");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Tema(bruker, "overskrift", "tekst", 2040, 03, 02, 20, 02, 02, new ArrayList<>());
        });
        assertThrows(DateTimeException.class, () -> {
            new Tema(bruker, "overskrift", "tekst", 2022, -2, 2, 20, 02, 61, new ArrayList<>());
        });
    }

    @Test
    public void testLikeTema() {
        assertThrows(IllegalArgumentException.class, () -> tema.likeTema(bruker2), "Kan ikke like sitt eget tema");
        assertEquals(0, tema.getLikes());
        assertDoesNotThrow(() -> tema.likeTema(bruker));
        assertEquals(1, tema.getLikes());
        assertThrows(IllegalArgumentException.class, () -> tema.likeTema(bruker));
    }

    @Test
    public void testUnlikeTema() {
        tema.likeTema(bruker);
        assertEquals(1, tema.getLikes());
        assertDoesNotThrow(() -> tema.unlikeTema(bruker));
        assertEquals(0, tema.getLikes());
        assertThrows(IllegalArgumentException.class, () -> tema.unlikeTema(bruker));
    }

    @Test
    public void testSjekkTemaLikt() {
        assertFalse(tema.sjekkTemaLikt(bruker));
        tema.likeTema(bruker);
        assertTrue(tema.sjekkTemaLikt(bruker));
        tema.unlikeTema(bruker);
        assertFalse(tema.sjekkTemaLikt(bruker));
    }

    @Test
    public void testLeggTilKommentarTilTema() {
        assertThrows(IllegalArgumentException.class, () -> tema.leggTilKommentarTilTema(bruker, " "));
        tema.leggTilKommentarTilTema(bruker, "tekst");
    }

    @Test
    public void testLeggTilEksisterendeKommentarTilTema() {
        assertEquals(0, tema.getAlleKommentarerTilTema().size());
        tema.leggTilEksisterendeKommentarTilTema(chat);
        assertEquals(1, tema.getAlleKommentarerTilTema().size());
        assertTrue(tema.getAlleKommentarerTilTema().contains(chat));
        assertThrows(IllegalArgumentException.class, () -> tema.leggTilEksisterendeKommentarTilTema(chat));
    }

    @Test
    public void testSlettKommentarTilTema() {
        assertThrows(IllegalArgumentException.class, () -> tema.slettKommentarTilTema(chat));
        tema.leggTilEksisterendeKommentarTilTema(chat);
        tema.slettKommentarTilTema(chat);
        assertFalse(tema.getAlleKommentarerTilTema().contains(chat));
    }

    @Test
    public void testSjekkKommentarEksisterer() {
        assertThrows(IllegalArgumentException.class, () -> tema.sjekkKommentarEksisterer(chat));
        tema.leggTilEksisterendeKommentarTilTema(chat);
        assertDoesNotThrow(() -> tema.sjekkKommentarEksisterer(chat));
    }
    
}
