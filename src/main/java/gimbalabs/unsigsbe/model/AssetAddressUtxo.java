package gimbalabs.unsigsbe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetAddressUtxo {
    private String txHash;
    private Integer outputIndex;
    private List<TransactionOutputAmount> amount = new ArrayList();
    private String block;
    private String dataHash;
    private String asset;
    private String assetQuantity;

}

