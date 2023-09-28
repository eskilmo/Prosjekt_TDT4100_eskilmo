package chatforum;

import java.util.Comparator;

public class InnleggLikesComparator implements Comparator<Innlegg> {

    @Override
    public int compare(Innlegg innlegg1, Innlegg innlegg2) {
        return innlegg2.getLikes()-innlegg1.getLikes();
    }
}
