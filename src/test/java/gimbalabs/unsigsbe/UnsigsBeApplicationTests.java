package gimbalabs.unsigsbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:local-test-application.properties")
class UnsigsBeApplicationTests {
    protected JacksonJsonParser jsonParser = new JacksonJsonParser();

    @Autowired
    ObjectMapper objectMapper;

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Test
    void contextLoads() {
    }

}
