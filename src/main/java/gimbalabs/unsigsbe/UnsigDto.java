package gimbalabs.unsigsbe;

import java.util.Map;

public class UnsigDto {
    public UnsigDto(String unsigId, Map<String, Object> details) {
        this.unsigId = unsigId;
        this.details = details;
    }

    private String unsigId;
    private Map<String, Object> details;

    public String getUnsigId() {
        return unsigId;
    }

    public void setUnsigId(String unsigId) {
        this.unsigId = unsigId;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
