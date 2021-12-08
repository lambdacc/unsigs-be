package gimbalabs.unsigsbe;

import org.eclipse.collections.api.list.MutableList;

import java.util.Map;

public interface UnsigsService {
    Map<String, Object> listOffers(Integer pageNo, Integer pageSize);

    Map<String, Object> listUnsigs(Integer pageNo, Integer pageSize);

    UnsigDetailsEntity getUnsig(String unsigId);

    OfferEntity saveOffer(Offer offer);

    boolean saveUnsigDetails(MutableList<UnsigDetails> unsigDetailsList);
}
