package gimbalabs.unsigsbe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class JsonParseTest extends UnsigsBeApplicationTests {

    @Test
    public void givenUnsigsJsonFile_whenParseIntoMap_thenOk() throws Exception {

        String contentString = Files.readString(Path.of("src/test/resources/allUnsigs.json"));
        Map<String, Object> map = jsonParser.parseMap(contentString);
        assertFalse(map.isEmpty());
    }

}
