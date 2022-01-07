package gimbalabs.unsigsbe.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OfferDto {

    public String txHash;
    public Integer txIndex;
    public String datumHash;
    private String unsigId;
    private String owner;
    private Long amount;
    private Map<String, Object> details;

}
