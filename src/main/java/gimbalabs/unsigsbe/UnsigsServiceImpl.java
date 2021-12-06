package gimbalabs.unsigsbe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnsigsServiceImpl implements UnsigsService {

    private final OfferRepository offerRepository;

    public UnsigsServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public Page<OfferEntity> listOffers(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return offerRepository.findAll(paging);
    }

    @Override
    public OfferEntity saveOffer(Offer offer) {
        Optional<OfferEntity> offerOp = offerRepository.findByUnsigId(offer.unsigId);
        OfferEntity offerE = null;
        if (offerOp.isPresent()) {
            offerE = offerOp.get();

        } else {
            offerE = new OfferEntity();
            offerE.setUnsigId(offer.unsigId);
        }
        offerE.setOwner(offer.owner);
        offerE.setAmount(offer.amount);

        return offerRepository.save(offerE);
    }
}
