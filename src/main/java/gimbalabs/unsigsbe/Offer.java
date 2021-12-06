package gimbalabs.unsigsbe;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Offer {

    @NotNull(message = "unsigId is required")
    public Long unsigId;
    @NotEmpty(message = "owner is required")
    public String owner;
    @NotNull(message = "amount is required")
    public Long amount;

}
