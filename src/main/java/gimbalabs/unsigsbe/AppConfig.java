package gimbalabs.unsigsbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${cardano.network}")
    private String cardanoNetwork;
    @Value("${testnet.blockfrost.baseurl}")
    private String testnetBaseUrl;
    @Value("${mainnet.blockfrost.baseurl}")
    private String mainnetBaseUrl;
    @Value("${testnet.api.key}")
    private String testnetApiKey;
    @Value("${mainnet.api.key}")
    private String mainnetApiKey;

    public String getCardanoNetwork() {
        return cardanoNetwork;
    }

    public String getTestnetApiKey() {
        return testnetApiKey;
    }

    public String getMainnetApiKey() {
        return mainnetApiKey;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean
    public JacksonJsonParser jsonParser() {
        return new JacksonJsonParser();
    }


    @Bean
    public WebClient blockfrostWebClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                .build();
        String baseUrl = cardanoNetwork.equals("mainnet") ? mainnetBaseUrl : testnetBaseUrl;
        String apiKey = cardanoNetwork.equals("mainnet") ? mainnetApiKey : testnetApiKey;
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("project_id", apiKey)
                .defaultHeader("Accept", "application/json")
                .exchangeStrategies(exchangeStrategies)
                .build();
    }


}
