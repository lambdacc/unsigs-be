package gimbalabs.unsigsbe;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.utility.ListIterate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class JsonParseTest extends UnsigsBeApplicationTests {

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

        allUnsigs.forEach(new Consumer<Map>() {
            @Override
            public void accept(Map m) {
                String unsigId = (String) m.keySet().iterator().next();
                Map unsigDetails = (Map) m.get(unsigId);
                try {
                    objectMapper.writeValueAsString(unsigDetails);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
        });
        //checksum


    }

}
