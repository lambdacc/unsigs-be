package gimbalabs.unsigsbe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    Optional<OfferEntity> findByUnsigId(String unsigId);
}
