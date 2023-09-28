package chatforum;

import java.util.Comparator;

public class InnleggDatoEldstComparator implements Comparator<Innlegg> {

    @Override
    public int compare(Innlegg innlegg1, Innlegg innlegg2) {
        return innlegg1.getDato().compareTo(innlegg2.getDato());
    }
}