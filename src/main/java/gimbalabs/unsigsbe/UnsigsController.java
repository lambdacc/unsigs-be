package gimbalabs.unsigsbe;

import gimbalabs.unsigsbe.dto.UnsigDto;
import gimbalabs.unsigsbe.entity.OfferEntity;
import gimbalabs.unsigsbe.exception.DatumMismatchException;
import gimbalabs.unsigsbe.model.AssetAddressUtxo;
import gimbalabs.unsigsbe.model.AssetTransaction;
import gimbalabs.unsigsbe.model.Offer;
import lombok.AllArgsConstructor;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(path = "/api/v1",
        produces = "application/json")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UnsigsController {

    private final UnsigsService service;
    private final BlockfrostAdapter blockfrostAdapter;

    @GetMapping("/ping")
    public ResponseEntity<UnifiedMap<String, String>> ping() {
        return ok(UnifiedMap.newWithKeysValues("response", "Hello world"));
    }

    @GetMapping("/offers")
    public ResponseEntity<Map<String, Object>> listOffers(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @Max(100) @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "D") String order) {
        return ok(service.listOffers(pageNo, pageSize, order));
    }

    @PutMapping("/offers")
    public ResponseEntity<OfferEntity> saveOffer(
            @RequestBody Offer offer) {

        return accepted().body(service.saveOffer(offer));
    }

    @DeleteMapping("/offers")
    public ResponseEntity<Map<String, String>> deleteOffer(
            @RequestBody Offer offer) {

        String unsigId = service.deleteOffer(offer);
        return accepted()
                .body(UnifiedMap.newWithKeysValues("unsigId", unsigId));
    }

    @GetMapping("/unsigs")
    public ResponseEntity<Map<String, Object>> listUnsigs(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @Max(100) @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return ok(service.listUnsigs(pageNo, pageSize));
    }

    @PostMapping("/unsigs/find")
    public ResponseEntity<Map<String, Object>> findUnsigsByIds(
            @RequestBody(required = true) List<String> unsigIds) {

        if (unsigIds.size() > 30) {
            throw new RuntimeException("Requested content size above limit");
        }
        return ok(service.findUnsigsByIds(unsigIds));
    }

    @GetMapping("/unsigs/{id}")
    public ResponseEntity<UnsigDto> getUnsig(
            @PathVariable String id) {
        return ok(service.getUnsig(id));
    }

    @PostMapping("/load-data")
    public ResponseEntity<Boolean> loadData() {
        try {
            return accepted().body(service.loadMasterData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalServerError().body(false);
    }

    @GetMapping("/last-transaction/{asset}")
    public ResponseEntity<AssetTransaction> getLastAssetTransaction(
            @PathVariable String asset) throws Exception {

        return ok(blockfrostAdapter.getLatestAssetTransaction(asset));
    }

    @GetMapping("/utxo")
    public ResponseEntity<AssetAddressUtxo> getUtxoInfo(
            @NotEmpty @RequestParam String address,
            @NotEmpty @RequestParam String unsigAsset,
            @RequestParam String datumHash

    ) {
        AssetAddressUtxo assetUtxoAtAddress = blockfrostAdapter.getAssetUtxoAtAddress(address, unsigAsset);
        if (assetUtxoAtAddress == null) {
            return notFound().build();
        }

        if (!assetUtxoAtAddress.getDataHash().equals(datumHash)) {
            throw new DatumMismatchException(
                    "Expected datumHash is : " + datumHash + " whereas obtained datumHash is : " + assetUtxoAtAddress.getDataHash(),
                    HttpStatus.UNPROCESSABLE_ENTITY
                    );
        }

        return ok(assetUtxoAtAddress);
    }


}
