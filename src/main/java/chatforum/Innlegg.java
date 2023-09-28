package chatforum;

import java.time.LocalDateTime;
import java.util.List;

public interface Innlegg {

    default void testNullInput(String streng) {
        streng=streng.replaceAll("[\\n\\t ]", "");
        if (streng.equals("")) {
            throw new IllegalArgumentException("Skriv noe i meldingen");
        }
    }

    default void testLovligDato(int yyyy, int MM, int dd, int HH, int mm, int ss) {
        LocalDateTime dato=LocalDateTime.of(yyyy, MM, dd, HH, mm, ss);
        if (yyyy<2022) {
            throw new IllegalArgumentException("Kan ikke legge til kommentar fra før 2022");
        }
        if (dato.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Kan ikke legge til tema fra fremtiden");
        }
    }
    
    Bruker getBruker();

    String getTekst();

    LocalDateTime getDato();

    String getPrettyDato();

    String getFunksjonellDato();

    int getLikes();

    List<Bruker> getLikesBrukere();

}
