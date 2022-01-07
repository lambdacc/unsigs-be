package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.model.AssetTransaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class BlockfrostAdapter {

    private final WebClient blockfrostWebClient;

    public AssetTransaction getLatestAssetTransaction(String asset) throws Exception {

        if (asset == null || asset.isEmpty()) {
            return null;
        }

        AssetTransaction[] assetTransactionsArr = blockfrostWebClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/assets/{asset}/transactions"
                                        .replaceFirst("\\{asset\\}", asset))
                                .queryParam("order", "desc")
                                .queryParam("page", "1")
                                .queryParam("count", "1")
                                .build()
                )
                .retrieve().bodyToMono(AssetTransaction[].class).block();

        return (assetTransactionsArr.length > 0) ? assetTransactionsArr[0] : null;
    }

}