package gimbalabs.unsigsbe.model;

import lombok.Data;

@Data
public class AssetTransaction {

    private String txHash;
    private Integer txIndex;
    private Integer blockHeight;
    private Integer blockTime;
}
