package chatforum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Chatforum {
    
    private Bruker innloggetBruker;
    private Tema temaTrykketPaa;
    private Chat kommentarTrykketPaa;
    private List<Bruker> alleBrukere=new ArrayList<>();
    private List<Tema> alleTema=new ArrayList<>();
    private boolean sorterDato=true;

    public Bruker getInnloggetBruker() {
        return innloggetBruker;
    }

    protected void setInnloggetBrukerNull() {
        this.innloggetBruker=null;
    }

    public Tema getTemaTrykketPaa() {
        return temaTrykketPaa;
    }

    public void setTemaTrykketPaa(Tema temaTrykketPaa) {
        sjekkTemaEksisterer(temaTrykketPaa);
        this.temaTrykketPaa=temaTrykketPaa;
    }

    public Chat getKommentarTrykketPaa() {
        return kommentarTrykketPaa;
    }

    public void setKommentarTrykketPaa(Chat kommentarTrykketPaa) {
        sjekkTemaEksisterer(kommentarTrykketPaa.getTema());
        kommentarTrykketPaa.getTema().sjekkKommentarEksisterer(kommentarTrykketPaa);
        this.kommentarTrykketPaa=kommentarTrykketPaa;
    }

    public void brukerLoggInn(String brukernavn, String passord) {
        for (Bruker bruker : alleBrukere) {
            if (bruker.getBrukernavn().equals(brukernavn)) {
                this.innloggetBruker=bruker;
            }
        }
        if (!sjekkBrukerEksisterer(innloggetBruker)) {
            throw new IllegalArgumentException("Brukernavnet '"+brukernavn+"' eksisterer ikke");
        }
        innloggetBruker.testRiktigPassord(passord);
    }

    public void brukerLoggUt() {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Man må være logget inn for å kunne logge ut");
        }
        innloggetBruker=null;
    }

    public boolean sjekkBrukerLoggetInn() {
        return innloggetBruker!=null;
    }

    public List<Bruker> getAlleBrukere() {
        return new ArrayList<>(alleBrukere);
    }

    public void leggTilNyBruker(String brukernavn, String epost, String passord) {
        if (sjekkBrukernavnEksisterer(brukernavn)) {
            throw new IllegalArgumentException("Brukernavnet '"+brukernavn+"' ekisterer allerede");
        }
        alleBrukere.add(new Bruker(brukernavn, epost, passord));
    }

    public void leggTilBruker(Bruker bruker) {
        if (sjekkBrukerEksisterer(bruker)) {
            throw new IllegalArgumentException("Brukeren '"+bruker+"' ekisterer allerede");
        }
        if (sjekkBrukernavnEksisterer(bruker.getBrukernavn())) {
            throw new IllegalArgumentException("Brukernavnet '"+bruker.getBrukernavn()+"' ekisterer allerede");
        }
        alleBrukere.add(bruker);
    }

    public void slettBruker(String passord) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for slette brukeren");
        }
        if (!sjekkBrukerEksisterer(innloggetBruker)) {
            throw new IllegalStateException("Bruker eksisterer ikke");
        }
        innloggetBruker.testRiktigPassord(passord);

        for (Tema tema : getAlleTema()) {
            if (tema.getBruker().equals(innloggetBruker)) {
                alleTema.remove(tema);
            }
            try {
                tema.unlikeTema(innloggetBruker);
            } catch (Exception e) {
                //Gå videre dersom brukeren ikke har likt temaet
            }
            for (Chat kommentar : tema.getAlleKommentarerTilTema()) {
                if (kommentar.getBruker().equals(innloggetBruker)) {
                    tema.slettKommentarTilTema(kommentar);
                }
                try {
                    kommentar.unlikeKommentar(innloggetBruker);
                } catch (Exception e) {
                    //Gå videre dersom brukeren ikke har likt kommentaren
                }
            }
        }
        alleBrukere.remove(innloggetBruker);
        innloggetBruker=null;
    }

    public void endreBrukernavn(String brukernavn) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Logg inn for å endre navn");
        }
        if (sjekkBrukernavnEksisterer(brukernavn)) {
            throw new IllegalArgumentException("Brukernavnet '"+brukernavn+"' eksisterer allerede");
        }
        innloggetBruker.endreBrukeravn(brukernavn);
    }

    public void endrePassord(String gammeltPassord, String nyttPassord) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Logg inn for å endre passord");
        }
        innloggetBruker.endrePassord(gammeltPassord, nyttPassord);
    }

    public boolean sjekkBrukerEksisterer(Bruker bruker) {
        return alleBrukere.contains(bruker);
    }

    public boolean sjekkBrukernavnEksisterer(String brukernavn) {
        return alleBrukere.stream().anyMatch((bruker) -> bruker.getBrukernavn().equals(brukernavn));
    }

    public List<Tema> getAlleTema() {
        return new ArrayList<>(alleTema);
    }

    public void leggTilTema(String overskrift, String tekst) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å chatte");
        }
        Tema tema=new Tema(innloggetBruker, overskrift, tekst);
        alleTema.add(tema);
        setTemaTrykketPaa(tema);
    }

    protected void leggTilEksisterendeTema(Tema tema) {
        if (!sjekkBrukerEksisterer(tema.getBruker())) {
            throw new IllegalArgumentException("Bruker eksisterer ikke");
        }
        sjekkOmBrukereSomLikerEksisterer(tema);
        alleTema.add(tema);
    }

    public void slettTema(Tema tema) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å slette temaer");
        }
        if (tema==null) {
            throw new IllegalArgumentException("Velg et tema å slette");
        }
        if (!innloggetBruker.equals(tema.getBruker())) {
            throw new IllegalArgumentException("Du kan kun slette dine egne temaer");
        }
        sjekkTemaEksisterer(tema);
        alleTema.remove(tema);
    }

    public void leggTilKommentar(Tema tema, String tekst) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å kommentere");
        }
        if (tema==null) {
            throw new NullPointerException("Du må velg et tema du vil kommentere");
        }
        sjekkTemaEksisterer(tema);
        tema.leggTilKommentarTilTema(innloggetBruker, tekst);
    }

    protected void leggTilEksisterendeKommentar(Chat kommentar) {
        if (!sjekkBrukerEksisterer(kommentar.getBruker())) {
            throw new IllegalArgumentException("Bruker eksisterer ikke");
        }
        sjekkTemaEksisterer(kommentar.getTema());
        sjekkOmBrukereSomLikerEksisterer(kommentar);
        kommentar.getTema().leggTilEksisterendeKommentarTilTema(kommentar);
    }

    private void sjekkOmBrukereSomLikerEksisterer(Innlegg innlegg) {
        if (!innlegg.getLikesBrukere().isEmpty()) {
            if (innlegg.getLikesBrukere().stream().anyMatch((bruker) -> !sjekkBrukerEksisterer(bruker))) {
                throw new IllegalArgumentException("Listen med folk som liker kommentaren er ikke i chatforumet");
            }
        }
    }

    public void slettKommentar(Chat kommentar) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å slette kommentarer");
        }
        if (kommentar==null) {
            throw new IllegalArgumentException("Velg en kommentar å slette");
        }
        //Oppretteren av temaet kan også slette kommentarer
        if (!innloggetBruker.equals(kommentar.getBruker()) && !innloggetBruker.equals(kommentar.getTema().getBruker())) {
            throw new IllegalArgumentException("Du kan kun slette dine egne kommentarer");
        }
        kommentar.getTema().slettKommentarTilTema(kommentar);
    }

    public void sjekkTemaEksisterer(Tema tema) {
        if (!getAlleTema().contains(tema)) {
            throw new IllegalArgumentException("Velg et tema i chatforumet");
        }
    }

    public void likTema(Tema tema) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å like tema");
        }
        sjekkTemaEksisterer(tema);

        tema.likeTema(innloggetBruker);
    }

    public void unlikTema(Tema tema) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å unlike tema");
        }
        sjekkTemaEksisterer(tema);
        if (tema.getBruker().equals(innloggetBruker)) {
            throw new IllegalArgumentException("Du kan ikke unlike ditt eget tema");
        }

        tema.unlikeTema(innloggetBruker);
    }

    public void likKommentar(Chat kommentar) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å like kommentarer");
        }
        kommentar.getTema().sjekkKommentarEksisterer(kommentar);

        kommentar.likeKommentar(innloggetBruker);
    }

    public void unlikKommentar(Chat kommentar) {
        if (!sjekkBrukerLoggetInn()) {
            throw new IllegalStateException("Du må være innlogget for å unlike kommentarer");
        }
        kommentar.getTema().sjekkKommentarEksisterer(kommentar);

        kommentar.unlikeKommentar(innloggetBruker);
    }

    public void sorter() {
        if (sorterDato) {
            sorterDato();
        }
        else {
            sorterLikes();
        }
    }

    public void sorterDato() {
        sorterDato=true;
        sorterTema(new InnleggDatoNyestComparator());
        sorterKommentar(new InnleggDatoEldstComparator());
    }

    public void sorterLikes() {
        sorterDato=false;
        sorterTema(new InnleggLikesComparator());
        sorterKommentar(new InnleggLikesComparator());
    }

    private void sorterTema(Comparator<Innlegg> comparator) {
        alleTema.sort(comparator);
    }

    private void sorterKommentar(Comparator<Innlegg> comparator) {
        for (Tema tema : alleTema) {
            tema.sorterKommentarerTilTema(comparator);
        }
    }
}
