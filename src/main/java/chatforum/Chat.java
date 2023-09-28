package chatforum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Innlegg {
    private Tema tema;
    private Bruker bruker;
    private String tekst;
    private LocalDateTime dato;
    private List<Bruker> likesBrukere=new ArrayList<>();
    
    public Chat(Tema tema, Bruker bruker, String tekst) {
        testNullInput(tekst);
        this.tema=tema;
        this.bruker = bruker;
        this.tekst = tekst;
        this.dato=LocalDateTime.now();
    }

    public Chat(Tema tema, Bruker bruker, String tekst, int yyyy, int MM, int dd, int HH, int mm, int ss, List<Bruker> likesBrukere) {
        testNullInput(tekst);
        testLovligDato(yyyy, MM, dd, HH, mm, ss);
        this.tema=tema;
        this.bruker = bruker;
        this.tekst = tekst;
        if (LocalDateTime.of(yyyy, MM, dd, HH, mm, ss).isBefore(tema.getDato())) {
            throw new IllegalArgumentException("Kommentaren kan ikke ha bli opprettet før temaet");
        }
        this.dato=LocalDateTime.of(yyyy, MM, dd, HH, mm, ss);
        this.likesBrukere=likesBrukere;
    }

    public Tema getTema() {
        return tema;
    }

    public Bruker getBruker() {
        return bruker;
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

    public void likeKommentar(Bruker bruker) {
        if (this.bruker.equals(bruker)) {
            throw new IllegalArgumentException("Du kan ikke like din egen kommentar");
        }
        if (likesBrukere.contains(bruker)) {
            throw new IllegalArgumentException("Du har allerede likt denne kommentaren");
        }
        likesBrukere.add(bruker);
    }

    public void unlikeKommentar(Bruker bruker) {
        if (this.bruker.equals(bruker)) {
            throw new IllegalArgumentException("Du kan ikke unlike din egen kommentar");
        }
        if (!likesBrukere.contains(bruker)) {
            throw new IllegalArgumentException("Du har ikke likt denne kommentaren");
        }
        likesBrukere.remove(bruker);
    }

    public boolean sjekkKommentarLikt(Bruker bruker) {
        return likesBrukere.contains(bruker);
    }

    @Override
    public String toString() {
        return getTekst()+"\n"+getBruker()+"\t"+getPrettyDato()+"\t\t"+"Likes: "+getLikes();
    }    
}
