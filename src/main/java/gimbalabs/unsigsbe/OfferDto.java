package gimbalabs.unsigsbe;

import java.util.Map;

public class OfferDto {

    private String unsigId;
    private String owner;
    private Long amount;
    private Map<String, Object> details;

    public String getUnsigId() {
        return unsigId;
    }

    public void setUnsigId(String unsigId) {
        this.unsigId = unsigId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
