package gimbalabs.unsigsbe.model;

import javax.validation.constraints.NotNull;

public class UnsigDetails {

    @NotNull(message = "unsigId is required")
    public String unsigId;
    public String details;

}
