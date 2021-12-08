package gimbalabs.unsigsbe;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.utility.ListIterate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static gimbalabs.unsigsbe.Constants.RESULT_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class UnsigsControllerIT extends UnsigsBeApplicationTests {

    @Autowired
    OfferRepository offerRepository;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    UnsigsService unsigsService;

    @Autowired
    UnsigDetailsRepository unsigDetailsRepository;

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
    public void whenCreateNewOffer_thenOk() throws Exception {

        Offer o = newOffer();

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

    private Offer newOffer() {
        Offer o = new Offer();
        o.unsigId = String.valueOf(Math.abs(new Random().nextLong()));
        o.owner = UUID.randomUUID().toString();
        o.amount = 10202020L;
        return o;
    }

    @Test
    public void whenCreateAndListOffers_thenOk() throws Exception {
        long initialCount = offerRepository.count();
        Offer o = newOffer();
        MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();
        o = newOffer();
        response = mockMvc.perform(
                        put("/api/v1/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(o)))
                .andExpect(status().isAccepted())
                .andReturn().getResponse();

        long newCount = offerRepository.count();
        assertEquals(initialCount + 2, newCount);


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
    }


    @Test
    public void whenLoadMasterData_thenOk() throws Exception {
        String contentString = Files.readString(Path.of("src/test/resources/allUnsigs.json"));
        Map<String, Object> map = jsonParser.parseMap(contentString);
        assertFalse(map.isEmpty());

        List<Map> transactions = (List<Map>) ((Map) map.get("data")).get("transactions");
        MutableList<Map> allMetadata = ListIterate.flatCollect(transactions, e -> (List) e.get("metadata"));
        MutableList<Map> allValues = allMetadata.collect(e -> (Map) e.get("value"));
        MutableList<Map> allUnsigs = (MutableList<Map>) allValues.flatCollect(Map::values);

        assertFalse(allUnsigs.isEmpty());

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

        assertEquals(allUnsigs.size(), resultToStore.size());
        boolean b = unsigsService.saveUnsigDetails(resultToStore);
        assertTrue(b);
        assertEquals(allUnsigs.size(), unsigDetailsRepository.count());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/unsigs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-listUnsigs",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> rspMap = jsonParser.parseMap(response.getContentAsString());
        List l = (List) rspMap.get(RESULT_LIST);
        assertTrue(l.size() > 0);

        Map firstItem = (Map) l.get(0);
        String key = (String) firstItem.keySet().iterator().next();

        response = mockMvc.perform(
                        get("/api/v1/unsigs/{key}".replaceFirst("\\{key\\}", key))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}-{method-name}-listUnsigs",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        rspMap = jsonParser.parseMap(response.getContentAsString());
        String id = (String) rspMap.get("unsigId");
        assertEquals(id, key);


    }


}
