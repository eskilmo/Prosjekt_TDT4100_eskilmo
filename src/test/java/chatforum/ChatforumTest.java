package chatforum;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatforumTest {

    private Chatforum chatforum;
    private Bruker bruker;
    private Bruker bruker2;
    private Bruker bruker3;
    private Tema tema;
    private Tema tema2;
    private Chat kommentar;
    private Chat kommentar2;
    private Chat kommentar3;
    private List<Bruker> likesBrukere;
    private List<Bruker> likesBrukere2;
    private Tema temaForSortering;
    private Tema temaForSortering2;
    private Chat kommentarForSortering;
    private Chat kommentarForSortering2;
    

    @BeforeEach
    public void setup() {
        chatforum=new Chatforum();
        bruker=new Bruker("brukernavn", "epost@gmail.com", "Passord1!");
        bruker2=new Bruker("brukernavn2", "epost2@gmail.com", "Passord2!");
        bruker3=new Bruker("brukernavn3", "epost3@gmail.com", "Passord3!");
        tema=new Tema(bruker, "overskrift", "tekst");
        tema2=new Tema(bruker2, "overskrift2", "tekst2");
        kommentar=new Chat(tema, bruker, "tekst");
        kommentar2=new Chat(tema2, bruker, "tekst2");
        kommentar3=new Chat(tema, bruker2, "tekst3");
        likesBrukere=new ArrayList<>();
        likesBrukere2=new ArrayList<>();
        likesBrukere.add(bruker2);
        temaForSortering=new Tema(bruker, "overskrift", "tekst", 2022, 04, 26, 12, 00, 00, likesBrukere2);
        temaForSortering2=new Tema(bruker, "overskrift", "tekst", 2022, 04, 25, 12, 00, 00, likesBrukere);
        kommentarForSortering=new Chat(temaForSortering, bruker, "tekst", 2022, 04, 28, 12, 00, 00, likesBrukere);
        kommentarForSortering2=new Chat(temaForSortering, bruker, "tekst", 2022, 04, 27, 12, 00, 00, likesBrukere2);
    }

    @Test
    @DisplayName("Test at logg inn funker og sjekk om bruker er logget inn")
    public void testBrukerLoggInn() {
        assertThrows(IllegalArgumentException.class, () -> {
            chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        }, "Bruker eksisterer ikke");
        chatforum.leggTilBruker(bruker);
        assertThrows(IllegalArgumentException.class, () -> {
            chatforum.brukerLoggInn(bruker.getBrukernavn(), "");
        }, "Ikke logge inn med feil passord");
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertEquals(bruker, chatforum.getInnloggetBruker());
        assertTrue(chatforum.sjekkBrukerLoggetInn());
    }

    @Test
    @DisplayName("Test at logg ut funker og sjekk om bruker er logget ut")
    public void testBrukerLoggUt() {
        assertThrows(IllegalStateException.class, () -> {
            chatforum.brukerLoggUt();
        }, "Kan ikke logge ut hvis man ikke er logget inn");
        chatforum.leggTilBruker(bruker);
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertDoesNotThrow(() -> chatforum.brukerLoggUt());
        assertFalse(chatforum.sjekkBrukerLoggetInn());
    }

    @Test
    @DisplayName("Test å legge til en ny bruker")
    public void testLeggTilNyBruker() {
        chatforum.leggTilBruker(bruker);
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilNyBruker("brukernavn", "epost@gmail.com", "Passord1!"));
    }

    @Test
    @DisplayName("Test å legge til eksisterende bruker-objekt")
    public void testLeggTilBruker() {
        assertDoesNotThrow(() -> chatforum.leggTilBruker(bruker));
        Bruker bruker2=new Bruker("brukernavn", "epost@gmail.com", "Passord1!");
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilBruker(bruker2), "Kan ikke legge til bruker med samme brukernavn");
    }

    @Test
    @DisplayName("Test å slette bruker, og at alle brukerens tema og kommentarer blir slettet, og alle likes fjernes")
    public void testSlettBruker() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        chatforum.leggTilEksisterendeTema(tema);
        chatforum.leggTilEksisterendeKommentar(kommentar);
        chatforum.leggTilEksisterendeTema(tema2);
        tema2.leggTilEksisterendeKommentarTilTema(kommentar2);

        assertThrows(IllegalStateException.class, () -> chatforum.slettBruker(bruker.getPassord()));
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettBruker("passord"));
        chatforum.likTema(tema2);
        
        chatforum.slettBruker(bruker.getPassord());
        assertFalse(chatforum.sjekkBrukerEksisterer(bruker));
        assertFalse(chatforum.sjekkBrukerLoggetInn());
        assertFalse(chatforum.getAlleTema().contains(tema), "brukerens tema skal slettes");
        assertFalse(chatforum.getAlleTema().get(0).getAlleKommentarerTilTema().contains(kommentar2), "kommentaren skal slettes");
        assertFalse(chatforum.getAlleTema().get(0).getLikesBrukere().contains(bruker), "likes skal være borte");
    }

    @Test
    @DisplayName("Test å endre brukernavn")
    public void testEndreBrukernavn() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.endreBrukernavn("nytt_brukernavn"));
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.endreBrukernavn("brukernavn2"));
        assertThrows(IllegalArgumentException.class, () -> chatforum.endreBrukernavn("bruker navn7"));
        assertDoesNotThrow(() -> chatforum.endreBrukernavn("nytt_brukernavn"));
    }

    @Test
    @DisplayName("Test å endre passord")
    public void testEndrePassord() {
        chatforum.leggTilBruker(bruker);
        assertThrows(IllegalStateException.class, () -> chatforum.endrePassord(bruker.getPassord(), "Passord2?"));
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.endrePassord("", "Passord2?"));
        assertThrows(IllegalArgumentException.class, () -> chatforum.endrePassord(bruker.getPassord(), "123"));
        assertDoesNotThrow(() -> chatforum.endrePassord(bruker.getPassord(), "Passord2?"));
    }

    @Test
    @DisplayName("Test sjekkene for om bruker og brukernavn eksisterer")
    public void testSjekkBrukerOgBrukernavnEksisterer() {
        assertFalse(chatforum.sjekkBrukerEksisterer(bruker));
        assertFalse(chatforum.sjekkBrukernavnEksisterer(bruker.getBrukernavn()));
        chatforum.leggTilBruker(bruker);
        assertTrue(chatforum.sjekkBrukerEksisterer(bruker));
        assertTrue(chatforum.sjekkBrukernavnEksisterer(bruker.getBrukernavn()));
    }

    @Test
    @DisplayName("Test sjekk for om tema eksisterer")
    public void testSkjekkTemaEksisterer() {
        chatforum.leggTilBruker(bruker);
        assertThrows(IllegalArgumentException.class, () -> chatforum.sjekkTemaEksisterer(tema));
        chatforum.leggTilEksisterendeTema(tema);
        assertDoesNotThrow(() -> chatforum.sjekkTemaEksisterer(tema));
    }

    @Test
    public void testLeggTilTema() {
        assertThrows(IllegalStateException.class, () -> chatforum.leggTilTema("overskrift", "tekst"));
        chatforum.leggTilBruker(bruker);
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilTema(" \n", ""));
        assertEquals(0, chatforum.getAlleTema().size());
        assertDoesNotThrow(() -> chatforum.leggTilTema("overskrift", "tekst"));
        assertEquals(1, chatforum.getAlleTema().size());
        assertEquals(chatforum.getAlleTema().get(0), chatforum.getTemaTrykketPaa());
    }

    @Test
    public void testLeggTilEksisterendeTema() {
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilEksisterendeTema(tema), "bruker er ikke i chatforumet");
        chatforum.leggTilBruker(bruker);
        Tema temaMedFeilBrukerLikes=new Tema(bruker, "overskrift", "tekst", 2022, 04, 02, 02, 02, 02, likesBrukere);
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilEksisterendeTema(temaMedFeilBrukerLikes), "bruker2 har likt temaet men er ikke i chatforumet");
        assertEquals(0, chatforum.getAlleTema().size());
        chatforum.leggTilEksisterendeTema(tema);
        assertTrue(chatforum.getAlleTema().contains(tema));
        assertEquals(1, chatforum.getAlleTema().size());
    }

    @Test
    public void testSlettTema() {
        assertThrows(IllegalStateException.class, () -> chatforum.slettTema(tema));
        chatforum.leggTilBruker(bruker);
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettTema(null));
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettTema(tema));
        chatforum.leggTilEksisterendeTema(tema);
        assertDoesNotThrow(() -> chatforum.slettTema(tema), "eieren skal kunne slette temaet dersom eieren er logget inn");
        assertFalse(chatforum.getAlleTema().contains(tema));

        chatforum.leggTilBruker(bruker2);
        chatforum.brukerLoggInn(bruker2.getBrukernavn(), bruker2.getPassord());
        chatforum.leggTilEksisterendeTema(tema);
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettTema(tema), "kun eieren skal kunne slette sitt tema");
        assertTrue(chatforum.getAlleTema().contains(tema));
    }

    @Test
    public void testLeggTilKommentar() {
        assertThrows(IllegalStateException.class, () -> chatforum.leggTilKommentar(tema, "tekst"));
        chatforum.leggTilBruker(bruker);
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilKommentar(tema, "tekst"));
        chatforum.leggTilEksisterendeTema(tema);
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilKommentar(tema, " \t"));
        assertEquals(0,tema.getAlleKommentarerTilTema().size());
        assertDoesNotThrow(() -> chatforum.leggTilKommentar(tema, "tekst"));
        assertEquals(1,tema.getAlleKommentarerTilTema().size());
    }

    @Test
    public void testLeggTilEksisterendeKommentar() {
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilEksisterendeKommentar(kommentar), "bruker er ikke i chatforumet");
        chatforum.leggTilBruker(bruker);
        Chat kommentarMedFeilBrukerLikes=new Chat(temaForSortering, bruker, "tekst", 2022, 04, 29, 02, 02, 02, likesBrukere);
        assertThrows(IllegalArgumentException.class, () -> chatforum.leggTilEksisterendeKommentar(kommentarMedFeilBrukerLikes), "bruker2 har likt temaet men er ikke i chatforumet");

        chatforum.leggTilEksisterendeTema(tema);
        assertEquals(0, tema.getAlleKommentarerTilTema().size());
        chatforum.leggTilEksisterendeKommentar(kommentar);
        assertTrue(tema.getAlleKommentarerTilTema().contains(kommentar));
        assertEquals(1, tema.getAlleKommentarerTilTema().size());
    }

    @Test
    public void testSlettKommentar() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.slettKommentar(kommentar));
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        chatforum.leggTilEksisterendeTema(tema2);
        tema2.leggTilEksisterendeKommentarTilTema(kommentar2);

        assertThrows(IllegalArgumentException.class, () -> chatforum.slettKommentar(null));
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettKommentar(kommentar), "kan ikke slette kommentar som ikke er lagt til");
        assertDoesNotThrow(() -> chatforum.slettKommentar(kommentar2), "eieren av kommetaren skal kunne slette kommentar");
        assertFalse(tema2.getAlleKommentarerTilTema().contains(kommentar));

        chatforum.brukerLoggInn(bruker2.getBrukernavn(), bruker2.getPassord());
        tema2.leggTilEksisterendeKommentarTilTema(kommentar2);
        assertDoesNotThrow(() -> chatforum.slettKommentar(kommentar2), "eieren av tema skal kunne slette kommentar");
        assertFalse(tema2.getAlleKommentarerTilTema().contains(kommentar));

        chatforum.leggTilBruker(bruker3);
        chatforum.brukerLoggInn(bruker3.getBrukernavn(), bruker3.getPassord());
        tema2.leggTilEksisterendeKommentarTilTema(kommentar);
        assertThrows(IllegalArgumentException.class, () -> chatforum.slettKommentar(kommentar), "en annen enn eieren av temaet eller kommentaren kan ikke slette kommentaren");
        assertTrue(tema2.getAlleKommentarerTilTema().contains(kommentar));
    }

    @Test
    public void testLikTema() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.likTema(tema2), "må være innlogget for å like tema");
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.likTema(tema2), "skal ikke kunne like tema som ikke finnes");
        chatforum.leggTilEksisterendeTema(tema);
        assertThrows(IllegalArgumentException.class, () -> chatforum.likTema(tema), "skal ikke kunne like eget tema");
        chatforum.leggTilEksisterendeTema(tema2);
        assertDoesNotThrow(() -> chatforum.likTema(tema2), "skal kunne like andres tema");
        assertTrue(tema2.getLikesBrukere().contains(bruker));
        assertEquals(1, tema2.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chatforum.likTema(tema2), "skal ikke kunne like andres tema når det allerede er likt");
    }

    @Test
    public void testUnlikTema() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.unlikTema(tema2), "må være innlogget for å unlike tema");
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikTema(tema2), "skal ikke kunne unlike tema som ikke finnes");
        chatforum.leggTilEksisterendeTema(tema);
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikTema(tema), "skal ikke kunne unlike eget tema");
        chatforum.leggTilEksisterendeTema(tema2);
        chatforum.likTema(tema2);
        assertDoesNotThrow(() -> chatforum.unlikTema(tema2), "skal kunne unlike andres tema etter å ha likt det");
        assertFalse(tema2.getLikesBrukere().contains(bruker));
        assertEquals(0, tema2.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikTema(tema2), "skal ikke kunne unlike andres tema når det ikke likt");
    }

    @Test
    public void testLikKommentar() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.likKommentar(kommentar3), "må være innlogget for å like kommentar");
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.likKommentar(kommentar3), "skal ikke kunne like kommentar som ikke finnes");
        chatforum.leggTilEksisterendeTema(tema);
        chatforum.leggTilEksisterendeKommentar(kommentar);
        assertThrows(IllegalArgumentException.class, () -> chatforum.likKommentar(kommentar), "skal ikke kunne like egen kommentar");
        chatforum.leggTilEksisterendeKommentar(kommentar3);
        assertDoesNotThrow(() -> chatforum.likKommentar(kommentar3), "skal kunne like andres kommentar");
        assertTrue(kommentar3.getLikesBrukere().contains(bruker));
        assertEquals(1, kommentar3.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chatforum.likKommentar(kommentar3), "skal ikke kunne like andres kommentar når det allerede er likt");
    }

    @Test
    public void testUnlikKommentar() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        assertThrows(IllegalStateException.class, () -> chatforum.unlikKommentar(kommentar3), "må være innlogget for å unlike kommentar");
        chatforum.brukerLoggInn(bruker.getBrukernavn(), bruker.getPassord());
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikKommentar(kommentar3), "skal ikke kunne unlike kommentar som ikke finnes");
        chatforum.leggTilEksisterendeTema(tema);
        chatforum.leggTilEksisterendeKommentar(kommentar);
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikKommentar(kommentar), "skal ikke kunne unlike egen kommentar");
        chatforum.leggTilEksisterendeKommentar(kommentar3);
        chatforum.likKommentar(kommentar3);
        assertDoesNotThrow(() -> chatforum.unlikKommentar(kommentar3), "skal kunne unlike andres kommentar etter å ha likt de");
        assertFalse(kommentar3.getLikesBrukere().contains(bruker));
        assertEquals(0, kommentar3.getLikes());
        assertThrows(IllegalArgumentException.class, () -> chatforum.unlikKommentar(kommentar3), "skal ikke kunne unlike andres kommentar når det ikke likt");
    }

    @Test
    @DisplayName("Test om begge listene sorteres etter dato.")
    public void testSorterDato() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        chatforum.leggTilEksisterendeTema(temaForSortering);
        chatforum.leggTilEksisterendeTema(temaForSortering2);
        chatforum.leggTilEksisterendeKommentar(kommentarForSortering);
        chatforum.leggTilEksisterendeKommentar(kommentarForSortering2);

        chatforum.sorterDato();
        assertEquals(temaForSortering, chatforum.getAlleTema().get(0), "Temaene skal sorteres etter nyest først");
        assertEquals(temaForSortering2, chatforum.getAlleTema().get(1));

        assertEquals(kommentarForSortering2, temaForSortering.getAlleKommentarerTilTema().get(0), "Kommentarene skal sorteres etter eldst først");
        assertEquals(kommentarForSortering, temaForSortering.getAlleKommentarerTilTema().get(1));
    }

    @Test
    @DisplayName("Test om begge listene sorteres etter flest likes.")
    public void testSorterLikes() {
        chatforum.leggTilBruker(bruker);
        chatforum.leggTilBruker(bruker2);
        chatforum.leggTilEksisterendeTema(temaForSortering);
        chatforum.leggTilEksisterendeTema(temaForSortering2);
        chatforum.leggTilEksisterendeKommentar(kommentarForSortering);
        chatforum.leggTilEksisterendeKommentar(kommentarForSortering2);

        chatforum.sorterLikes();
        assertEquals(temaForSortering2, chatforum.getAlleTema().get(0), "Temaene skal sorteres etter flest likes");
        assertEquals(temaForSortering, chatforum.getAlleTema().get(1));

        assertEquals(kommentarForSortering, temaForSortering.getAlleKommentarerTilTema().get(0), "Kommentarene skal sorteres etter flest likes");
        assertEquals(kommentarForSortering2, temaForSortering.getAlleKommentarerTilTema().get(1));
    }

}
