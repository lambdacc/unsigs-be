package gimbalabs.unsigsbe;

import org.springframework.data.domain.Page;

public interface UnsigsService {
    Page<OfferEntity> listOffers(Integer pageNo, Integer pageSize);

    OfferEntity saveOffer(Offer offer);
}
