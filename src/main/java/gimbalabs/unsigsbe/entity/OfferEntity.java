package gimbalabs.unsigsbe.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "offer")
@Data
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

    @Column(name = "tx_hash")
    private String txHash;

    @Column(name = "tx_index")
    private Integer txIndex;

}
