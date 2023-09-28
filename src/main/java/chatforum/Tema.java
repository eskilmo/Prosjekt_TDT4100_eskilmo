package chatforum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tema implements Innlegg {
    private Bruker bruker;
    private String overskrift;
    private String tekst;
    private LocalDateTime dato;
    private List<Bruker> likesBrukere=new ArrayList<>();
    private List<Chat> alleKommentarerTilTema=new ArrayList<>();
    
    public Tema(Bruker bruker, String overskrift, String tekst) {
        testNullInput(overskrift);
        testNullInput(tekst);
        this.bruker = bruker;
        this.overskrift=overskrift;
        this.tekst = tekst;
        this.dato=LocalDateTime.now();
    }

    public Tema(Bruker bruker, String overskrift, String tekst, int yyyy, int MM, int dd, int HH, int mm, int ss, List<Bruker> likesBrukere) {
        testNullInput(tekst);
        testLovligDato(yyyy, MM, dd, HH, mm, ss);
        this.bruker = bruker;
        this.overskrift=overskrift;
        this.tekst = tekst;
        this.dato=LocalDateTime.of(yyyy, MM, dd, HH, mm, ss);
        this.likesBrukere=likesBrukere;
    }

    public Bruker getBruker() {
        return bruker;
    }

    public String getOverskrift() {
        return overskrift;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDateTime getDato() {
        return dato;
    }

    public String getPrettyDato() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dato.format(formatter);
    }

    public String getFunksjonellDato() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss");
        return dato.format(formatter);
    }

    public int getLikes() {
        return likesBrukere.size();
    }

    public List<Bruker> getLikesBrukere() {
        return new ArrayList<>(likesBrukere);
    }

    public void likeTema(Bruker bruker) {
        if (this.bruker.equals(bruker)) {
            throw new IllegalArgumentException("Du kan ikke like ditt eget tema");
        }
        if (likesBrukere.contains(bruker)) {
            throw new IllegalArgumentException("Du har allerede likt dette temaet");
        }
        likesBrukere.add(bruker);
    }

    public void unlikeTema(Bruker bruker) {
        if (this.bruker.equals(bruker)) {
            throw new IllegalArgumentException("Du kan ikke unlike ditt eget tema");
        }
        if (!likesBrukere.contains(bruker)) {
            throw new IllegalArgumentException("Du har ikke likt dette temaet");
        }
        likesBrukere.remove(bruker);
    }

    public boolean sjekkTemaLikt(Bruker bruker) {
        return likesBrukere.contains(bruker);
    }

    public List<Chat> getAlleKommentarerTilTema() {
        return new ArrayList<>(alleKommentarerTilTema);
    }

    public void leggTilKommentarTilTema(Bruker bruker, String tekst) {
        alleKommentarerTilTema.add(new Chat(this, bruker, tekst));
    }

    public void leggTilEksisterendeKommentarTilTema(Chat kommentar) {
        if (alleKommentarerTilTema.contains(kommentar)) {
            throw new IllegalArgumentException("Kommentaren er allerede lagt til i temaet");
        }
        alleKommentarerTilTema.add(kommentar);
    }

    public void slettKommentarTilTema(Chat kommentar) {
        if (!alleKommentarerTilTema.contains(kommentar)) {
            throw new IllegalArgumentException("Kommentaren er ikke lagt til i temaet");
        }
        alleKommentarerTilTema.remove(kommentar);
    }

    public void sjekkKommentarEksisterer(Chat kommentar) {
        if (!getAlleKommentarerTilTema().contains(kommentar)) {
            throw new IllegalArgumentException("Velg en kommentar å like");
        }
    }

    public void sorterKommentarerTilTema(Comparator<Innlegg> comparator) {
        alleKommentarerTilTema.sort(comparator);
    }

    @Override
    public String toString() {
        return getOverskrift()+"\n"+getTekst()+"\n"+getBruker()+"\t"+getPrettyDato()+"\t\t"+"Likes: "+getLikes();
    }
}
