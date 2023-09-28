package chatforum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatforumHandlerTest {
    
    private Chatforum chatforum;
    private ChatforumHandler chatforumHandler;

    @BeforeEach
    public void setup() {
        chatforum=new Chatforum();
        chatforumHandler=new ChatforumHandler();

        chatforum.leggTilNyBruker("brukernavn", "bruker@gmail.com", "Passord1!");
        chatforum.brukerLoggInn("brukernavn", "Passord1!");
        Tema tema=new Tema(chatforum.getAlleBrukere().get(0), "overskrift", "tekst", 2022, 04, 24, 20, 31, 05, new ArrayList<>());
        chatforum.leggTilEksisterendeTema(tema);

        chatforum.leggTilNyBruker("brukernavn2", "bruker2@gmail.com", "Passord2!");
        chatforum.brukerLoggInn("brukernavn2", "Passord2!");
        chatforum.likTema(tema);

        Chat kommentar=new Chat(tema, chatforum.getAlleBrukere().get(1), "kommentar", 2022, 04, 24, 20, 32, 40, new ArrayList<>());
        chatforum.leggTilEksisterendeKommentar(kommentar);

        chatforum.brukerLoggUt();
        chatforum.setTemaTrykketPaa(chatforum.getAlleTema().get(0));
    }

    @Test
	public void testSkrivProgramMedIkkeEksisterendeFil() {
		assertThrows(FileNotFoundException.class, () -> {
            chatforum=chatforumHandler.skrivProgram("feil_filnavn");
        }, "Filen eksisterer ikke");
	}

    @Test
    public void testSkrivProgramMedUgyldigFil() {
        assertThrows(Exception.class, () -> {
            chatforum=chatforumHandler.skrivProgram("ugyldigLagring");
        }, "Filen er ugyldig");
    }

    @Test
    public void testSkrivProgram() {
        Chatforum lagreNyttChatforum;
        try {
            lagreNyttChatforum=chatforumHandler.skrivProgram("testLagring");
        } catch (FileNotFoundException e) {
            fail("Kunne ikke skrive til fil.");
            return;
        }
        assertEquals(chatforum.getAlleBrukere().toString(), lagreNyttChatforum.getAlleBrukere().toString(), "listen med alle brukere skal være like");
        assertEquals(chatforum.getAlleBrukere().get(0).getBrukernavn().toString(), lagreNyttChatforum.getAlleBrukere().get(0).getBrukernavn().toString(), "brukernavnene skal være like");
        assertEquals(chatforum.getAlleBrukere().get(0).getEpost().toString(), lagreNyttChatforum.getAlleBrukere().get(0).getEpost().toString(), "epostene skal være like");
        assertEquals(chatforum.getAlleBrukere().get(0).getPassord().toString(), lagreNyttChatforum.getAlleBrukere().get(0).getPassord().toString(), "passordene skal være like");
        assertEquals(chatforum.getAlleTema().toString(), lagreNyttChatforum.getAlleTema().toString(), "listen med alle temaer skal være like");
        assertEquals(chatforum.getAlleTema().get(0).toString(), lagreNyttChatforum.getAlleTema().get(0).toString(), "temaene skal være like");
        assertEquals(chatforum.getAlleTema().get(0).getLikesBrukere().toString(), lagreNyttChatforum.getAlleTema().get(0).getLikesBrukere().toString(), "de som har likt temaene skal være like");
        assertEquals(chatforum.getAlleTema().get(0).getAlleKommentarerTilTema().toString(), lagreNyttChatforum.getAlleTema().get(0).getAlleKommentarerTilTema().toString(), "kommentarene skal være like");
        assertEquals(chatforum.getAlleTema().get(0).getAlleKommentarerTilTema().get(0).getLikesBrukere().toString(), lagreNyttChatforum.getAlleTema().get(0).getAlleKommentarerTilTema().get(0).getLikesBrukere().toString(), "de som har likt kommentaren");
        assertEquals(chatforum.getInnloggetBruker(), lagreNyttChatforum.getInnloggetBruker(), "innlogget bruker skal være lik");
        assertEquals(chatforum.getTemaTrykketPaa().toString(), lagreNyttChatforum.getTemaTrykketPaa().toString(), "tema trykket på skal være lik");
        assertEquals(chatforum.getKommentarTrykketPaa(), lagreNyttChatforum.getKommentarTrykketPaa(), "kommentar trykket på skal være lik");
    }

    @Test
    public void testLagreProgram() {
        try {
            chatforumHandler.lagreProgram(chatforum, "nyLagreFil");
        } catch (FileNotFoundException e) {
            fail("Kan ikke lagre program til fil.");
        }

        byte[] testFil=null;
        byte[] nyFil=null;

        try {
            testFil=Files.readAllBytes(Path.of(ChatforumHandler.getFilePath("testLagring")));
        } catch (IOException e) {
            fail("Kunne ikke lagre til testfil");
        }

        try {
            nyFil=Files.readAllBytes(Path.of(ChatforumHandler.getFilePath("nyLagreFil")));
        } catch (IOException e) {
            fail("Kunne ikke lagre til ny testfil");
        }
        
        assertNotNull(testFil);
        assertNotNull(nyFil);
        assertTrue(Arrays.equals(testFil, nyFil));
    }

    @AfterAll
    public static void ryddOpp() {
        File nyTestLagreFil = new File(ChatforumHandler.getFilePath("nyLagreFil"));
		nyTestLagreFil.delete();
    }
}
