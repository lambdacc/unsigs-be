package gimbalabs.unsigsbe;

import javax.persistence.*;

@Entity
@Table(name = "offer")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic(optional = false)
    @Column(name = "unsig_id", unique = true, updatable = false)
    private String unsigId;

    @Column(name = "owner")
    private String owner;

    @Column(name = "amount")
    private Long amount;

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
}
