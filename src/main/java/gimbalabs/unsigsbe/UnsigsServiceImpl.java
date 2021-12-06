package gimbalabs.unsigsbe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static gimbalabs.unsigsbe.Constants.*;


@Service
public class UnsigsServiceImpl implements UnsigsService {

    private final OfferRepository offerRepository;

    public UnsigsServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public Map<String, Object> listOffers(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<OfferEntity> pagedResult = offerRepository.findAll(paging);
        Map<String, Object> resultMap = Util.newPagedResponseMap();
        if (pagedResult.isEmpty()) {
            return resultMap;
        }

        List<Offer> content = pagedResult.stream().map(this::buildOfferDto).collect(Collectors.toList());
        resultMap.put(RESULT_LIST, content);
        resultMap.put(LIST_SIZE, content.size());
        resultMap.put(TOTAL_PAGES, pagedResult.getTotalPages());
        resultMap.put(HAS_NEXT_PAGE, pagedResult.hasNext());
        return resultMap;
    }

    private Offer buildOfferDto(OfferEntity e) {
        Offer o = new Offer();
        o.unsigId = e.getUnsigId();
        o.owner = e.getOwner();
        o.amount = e.getAmount();
        return o;
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
