package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.entity.OfferEntity;
import gimbalabs.unsigsbe.entity.UnsigDetailsEntity;
import gimbalabs.unsigsbe.model.AssetAddressUtxo;
import gimbalabs.unsigsbe.model.Offer;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.utility.ListIterate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static gimbalabs.unsigsbe.Constants.RESULT_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class UnsigsControllerIT extends UnsigsBeApplicationTests {

    @Autowired
    OfferRepository offerRepository;
    @Autowired
    UnsigsService unsigsService;
    @Autowired
    BlockfrostAdapter blockfrostAdapter;
    @Autowired
    UnsigDetailsRepository unsigDetailsRepository;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        JacksonTester.initFields(this, objectMapper);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void givenNonExistentUnsig_whenCreateOffer_thenNotFound() throws Exception {

        Offer o = newOffer(randomId(), 1020L, randomString());

        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();
    }

    @Test
    public void whenCreateNewOffer_thenOk() throws Exception {

        String unsigId = unsigDetailsRepository.findAll(PageRequest.of(10, 10)).getContent().get(6).getUnsigId();
        Offer o = newOffer(unsigId, 1020L, randomString());

        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();
    }

    @Test
    public void whenDeleteOffer_thenOk() throws Exception {

        String unsigId = unsigDetailsRepository.findAll(PageRequest.of(10, 10)).getContent().get(1).getUnsigId();
        long amt = 45678L;
        Offer o = newOffer(unsigId, amt, randomString());

        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        OfferEntity byUnsigId = offerRepository.findByUnsigId(unsigId).orElseThrow();
        assertNotNull(byUnsigId);
        assertEquals(amt, byUnsigId.getAmount());


        response = mockMvc.perform(
                        delete("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());
        assertEquals(unsigId, map.get("unsigId"));


        Optional<OfferEntity> unsigOp = offerRepository.findByUnsigId(unsigId);
        assertTrue(unsigOp.isEmpty());
    }

    @Test
    public void givenPropertiesNotMatch_whenDeleteOffer_thenForbidden() throws Exception {

        String unsigId = unsigDetailsRepository.findAll(PageRequest.of(10, 10)).getContent().get(1).getUnsigId();
        long amt = 45678L;
        Offer o = newOffer(unsigId, amt, randomString());

        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        OfferEntity byUnsigId = offerRepository.findByUnsigId(unsigId).orElseThrow();
        assertNotNull(byUnsigId);
        assertEquals(amt, byUnsigId.getAmount());


        o.owner = randomString();
        response = mockMvc.perform(
                        delete("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

    }

    private String randomId() {
        return String.valueOf(Math.abs(new Random().nextLong()));
    }

    private Offer newOffer(String unsigId, Long amount, String owner) {
        Offer o = new Offer();
        o.unsigId = unsigId;
        o.owner = owner;
        o.amount = amount;
        o.txHash = randomString();
        o.txIndex = Math.abs(new Random().nextInt());
        o.datumHash = randomString();
        return o;
    }

    @Test
    public void whenCreateAndListOffers_thenOk() throws Exception {
        offerRepository.deleteAll();
        Page<UnsigDetailsEntity> firstTen = unsigDetailsRepository.findAll(PageRequest.of(1, 10));
        List<UnsigDetailsEntity> content = new ArrayList(firstTen.getContent());
        Collections.shuffle(content);
        UnsigDetailsEntity item1 = content.get(0);
        UnsigDetailsEntity item2 = content.get(1);

        assertNotNull(item1);
        assertNotNull(item2);

        long initialCount = offerRepository.count();
        assertEquals(0, initialCount);

        Offer o = newOffer(item1.getUnsigId(), 1020L, randomString());
        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer(item2.getUnsigId(), 1020L, randomString());
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        long newCount0 = offerRepository.count();
        assertEquals(initialCount + 2, newCount0);

        //update existing - count does not change
        o = newOffer(item2.getUnsigId(), 8888L, randomString());
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        long newCount = offerRepository.count();
        assertEquals(newCount0, newCount);

        response = mockMvc.perform(
                        get("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());
        List<Map> resList = (List<Map>) map.get(RESULT_LIST);
        assertEquals(initialCount + 2, resList.size());

        //order check - default desc
        Long firstVal = Long.valueOf((Integer) resList.get(0).get("amount"));
        Long secondVal = Long.valueOf((Integer) resList.get(1).get("amount"));
        assertEquals(1020L, secondVal);
        assertEquals(8888L, firstVal);


        response = mockMvc.perform(
                        get("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("order", "A")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        map = jsonParser.parseMap(response.getContentAsString());
        resList = (List<Map>) map.get(RESULT_LIST);
        assertEquals(initialCount + 2, resList.size());

        //now order is asc
        firstVal = Long.valueOf((Integer) resList.get(0).get("amount"));
        secondVal = Long.valueOf((Integer) resList.get(1).get("amount"));
        assertEquals(1020L, firstVal);
        assertEquals(8888L, secondVal);

    }

    @Test
    public void whenCreateAndListOffersByOwner_thenOk() throws Exception {
        offerRepository.deleteAll();
        Page<UnsigDetailsEntity> firstTen = unsigDetailsRepository.findAll(PageRequest.of(1, 10));
        List<UnsigDetailsEntity> content = new ArrayList(firstTen.getContent());
        Collections.shuffle(content);
        UnsigDetailsEntity item1 = content.get(0);
        UnsigDetailsEntity item2 = content.get(1);
        UnsigDetailsEntity item3 = content.get(2);

        assertNotNull(item1);
        assertNotNull(item2);
        assertNotNull(item3);

        long initialCount = offerRepository.count();
        assertEquals(0, initialCount);

        String ownerOne = randomString();
        String ownerTwo = randomString();
        Offer o = newOffer(item1.getUnsigId(), 100L, ownerOne);
        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer(item2.getUnsigId(), 200L, ownerTwo);
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer(item3.getUnsigId(), 300L, ownerOne);
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        long newCount0 = offerRepository.count();
        assertEquals(3, newCount0);


        response = mockMvc.perform(
                        get("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("pageNo", "0")
                                .queryParam("pageSize", "10")
                                .queryParam("owner", ownerOne)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());
        List<Map> resList = (List<Map>) map.get(RESULT_LIST);
        assertEquals(2, resList.size());

        //order check - default desc
        Long firstVal = Long.valueOf((Integer) resList.get(0).get("amount"));
        Long secondVal = Long.valueOf((Integer) resList.get(1).get("amount"));
        assertEquals(300L, firstVal);
        assertEquals(100L, secondVal);
        assertEquals(item3.getUnsigId(), resList.get(0).get("unsigId"));
        assertEquals(ownerOne, resList.get(0).get("owner"));
        assertEquals(item1.getUnsigId(), resList.get(1).get("unsigId"));
        assertEquals(ownerOne, resList.get(1).get("owner"));

    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void whenCreateAndGetUnsigDetailsById_thenContainsOfferDetails() throws Exception {
        offerRepository.deleteAll();
        Page<UnsigDetailsEntity> firstTen = unsigDetailsRepository.findAll(PageRequest.of(1, 10));
        List<UnsigDetailsEntity> content = new ArrayList(firstTen.getContent());
        Collections.shuffle(content);
        UnsigDetailsEntity item1 = content.get(0);
        assertNotNull(item1);

        long initialCount = offerRepository.count();
        assertEquals(0, initialCount);

        String unsigId = item1.getUnsigId();
        Offer o = newOffer(unsigId, 1020L, randomString());
        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andDo(document("{class-name}-{method-name}-create",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        long newCount0 = offerRepository.count();
        assertEquals(initialCount + 1, newCount0);

        response = mockMvc.perform(
                        get("/api/v1/unsigs/{id}".replaceFirst("\\{id\\}", unsigId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-get",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());
        assertEquals(unsigId, map.get("unsigId"));
        Map offerDetailsMap = (Map) map.get("offerDetails");
        assertEquals(unsigId, offerDetailsMap.get("unsigId"));
        assertEquals(o.owner, offerDetailsMap.get("owner"));
        assertEquals(o.amount, Long.valueOf((Integer) offerDetailsMap.get("amount")));
        assertEquals(o.txHash, offerDetailsMap.get("txHash"));
        assertEquals(o.txIndex, offerDetailsMap.get("txIndex"));
        assertEquals(o.datumHash, offerDetailsMap.get("datumHash"));
    }

    @Test
    public void givenMasterDataLoaded_whenListOrGet_thenOk() throws Exception {
        String contentString = Files.readString(Path.of("src/test/resources/unsigs-test.json"));
        Map<String, Object> map = jsonParser.parseMap(contentString);
        assertFalse(map.isEmpty());
        int countUnsigs = map.size();

        boolean b = unsigsService.loadMasterData();
        assertTrue(b);
        assertEquals(countUnsigs, unsigDetailsRepository.count());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/unsigs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-list",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> rspMap = jsonParser.parseMap(response.getContentAsString());
        List l = (List) rspMap.get(RESULT_LIST);
        assertTrue(l.size() > 0);

        Map firstItem = (Map) l.get(0);
        String idValue = (String) firstItem.get("unsigId");

        response = mockMvc.perform(
                        get("/api/v1/unsigs/{id}".replaceFirst("\\{id\\}", idValue))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-get",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        rspMap = jsonParser.parseMap(response.getContentAsString());
        String id = (String) rspMap.get("unsigId");
        assertEquals(id, idValue);
        Map detailsMap = (Map) rspMap.get("details");


    }

    @Test
    public void givenMasterDataLoaded_whenFindByUnsigIds_thenOk() throws Exception {

        long count = unsigDetailsRepository.count();
        if (count < 100) {
            loadUnsigMasterData();
        }

        Page<UnsigDetailsEntity> set1 = unsigDetailsRepository.findAll(PageRequest.of(2, 15));
        Page<UnsigDetailsEntity> set2 = unsigDetailsRepository.findAll(PageRequest.of(6, 10));

        MutableList<String> usIds = ListIterate.collect(set1.getContent(), e -> e.getUnsigId());
        usIds.addAll(ListIterate.collect(set2.getContent(), e -> e.getUnsigId()));

        assertEquals(25, usIds.size());

        //create an offer for first 2, last one
        offerRepository.deleteAll();
        MutableList<UnsigDetailsEntity> offerList = FastList.newList();
        offerList.add(set1.getContent().get(0));
        offerList.add(set1.getContent().get(2));
        offerList.add(set2.getContent().get(set2.getSize() - 1));

        for (UnsigDetailsEntity item : offerList) {
            String unsigId = item.getUnsigId();
            Offer o = newOffer(unsigId, Math.abs(new Random().nextLong()), randomString());
            mockMvc.perform(
                            put("/api/v1/offers")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(o)))
                    .andExpect(status().isAccepted());
        }


        MockHttpServletResponse response = mockMvc.perform(
                        post("/api/v1/unsigs/find")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(usIds))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-find",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> rspMap = jsonParser.parseMap(response.getContentAsString());
        List<Map> l = (List) rspMap.get(RESULT_LIST);
        assertEquals(25, l.size());

        MutableList<Object> rspIds = ListIterate.collect(l, e -> e.get("unsigId"));
        assertTrue(rspIds.allSatisfy(e -> usIds.contains(e)));
    }

    @Test
    public void givenOffersCreated_whenFindOffersByUnsigIds_thenOk() throws Exception {

        offerRepository.deleteAll();
        Page<UnsigDetailsEntity> firstTen = unsigDetailsRepository.findAll(PageRequest.of(1, 10));
        List<UnsigDetailsEntity> content = new ArrayList(firstTen.getContent());
        Collections.shuffle(content);
        UnsigDetailsEntity item1 = content.get(0);
        UnsigDetailsEntity item2 = content.get(1);
        UnsigDetailsEntity item3 = content.get(2);
        UnsigDetailsEntity item4 = content.get(3);

        assertNotNull(item1);
        assertNotNull(item2);
        assertNotNull(item3);

        long initialCount = offerRepository.count();
        assertEquals(0, initialCount);

        Offer o = newOffer(item1.getUnsigId(), 1020L, randomString());
        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer(item2.getUnsigId(), 2020L, randomString());
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer(item3.getUnsigId(), 3030L, randomString());
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        long newCount0 = offerRepository.count();
        assertEquals(initialCount + 3, newCount0);

        response = mockMvc.perform(
                        post("/api/v1/offers/find")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(List.of(item1.getUnsigId(),item2.getUnsigId(),item3.getUnsigId(),item4.getUnsigId())))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());
        List<Map> resList = (List<Map>) map.get(RESULT_LIST);
        assertEquals(3, resList.size());

        MutableList<Object> resUnsigIds = ListIterate.collect(resList, e -> e.get("unsigId"));
        assertTrue(resUnsigIds.contains(item1.getUnsigId()));
        assertTrue(resUnsigIds.contains(item2.getUnsigId()));
        assertTrue(resUnsigIds.contains(item3.getUnsigId()));
        assertFalse(resUnsigIds.contains(item4.getUnsigId()));
    }

    private void loadUnsigMasterData() throws IOException {
        String contentString = Files.readString(Path.of("src/test/resources/unsigs-test.json"));
        Map<String, Object> map = jsonParser.parseMap(contentString);
        assertFalse(map.isEmpty());
        int countUnsigs = map.size();

        boolean b = unsigsService.loadMasterData();
        assertTrue(b);
        assertEquals(countUnsigs, unsigDetailsRepository.count());
    }


    @Test
    public void givenAssetString_whenGetLastTransaction_thenOk() throws Exception {

        String assetString = "1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d756e7369673030303137";
        // 1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d.unsig00017

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/last-transaction/{asset}".replaceFirst("\\{asset\\}", assetString))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> at = jsonParser.parseMap(response.getContentAsString());

        assertNotNull(at);
        assertNotNull(at.get("txHash"));
        assertNotNull(at.get("txIndex"));
        assertNotNull(at.get("blockHeight"));
        assertNotNull(at.get("blockTime"));

    }

    @Test
    public void givenAddressAndAssetId_whenGetUtxo_thenOk() throws Exception {

        //sundaeswap testnet
        /*AssetAddressUtxo(txHash=7b62472ef03eb3df38a394bbf4276ac468374c914e1205f93c3391b3dd8e6e22,
                outputIndex=0, amount=[TransactionOutputAmount(unit=lovelace, quantity=162892143504),
                TransactionOutputAmount(unit=57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c,
                        quantity=8242384190), TransactionOutputAmount(unit=d311d3488cc4fef19d05634adce8534977a3bc6fc18136ad65df1d4f70200a, quantity=1)],
        block=e04dddc8eaeb8b146238f1212c86d86c3da4f61856a03e745a7c64a8b31aec69, dataHash=bebf8c47a46af9c75f8761ab1aa60baa8fa4d84c8e4b387f45e81c9cd1f820a9)
        */
        String assetString = "57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c";
        String address = "addr_test1wp9m8xkpt2tmy7madqldspgzgug8f2p3pwhz589cq75685slenwf4";

        AssetAddressUtxo assetUtxoAtAddress = blockfrostAdapter.getAssetUtxoAtAddress(
                address,
                assetString
        );
        assertNotNull(assetUtxoAtAddress);

        String datumHash = assetUtxoAtAddress.getDataHash();

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/utxo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("address",address)
                                .param("unsigAsset",assetString)
                                .param("datumHash",datumHash)
                )
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-find",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());

        assertTrue(map.size() > 0);

        assertEquals(datumHash, map.get("dataHash"));
    }

    @Test
    public void givenAddressAndAssetIdButIncorrectDatum_whenGetUtxo_throwsDatumMismatchException() throws Exception {

        //sundaeswap testnet
        /*AssetAddressUtxo(txHash=7b62472ef03eb3df38a394bbf4276ac468374c914e1205f93c3391b3dd8e6e22,
                outputIndex=0, amount=[TransactionOutputAmount(unit=lovelace, quantity=162892143504),
                TransactionOutputAmount(unit=57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c,
                        quantity=8242384190), TransactionOutputAmount(unit=d311d3488cc4fef19d05634adce8534977a3bc6fc18136ad65df1d4f70200a, quantity=1)],
        block=e04dddc8eaeb8b146238f1212c86d86c3da4f61856a03e745a7c64a8b31aec69, dataHash=bebf8c47a46af9c75f8761ab1aa60baa8fa4d84c8e4b387f45e81c9cd1f820a9)
        */
        String assetString = "57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c";
        String address = "addr_test1wp9m8xkpt2tmy7madqldspgzgug8f2p3pwhz589cq75685slenwf4";

        AssetAddressUtxo assetUtxoAtAddress = blockfrostAdapter.getAssetUtxoAtAddress(
                address,
                assetString
        );
        assertNotNull(assetUtxoAtAddress);

        String datumHash = new StringBuilder(assetUtxoAtAddress.getDataHash()).reverse().toString();

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/utxo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("address",address)
                                .param("unsigAsset",assetString)
                                .param("datumHash",datumHash)
                )
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> map = jsonParser.parseMap(response.getContentAsString());

        assertTrue(map.size() > 0);
        assertNotNull(map.get("message"));

    }


}
