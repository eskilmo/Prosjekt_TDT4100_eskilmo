package chatforum;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatTest {

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
    @DisplayName("Test at det ikke går an å opprette en kommentar uten tekst")
    public void testKonstruktor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Chat(tema, bruker,  "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Chat(tema, bruker,  " \n   \t");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Chat(tema, bruker, "tekst", 2022, 02, 02, 20, 02, 04, new ArrayList<>());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Chat(tema, bruker, "tekst", 2040, 03, 02, 20, 02, 02, new ArrayList<>());
        });
        assertThrows(DateTimeException.class, () -> {
            new Chat(tema, bruker, "tekst", 2022, -2, 2, 20, 02, 61, new ArrayList<>());
        });
    }

    @Test
    public void testGetPrettyDato() {
        assertEquals("26.04.2022 12:30", chat.getPrettyDato());
    }

    @Test
    public void testGetFunksjonellDato() {
        assertEquals("2022,04,26,12,30,15", chat.getFunksjonellDato());
    }

    @Test
    @DisplayName("Test at det returneres en ny liste for likesBrukere, og at antall likes er riktig")
    public void testGetLikesBrukere() {
        List<Bruker> likesBrukere=new ArrayList<>();
        likesBrukere.add(bruker2);
        Chat chat=new Chat(tema, bruker, "tekst", 2022, 04, 28, 20, 20, 20, likesBrukere);
        assertFalse(chat.getLikesBrukere()==likesBrukere);
        assertEquals(1, chat.getLikes());
    }

    @Test
    public void testLikeKommentar() {
        assertThrows(IllegalArgumentException.class, () -> chat.likeKommentar(bruker2), "Kan ikke like sin egen kommentar");
        assertEquals(0, chat.getLikes());
        assertDoesNotThrow(() -> chat.likeKommentar(bruker));
        assertEquals(1, chat.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chat.likeKommentar(bruker));
    }

    @Test
    public void testUnlikeKommentar() {
        chat.likeKommentar(bruker);
        assertEquals(1, chat.getLikes());
        assertDoesNotThrow(() -> chat.unlikeKommentar(bruker));
        assertEquals(0, chat.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chat.unlikeKommentar(bruker));
    }

    @Test
    public void testSjekkKommentarLikt() {
        assertFalse(chat.sjekkKommentarLikt(bruker));
        chat.likeKommentar(bruker);
        assertTrue(chat.sjekkKommentarLikt(bruker));
        chat.unlikeKommentar(bruker);
        assertFalse(chat.sjekkKommentarLikt(bruker));
    }

}
