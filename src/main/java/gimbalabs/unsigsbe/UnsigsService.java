package gimbalabs.unsigsbe;

import java.util.Map;

public interface UnsigsService {
    Map<String, Object> listOffers(Integer pageNo, Integer pageSize);

    OfferEntity saveOffer(Offer offer);
}
