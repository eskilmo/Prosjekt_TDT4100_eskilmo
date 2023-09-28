package chatforum;

import java.io.FileNotFoundException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;

public class ChatforumController {

    private Chatforum chatforum=new Chatforum();
    private IChatforumHandler chatforumHandler=new ChatforumHandler();
    
    @FXML public Button loggInnKnapp, registrerKnapp, loggUtKnapp, byttBrukernavnKnapp, byttPassordKnapp, slettBrukerKnapp;

    @FXML public TextField brukernavnFelt, epostFelt, passordFelt, nyttBrukernavnFelt, gammeltPassordFelt, nyttPassordFelt, slettBrukerPassordFelt;

    @FXML public Label brukernavnVisningLabel, slettBrukerLabel;

    @FXML public Button sorterDatoKnapp, sorterLikesKnapp;

    @FXML public Button leggTilTemaKnapp, leggTilKommentarKnapp, slettTemaKnapp, slettKommentarKnapp;

    @FXML public Button likTemaKnapp, likKommentarKnapp, unlikTemaKnapp, unlikKommentarKnapp;

    @FXML public ListView<Tema> alleTemaer;
    
    @FXML public ListView<Chat> alleKommentarer;

    @FXML public ImageView avatar;

    @FXML public TextArea overskriftTema, tekstKommentar, tekstTema;

