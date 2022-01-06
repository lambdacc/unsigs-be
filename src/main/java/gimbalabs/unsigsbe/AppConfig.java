package gimbalabs.unsigsbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${cardano.network}")
    private String cardanoNetwork;
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



}
