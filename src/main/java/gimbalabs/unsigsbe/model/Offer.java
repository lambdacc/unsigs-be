package gimbalabs.unsigsbe.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Offer {

    @NotNull(message = "unsigId is required")
    public String unsigId;
    @NotEmpty(message = "owner is required")
    public String owner;
    @NotNull(message = "amount is required")
    public Long amount;
    public String txHash;
    public Integer txIndex;

}