    @FXML
    public void initialize() {
        try {
            chatforum=chatforumHandler.skrivProgram("lagring");
            chatforum.setInnloggetBrukerNull();
            sorterDato();
        }
        catch (FileNotFoundException e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void registrerNyBruker() {
        try {
            chatforum.leggTilNyBruker(brukernavnFelt.getText(), epostFelt.getText(), passordFelt.getText());
            chatforum.brukerLoggInn(brukernavnFelt.getText(), passordFelt.getText());
            brukernavnVisningLabel.setText(chatforum.getInnloggetBruker().getBrukernavn());
            nullstillFeltLogginn();
            loggetInnModus(true);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }
    
    @FXML
    public void loggInn() {
        try {
            chatforum.brukerLoggInn(brukernavnFelt.getText(), passordFelt.getText());
            brukernavnVisningLabel.setText(chatforum.getInnloggetBruker().getBrukernavn());
            nullstillFeltLogginn();
            loggetInnModus(true);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void loggUt() {
        try {
            chatforum.brukerLoggUt();
            nullstillFeltLoggut();
            loggetInnModus(false);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    private void loggetInnModus(boolean b) {
        loggInnKnapp.visibleProperty().set(!b);
        loggUtKnapp.visibleProperty().set(b);
        registrerKnapp.visibleProperty().set(!b);
        brukernavnVisningLabel.visibleProperty().set(b);
        brukernavnFelt.visibleProperty().set(!b);
        epostFelt.visibleProperty().set(!b);
        passordFelt.visibleProperty().set(!b);
        nyttBrukernavnFelt.visibleProperty().set(b);
        gammeltPassordFelt.visibleProperty().set(b);
        nyttPassordFelt.visibleProperty().set(b);
        slettBrukerPassordFelt.visibleProperty().set(b);
        byttBrukernavnKnapp.visibleProperty().set(b);
        byttPassordKnapp.visibleProperty().set(b);
        slettBrukerKnapp.visibleProperty().set(b);
        slettBrukerLabel.visibleProperty().set(b);
    }

    private void nullstillFeltLogginn() {
        brukernavnFelt.setText("");
        passordFelt.setText("");
        epostFelt.setText("");
    }

    private void nullstillFeltLoggut() {
        nyttBrukernavnFelt.setText("");
        gammeltPassordFelt.setText("");
        nyttPassordFelt.setText("");
        slettBrukerPassordFelt.setText("");
    }

    @FXML
    public void byttBrukernavn() {
        try {
            chatforum.endreBrukernavn(nyttBrukernavnFelt.getText());
            brukernavnVisningLabel.setText(chatforum.getInnloggetBruker().getBrukernavn());
            nyttBrukernavnFelt.setText("");
            visBekreftelseMelding("Byttet brukernavn til: "+chatforum.getInnloggetBruker().getBrukernavn());
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void byttPassord() {
        try {
            chatforum.endrePassord(gammeltPassordFelt.getText(), nyttPassordFelt.getText());
            gammeltPassordFelt.setText("");
            nyttPassordFelt.setText("");
            visBekreftelseMelding("Byttet passord");
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void slettBruker() {
        try {
            String brukerSomSlettes=chatforum.getInnloggetBruker().getBrukernavn();
            chatforum.slettBruker(slettBrukerPassordFelt.getText());
            nullstillFeltLoggut();
            loggetInnModus(false);
            lagreChatforum();
            getChatforum();
            visBekreftelseMelding("Slettet bruker: "+brukerSomSlettes);
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void sorterDato() {
        chatforum.sorterDato();
        oppdaterTemaer();
        oppdaterKommentarer();
    }

    @FXML
    public void sorterLikes() {
        chatforum.sorterLikes();
        oppdaterTemaer();
        oppdaterKommentarer();
    }

    @FXML
    public void temaTrykketPaa() {
        try {
            chatforum.setTemaTrykketPaa(alleTemaer.getSelectionModel().getSelectedItem());
            oppdaterKommentarer();
            visLikTemaKnapper(chatforum.getTemaTrykketPaa().sjekkTemaLikt(chatforum.getInnloggetBruker()));
            lagreChatforum();
            getChatforum();
        } catch (NullPointerException e) {
            //Ikke gjør noe
        }
    }

    @FXML
    public void chatTrykketPaa() {
        try {
            chatforum.setKommentarTrykketPaa(alleKommentarer.getSelectionModel().getSelectedItem());
            oppdaterTemaer();
            visLikKommentarKnapper(chatforum.getKommentarTrykketPaa().sjekkKommentarLikt(chatforum.getInnloggetBruker()));
            lagreChatforum();
            getChatforum();
        } catch (NullPointerException e) {
            //Ikke gjør noe
        }

    }

    @FXML
    public void leggTilTema() {
        try {
            chatforum.leggTilTema(overskriftTema.getText(),tekstTema.getText());
            overskriftTema.setText("");
            tekstTema.setText("");
            chatforum.sorter();
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void leggTilKommentar() {
        try {
            chatforum.leggTilKommentar(chatforum.getTemaTrykketPaa(), tekstKommentar.getText());
            tekstKommentar.setText("");
            chatforum.sorter();
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void likTema() {
        try {
            chatforum.likTema(chatforum.getTemaTrykketPaa());
            visLikTemaKnapper(true);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML 
    public void unlikTema() {
        try {
            chatforum.unlikTema(chatforum.getTemaTrykketPaa());
            visLikTemaKnapper(false);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML
    public void likKommentar() {
        try {
            chatforum.likKommentar(chatforum.getKommentarTrykketPaa());
            visLikKommentarKnapper(true);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML 
    public void unlikKommentar() {
        try {
            chatforum.unlikKommentar(chatforum.getKommentarTrykketPaa());
            visLikKommentarKnapper(false);
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    private void visLikTemaKnapper(Boolean b) {
        likTemaKnapp.visibleProperty().set(!b);
        unlikTemaKnapp.visibleProperty().set(b);
    }

    private void visLikKommentarKnapper(Boolean b) {
        likKommentarKnapp.visibleProperty().set(!b);
            unlikKommentarKnapp.visibleProperty().set(b);
    }

    @FXML
    public void slettTema() {
        try {
            chatforum.slettTema(chatforum.getTemaTrykketPaa());
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    @FXML 
    public void slettKommentar() {
        try {
            chatforum.slettKommentar(chatforum.getKommentarTrykketPaa());
            lagreChatforum();
            getChatforum();
        } catch (Exception e) {
            visFeilmelding(e.getLocalizedMessage());
        }
    }

    private void visFeilmelding(String feilmelding) {
        Alert alert=new Alert(AlertType.ERROR);
        alert.setTitle("Feilmelding");
        alert.setHeaderText(feilmelding);
        alert.showAndWait();
    }

    private void visBekreftelseMelding(String bekreftelse) {
        Alert alert=new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Bekreftelse");
        alert.setHeaderText(bekreftelse);
        alert.showAndWait();
    }

    private void oppdaterTemaer() {
        try {
            alleTemaer.getItems().clear();
            for (Tema tema : chatforum.getAlleTema()) {
                alleTemaer.getItems().add(tema);
            }
        } catch (NullPointerException e) {
            //Ikke gjør noe
        }
    }

    private void oppdaterKommentarer() {
        try {
            alleKommentarer.getItems().clear();
            for (Chat kommentar : chatforum.getTemaTrykketPaa().getAlleKommentarerTilTema()) {
                alleKommentarer.getItems().add(kommentar);
            }
        } catch (NullPointerException e) {
            //Ikke gjør noe
        }
    }

    private void lagreChatforum() {
        try {
            chatforumHandler.lagreProgram(chatforum, "lagring");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getChatforum() {
        try {
            chatforum=chatforumHandler.skrivProgram("lagring");
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        oppdaterTemaer();
        oppdaterKommentarer();
        alleTemaer.getSelectionModel().select(chatforum.getTemaTrykketPaa());
        alleKommentarer.getSelectionModel().select(chatforum.getKommentarTrykketPaa());
    }

}
