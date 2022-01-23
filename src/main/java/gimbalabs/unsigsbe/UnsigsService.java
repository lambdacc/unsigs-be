package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.dto.UnsigDto;
import gimbalabs.unsigsbe.entity.OfferEntity;
import gimbalabs.unsigsbe.model.Offer;
import gimbalabs.unsigsbe.model.UnsigDetails;
import org.eclipse.collections.api.list.MutableList;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UnsigsService {
    Map<String, Object> listOffers(Integer pageNo, Integer pageSize, String order, String owner);

    Map<String, Object> listUnsigs(Integer pageNo, Integer pageSize);

    UnsigDto getUnsig(String unsigId);

    MutableList<UnsigDto> getUnsigs(List<String> unsigIds);

    OfferEntity saveOffer(Offer offer);

    @NotNull(message = "unsigId is required") String deleteOffer(Offer offer);

    boolean saveUnsigDetails(MutableList<UnsigDetails> unsigDetailsList);

    boolean loadMasterData() throws IOException;

    Map<String, Object> findUnsigsByIds(List<String> unsigIds);
    Map<String, Object> findOffersByUnsigIds(List<String> unsigIds);
}
