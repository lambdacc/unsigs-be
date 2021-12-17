package gimbalabs.unsigsbe;

import org.eclipse.collections.api.list.MutableList;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UnsigsService {
    Map<String, Object> listOffers(Integer pageNo, Integer pageSize, String order);

    Map<String, Object> listUnsigs(Integer pageNo, Integer pageSize);

    UnsigDto getUnsig(String unsigId);

    MutableList<UnsigDto> getUnsigs(MutableList<String> unsigIds);

    OfferEntity saveOffer(Offer offer);

    @NotNull(message = "unsigId is required") String deleteOffer(Offer offer);

    boolean saveUnsigDetails(MutableList<UnsigDetails> unsigDetailsList);

    boolean loadMasterData() throws IOException;

    Map<String, Object> findUnsigsByIds(List<String> unsigIds);
}
