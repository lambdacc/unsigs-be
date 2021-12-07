package gimbalabs.unsigsbe;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.utility.Iterate;
import org.eclipse.collections.impl.utility.ListIterate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static gimbalabs.unsigsbe.Constants.*;


@Service
public class UnsigsServiceImpl implements UnsigsService {

    private final OfferRepository offerRepository;
    private final UnsigDetailsRepository unsigDetailsRepository;

    public UnsigsServiceImpl(OfferRepository offerRepository, UnsigDetailsRepository unsigDetailsRepository) {
        this.offerRepository = offerRepository;
        this.unsigDetailsRepository = unsigDetailsRepository;
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
        OfferEntity offerE;
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

    @Override
    public boolean saveUnsigDetails(MutableList<UnsigDetails> unsigDetailsList) {
        for (RichIterable<UnsigDetails> unsigDetailsChunk : unsigDetailsList.chunk(80)) {
            Collection<String> unsigIds = Iterate.collect(unsigDetailsChunk, e -> e.unsigId);
            List<UnsigDetailsEntity> unsigEntitiesInDb = unsigDetailsRepository.findByUnsigIdIn(unsigIds);

            RichIterable<UnsigDetailsEntity> allToSave = unsigDetailsChunk.collect(new Function<UnsigDetails, UnsigDetailsEntity>() {
                                                                                     @Override
                                                                                     public UnsigDetailsEntity valueOf(UnsigDetails details) {
                                                                                         UnsigDetailsEntity entity = ListIterate.detectOptional(unsigEntitiesInDb, e -> e.getUnsigId().equals(details.unsigId)).orElse(new UnsigDetailsEntity());
                                                                                         BeanUtils.copyProperties(details, entity);
                                                                                         return entity;
                                                                                     }
                                                                                 }
            );

            unsigDetailsRepository.saveAll(allToSave);
        }
        return true;
    }
}
