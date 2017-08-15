package DBService.model.collections;


import DBService.model.forOtherServices.InformationAboutSession;

import java.util.List;

/**
 * Created by Nik on 26.06.2017.
 */
@Deprecated
public class CollectionOfInformationAboutSession {
    List<InformationAboutSession> collection;

    public List<InformationAboutSession> getCollection() {
        return collection;
    }
}
