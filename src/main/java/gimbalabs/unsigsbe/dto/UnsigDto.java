package gimbalabs.unsigsbe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UnsigDto {

    private String unsigId;
    private Map<String, Object> details;
    private TrimmedOfferDto offerDetails;

}
