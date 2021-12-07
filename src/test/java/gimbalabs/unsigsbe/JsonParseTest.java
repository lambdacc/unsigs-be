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

import static gimbalabs.unsigsbe.Constants.RESULT_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class JsonParseTest extends UnsigsBeApplicationTests {

    @Autowired
    UnsigsService unsigsService;

    @Autowired
    UnsigDetailsRepository unsigDetailsRepository;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;


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
    public void givenUnsigsJsonFile_whenParseIntoMap_thenOk() throws Exception {

        String contentString = Files.readString(Path.of("src/test/resources/allUnsigs.json"));
        Map<String, Object> map = jsonParser.parseMap(contentString);
        assertFalse(map.isEmpty());
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
                .andDo(document("{class-name}-{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn().getResponse();

        Map<String, Object> rspMap = jsonParser.parseMap(response.getContentAsString());
        List l = (List) rspMap.get(RESULT_LIST);
        assertTrue(l.size() > 0);
    }

}
