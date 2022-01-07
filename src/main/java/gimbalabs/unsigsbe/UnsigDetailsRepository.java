package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.entity.UnsigDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface UnsigDetailsRepository extends JpaRepository<UnsigDetailsEntity, Long> {

    Optional<UnsigDetailsEntity> findByUnsigId(String unsigId);

    List<UnsigDetailsEntity> findByUnsigIdIn(Collection<String> unsigIds);
}
