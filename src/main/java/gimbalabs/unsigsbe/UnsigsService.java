package gimbalabs.unsigsbe;

import org.eclipse.collections.api.list.MutableList;

import java.io.IOException;
import java.util.Map;

public interface UnsigsService {
    Map<String, Object> listOffers(Integer pageNo, Integer pageSize);

    Map<String, Object> listUnsigs(Integer pageNo, Integer pageSize);

    UnsigDto getUnsig(String unsigId);

    MutableList<UnsigDto> getUnsigs(MutableList<String> unsigIds);

    OfferEntity saveOffer(Offer offer);

    boolean saveUnsigDetails(MutableList<UnsigDetails> unsigDetailsList);

    boolean loadMasterData() throws IOException;
}
