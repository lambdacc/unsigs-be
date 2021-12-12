package gimbalabs.unsigsbe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.utility.Iterate;
import org.eclipse.collections.impl.utility.ListIterate;
import org.eclipse.collections.impl.utility.MapIterate;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static gimbalabs.unsigsbe.Constants.*;


@Service
public class UnsigsServiceImpl implements UnsigsService {

    private final OfferRepository offerRepository;
    private final UnsigDetailsRepository unsigDetailsRepository;
    private final JacksonJsonParser jsonParser;
    private final ObjectMapper objectMapper;

    public UnsigsServiceImpl(OfferRepository offerRepository, UnsigDetailsRepository unsigDetailsRepository, JacksonJsonParser jsonParser, ObjectMapper objectMapper) {
        this.offerRepository = offerRepository;
        this.unsigDetailsRepository = unsigDetailsRepository;
        this.jsonParser = jsonParser;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> listOffers(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<OfferEntity> pagedResult = offerRepository.findAll(paging);
        Map<String, Object> resultMap = Util.newPagedResponseMap();
        if (pagedResult.isEmpty()) {
            return resultMap;
        }

        List<OfferEntity> content = pagedResult.getContent();
        MutableList<String> unsigIds = ListIterate.collect(content, e -> e.getUnsigId());
        MutableList<UnsigDto> unsigDtos = getUnsigs(unsigIds);

        MutableList<OfferDto> resultDtos = ListIterate.collectWith(content, (Function2<OfferEntity, MutableList<UnsigDto>, OfferDto>) (entity, dtos) -> {
            String id = entity.getUnsigId();
            UnsigDto d = dtos.detect(Predicates.attributeEqual(UnsigDto::getUnsigId, id));
            OfferDto offerDto = buildOfferDto(entity);
            offerDto.setDetails(d != null ? d.getDetails() : UnifiedMap.newMap());
            return offerDto;

        }, unsigDtos);

        resultMap.put(RESULT_LIST, resultDtos);
        resultMap.put(LIST_SIZE, resultDtos.size());
        resultMap.put(TOTAL_PAGES, pagedResult.getTotalPages());
        resultMap.put(HAS_NEXT_PAGE, pagedResult.hasNext());
        return resultMap;
    }

    @Override
    public Map<String, Object> listUnsigs(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<UnsigDetailsEntity> pagedResult = unsigDetailsRepository.findAll(paging);
        Map<String, Object> resultMap = Util.newPagedResponseMap();
        if (pagedResult.isEmpty()) {
            return resultMap;
        }

        MutableList<Object> content = ListIterate.collect(pagedResult.getContent(),
                e -> UnifiedMap.newWithKeysValues(
                        "unsigId", e.getUnsigId(),
                        "details", jsonParser.parseMap(e.getDetails())
                ));
        resultMap.put(RESULT_LIST, content);
        resultMap.put(LIST_SIZE, content.size());
        resultMap.put(TOTAL_PAGES, pagedResult.getTotalPages());
        resultMap.put(HAS_NEXT_PAGE, pagedResult.hasNext());
        return resultMap;
    }

    @Override
    public UnsigDto getUnsig(String unsigId) {
        UnsigDetailsEntity unsigE = unsigDetailsRepository.findByUnsigId(unsigId).orElseThrow();
        return new UnsigDto(unsigId, jsonParser.parseMap(unsigE.getDetails()));
    }

    @Override
    public MutableList<UnsigDto> getUnsigs(MutableList<String> unsigIds) {
        List<UnsigDetailsEntity> unsigEs = unsigDetailsRepository.findByUnsigIdIn(unsigIds);
        return ListIterate.collect(unsigEs, e -> new UnsigDto(e.getUnsigId(), jsonParser.parseMap(e.getDetails())));
    }

    private OfferDto buildOfferDto(OfferEntity e) {
        OfferDto dto = new OfferDto();
        BeanUtils.copyProperties(e, dto);
        return dto;
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
        for (RichIterable<UnsigDetails> unsigDetailsChunk : unsigDetailsList.chunk(300)) {
            Collection<String> unsigIds = Iterate.collect(unsigDetailsChunk, e -> e.unsigId);
            List<UnsigDetailsEntity> unsigEntitiesInDb = unsigDetailsRepository.findByUnsigIdIn(unsigIds);

            RichIterable<UnsigDetailsEntity> allToSave = unsigDetailsChunk.collect(
                    new Function<UnsigDetails, UnsigDetailsEntity>() {
                        @Override
                        public UnsigDetailsEntity valueOf(UnsigDetails details) {
                            UnsigDetailsEntity entity = ListIterate.detectOptional(unsigEntitiesInDb, e -> e.getUnsigId().equals(details.unsigId)).orElse(new UnsigDetailsEntity());
                            entity.setUnsigId(details.unsigId);
                            entity.setDetails(details.details);
                            return entity;
                        }
                    }
            );

            unsigDetailsRepository.saveAll(allToSave);
        }
        return true;
    }

    public boolean loadMasterDataOld() throws IOException {
        InputStream resourceAsStream = new ClassPathResource("unsigs-master.json").getInputStream();
        String contentString = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, Object> map = jsonParser.parseMap(contentString);

        List<Map> transactions = (List<Map>) ((Map) map.get("data")).get("transactions");
        MutableList<Map> allMetadata = ListIterate.flatCollect(transactions, e -> (List) e.get("metadata"));
        MutableList<Map> allValues = allMetadata.collect(e -> (Map) e.get("value"));
        MutableList<Map> allUnsigs = (MutableList<Map>) allValues.flatCollect(Map::values);


        MutableList<UnsigDetails> resultToStore = allUnsigs.collect(new Function<Map, UnsigDetails>() {
            @Override
            public UnsigDetails valueOf(Map m) {
                String unsigId = (String) m.keySet().iterator().next();
                UnsigDetails result = new UnsigDetails();
                result.unsigId = unsigId;
                try {
                    result.details = objectMapper.writeValueAsString(m.get(unsigId));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });

        return saveUnsigDetails(resultToStore);
    }

    @Override
    public boolean loadMasterData() throws IOException {
        InputStream resourceAsStream = new ClassPathResource("unsigs-master.json").getInputStream();
        String contentString = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, Object> map = jsonParser.parseMap(contentString);

        MutableList<UnsigDetails> resultToStore = FastList.newList();
        MapIterate.forEachKey(map, new Procedure<String>() {
            @Override
            public void value(String k) {
                Map<String, Object> detailsAsMap = (Map<String, Object>) map.get(k);
                String paddedStr = "00000" + k;
                String unsigId = paddedStr.substring(paddedStr.length() - 5);
                detailsAsMap.put("unsigId", unsigId);

                UnsigDetails unsigDetails = new UnsigDetails();
                unsigDetails.unsigId = unsigId;
                try {
                    unsigDetails.details = objectMapper.writeValueAsString(detailsAsMap);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                resultToStore.add(unsigDetails);
            }
        });

        return saveUnsigDetails(resultToStore);
    }
}
