package chatforum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ChatforumHandler implements IChatforumHandler {
    
    public void lagreProgram(Chatforum chatforum, String filename) throws FileNotFoundException {
        try(PrintWriter skriver=new PrintWriter(new File(getFilePath(filename)))) {

            for (Bruker bruker : chatforum.getAlleBrukere()) {
                skriver.println("***BRUKER***");
                skriver.println(bruker.getBrukernavn());
                skriver.println(bruker.getEpost());
                skriver.println(bruker.getPassord());

            }

            skriver.println("***INNLOGGET_BRUKER***");
            if (chatforum.getInnloggetBruker()==null) {
                skriver.println("**********NULL**********");
            }
            else {
                skriver.println(chatforum.getInnloggetBruker());
            }

            for (Tema tema : chatforum.getAlleTema()) {
                skriver.println("***NYTT_TEMA***");
                skriver.println(tema.getBruker());
                skriver.println(tema.getOverskrift());
                skriver.println("___OVERSKRIFTSLUTT___");
                skriver.println(tema.getTekst());
                skriver.println("___TEKSTSLUTT___");
                skriver.println(tema.getFunksjonellDato());
                for (Bruker bruker : tema.getLikesBrukere()) {
                    skriver.print(bruker+",");
                }
                skriver.println();
                if (tema==chatforum.getTemaTrykketPaa()) {
                    skriver.println("___TRYKKET___");
                }
                else {
                    skriver.println("___IKKE_TRYKKET___");
                }

                for (Chat kommentar : tema.getAlleKommentarerTilTema()) {
                    skriver.println("***NY_KOMMENTAR***");
                    skriver.println(kommentar.getBruker());
                    skriver.println(kommentar.getTekst());
                    skriver.println("___TEKSTSLUTT___");
                    skriver.println(kommentar.getFunksjonellDato());
                    for (Bruker bruker : kommentar.getLikesBrukere()) {
                        skriver.print(bruker+",");
                    }
                    skriver.println();
                    if (kommentar==chatforum.getKommentarTrykketPaa()) {
                        skriver.println("___TRYKKET___");
                    }
                    else {
                        skriver.println("___IKKE_TRYKKET___");
                    }
                }
            }
            skriver.println("***SLUTT***");
        }
    }

    public Chatforum skrivProgram(String filename) throws FileNotFoundException {
        try(Scanner leser=new Scanner(new File(getFilePath(filename)))) {
            Chatforum chatforum=new Chatforum();
            
            //Brukere
            while (true) {
                try {
                    if (leser.nextLine().equals("***INNLOGGET_BRUKER***")) {
                             break;
                    }
                    String brukernavn=leser.nextLine();
                    String epost=leser.nextLine();
                    String passord=leser.nextLine();
                    chatforum.leggTilNyBruker(brukernavn, epost, passord);
                }
                catch (NoSuchElementException e) {
                    break;
                }
            }

            //Innlogget bruker
            Bruker brukerSomSkalLoggesInn=finnBruker(chatforum, leser.nextLine());
            if (brukerSomSkalLoggesInn==null) {
                chatforum.setInnloggetBrukerNull();
            }
            else {
                chatforum.brukerLoggInn(brukerSomSkalLoggesInn.getBrukernavn(), brukerSomSkalLoggesInn.getPassord());
            }

            //Tema og kommentarer
            String mellomLinje=leser.nextLine();
            while (true) {
                try {
                    if (!mellomLinje.equals("***NYTT_TEMA***")) {
                        break;
                    }

                    //Les tema
                    String brukernavn=leser.nextLine();
                    String overskrift="";
                    String delOverskrift=leser.nextLine();
                    while(!delOverskrift.equals("___OVERSKRIFTSLUTT___")) {
                        overskrift+=delOverskrift;
                        delOverskrift=leser.nextLine();
                        if (!delOverskrift.equals("___OVERSKRIFTSLUTT___")) {
                            overskrift+="\n";
                        }
                    }
                    String tekst="";
                    String delTekst=leser.nextLine();
                    while(!delTekst.equals("___TEKSTSLUTT___")) {
                        tekst+=delTekst;
                        delTekst=leser.nextLine();
                        if (!delTekst.equals("___TEKSTSLUTT___")) {
                            tekst+="\n";
                        }
                    }
                    String dato=leser.nextLine();
                    String likesBrukereStreng=leser.nextLine();
                    String temaTrykketPaa=leser.nextLine();
                        
                    //Finn bruker, dato, likes og legg til tema i chatforumet
                    Bruker bruker=finnBruker(chatforum, brukernavn);
                    String[] datoSplittet=dato.split(",");
                    List<Bruker> likesBrukere=lagLikesBrukereListe(chatforum, likesBrukereStreng);
                    Tema tema=new Tema(bruker, overskrift, tekst, Integer.parseInt(datoSplittet[0]), Integer.parseInt(datoSplittet[1]), Integer.parseInt(datoSplittet[2]), Integer.parseInt(datoSplittet[3]), Integer.parseInt(datoSplittet[4]), Integer.parseInt(datoSplittet[5]), likesBrukere);
                    chatforum.leggTilEksisterendeTema(tema);

                    //Bestem om tema er trykket på
                    if (temaTrykketPaa.equals("___TRYKKET___")) {
                        chatforum.setTemaTrykketPaa(tema);
                    }

                    //Les alle kommentarer til tema
                    while (true) {
                        mellomLinje=leser.nextLine();
                        if (!mellomLinje.equals("***NY_KOMMENTAR***")) {
                            break;
                        }
                        String brukernavnKommentar=leser.nextLine();
                        String tekstKommentar="";
                        String delTekstKommentar=leser.nextLine();
                        while(!delTekstKommentar.equals("___TEKSTSLUTT___")) {
                            tekstKommentar+=delTekstKommentar;
                            delTekstKommentar=leser.nextLine();
                            if (!delTekstKommentar.equals("___TEKSTSLUTT___")) {
                                tekstKommentar+="\n";
                            }
                        }
                        String datoKommentar=leser.nextLine();
                        String likesBrukereStrengKommentar=leser.nextLine();
                        String kommentarTrykketPaa=leser.nextLine();

                        //Finn bruker, dato, likes og legg til kommentar i chatforumet
                        Bruker brukerKommentar=finnBruker(chatforum, brukernavnKommentar);
                        String[] datoSplittetKommentar=datoKommentar.split(",");
                        List<Bruker> likesBrukereKommentar=lagLikesBrukereListe(chatforum, likesBrukereStrengKommentar);
                        Chat kommentar=new Chat(tema, brukerKommentar, tekstKommentar, Integer.parseInt(datoSplittetKommentar[0]), Integer.parseInt(datoSplittetKommentar[1]), Integer.parseInt(datoSplittetKommentar[2]), Integer.parseInt(datoSplittetKommentar[3]), Integer.parseInt(datoSplittetKommentar[4]), Integer.parseInt(datoSplittetKommentar[5]), likesBrukereKommentar);
                        chatforum.leggTilEksisterendeKommentar(kommentar);

                        //Bestem om kommentar er trykket på
                        if (kommentarTrykketPaa.equals("___TRYKKET___")) {
                            chatforum.setKommentarTrykketPaa(kommentar);
                        }
                    }
                    
                }
                catch (NoSuchElementException e) {
                    break;
                }
            }
            return chatforum;
        }
    }

    private List<Bruker> lagLikesBrukereListe(Chatforum chatforum, String likesBrukere) {
        List<Bruker> nyLikesBrukere=new ArrayList<>();
        if (likesBrukere=="") {
            return nyLikesBrukere;
        }

        String[] splitLikesBrukere=likesBrukere.split(",");
        for (int i = 0; i < splitLikesBrukere.length; i++) {
            nyLikesBrukere.add(finnBruker(chatforum, splitLikesBrukere[i]));
        }

        return nyLikesBrukere;
    }

    private Bruker finnBruker(Chatforum chatforum, String brukernavn) {
        if (brukernavn.equals("**********NULL**********")) {
            return null;
        }
        for (Bruker bruker : chatforum.getAlleBrukere()) {
            if (bruker.getBrukernavn().equals(brukernavn)) {
                return bruker;
            }
        }
        throw new Error("Brukeren finnes ikke");
    }

    public static String getFilePath(String filename) {
        return ChatforumHandler.class.getResource("").getFile() + filename + ".txt";
    }

    public static void main(String[] args) throws FileNotFoundException {
        
        //Main funksjonen i ChatforumHandler kan kjøres for å skrive et Chatforum program til fil, og deretter åpne med ChatforumApp.

        ChatforumHandler handler=new ChatforumHandler();
        Chatforum chatforum=new Chatforum();
        Bruker bruker1=new Bruker("peter", "peter@gmail.com", "Passord1!");
        Bruker bruker2=new Bruker("anne", "anne@gmail.com", "Passord1!");
        Bruker bruker3=new Bruker("olav", "olav@gmail.com", "Passord1!");
        Bruker bruker4=new Bruker("isak", "isak@gmail.com", "Passord1!");
        Bruker bruker5=new Bruker("marte", "marte@gmail.com", "Passord1!");
        chatforum.leggTilBruker(bruker1);
        chatforum.leggTilBruker(bruker2);
        chatforum.leggTilBruker(bruker3);
        chatforum.leggTilBruker(bruker4);
        chatforum.leggTilBruker(bruker5);
        
        Tema tema1=new Tema(bruker1, "Her er overskrift", "Dette er tekst", 2022, 04, 26, 20, 30, 20, new ArrayList<>());
        Tema tema2=new Tema(bruker1, "overskrift", "tekst", 2022, 04, 27, 10, 24, 02, new ArrayList<>());
        Tema tema3=new Tema(bruker2, "overskrift", "tekst", 2022, 04, 27, 19, 21, 20, new ArrayList<>());
        Chat chat1=new Chat(tema1, bruker1, "Kommentar til temaet mitt", 2022, 04, 26, 21, 31, 00, new ArrayList<>());
        Chat chat2=new Chat(tema1, bruker2, "Ny kommentar", 2022, 04, 26, 22, 04, 00, new ArrayList<>());
        Chat chat3=new Chat(tema1, bruker3, "En til kommentar", 2022, 04, 26, 23, 21, 00, new ArrayList<>());
        Chat chat4=new Chat(tema2, bruker4, "Kommentar til annet tema", 2022, 04, 27, 12, 21, 00, new ArrayList<>());
        Chat chat5=new Chat(tema2, bruker5, "Kommentar nr 2", 2022, 04, 28, 10, 21, 00, new ArrayList<>());
        chatforum.leggTilEksisterendeTema(tema1);
        chatforum.leggTilEksisterendeTema(tema2);
        chatforum.leggTilEksisterendeTema(tema3);
        chatforum.leggTilEksisterendeKommentar(chat1);
        chatforum.leggTilEksisterendeKommentar(chat2);
        chatforum.leggTilEksisterendeKommentar(chat3);
        chatforum.leggTilEksisterendeKommentar(chat4);
        chatforum.leggTilEksisterendeKommentar(chat5);

        chatforum.brukerLoggInn("marte", "Passord1!");
        chatforum.likTema(tema1);
        chatforum.likTema(tema2);
        chatforum.likKommentar(chat2);
        chatforum.likKommentar(chat3);
        chatforum.brukerLoggUt();

        chatforum.brukerLoggInn("anne", "Passord1!");
        chatforum.likTema(tema2);
        chatforum.likKommentar(chat5);

        handler.lagreProgram(chatforum, "lagring");
    }
}
