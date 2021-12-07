package gimbalabs.unsigsbe;

import javax.persistence.*;

@Entity
@Table(name = "unsig_details")
public class UnsigDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "unsig_id",unique = true)
    private String unsigId;

    @Column(name = "details")
    private String details;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUnsigId() {
        return unsigId;
    }

    public void setUnsigId(String unsigId) {
        this.unsigId = unsigId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
