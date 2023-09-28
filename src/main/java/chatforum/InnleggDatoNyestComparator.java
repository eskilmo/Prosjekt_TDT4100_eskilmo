package chatforum;

import java.util.Comparator;

public class InnleggDatoNyestComparator implements Comparator<Innlegg> {

    @Override
    public int compare(Innlegg innlegg1, Innlegg innlegg2) {
        return innlegg2.getDato().compareTo(innlegg1.getDato());
    }
}
