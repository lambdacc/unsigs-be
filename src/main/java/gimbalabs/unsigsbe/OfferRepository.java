package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.entity.OfferEntity;
import gimbalabs.unsigsbe.entity.UnsigDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    Optional<OfferEntity> findByUnsigId(String unsigId);

    List<OfferEntity> findByUnsigIdIn(Collection<String> unsigIds);

}
