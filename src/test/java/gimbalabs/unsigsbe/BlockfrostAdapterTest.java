package gimbalabs.unsigsbe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
public class BlockfrostAdapterTest extends UnsigsBeApplicationTests {

    @Autowired
    BlockfrostAdapter blockfrostAdapter;

    @Autowired
    AppConfig appConfig;

    private WebClient webClient;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        JacksonTester.initFields(this, objectMapper);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        webClient = WebClient.builder()
                .baseUrl("https://cardano-testnet.blockfrost.io/api/v0")
                .defaultHeader("project_id",appConfig.getTestnetApiKey())
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Test
    public void whenGetAssetTransaction_thenReturnsAssetTransactionForCountPageAndOrder() throws Exception {

        String assetString = "1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d756e7369673030303137";

        String responseString = webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/assets/{asset}/transactions"
                        .replaceFirst("\\{asset\\}", assetString))
                                .queryParam("order","desc")
                                .build()
                )
                .retrieve().bodyToMono(String.class).block();

        Assertions.assertNotNull(responseString);
    }


}
