package gimbalabs.unsigsbe;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(path = "/api/v1",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class UnsigsController {

    private final UnsigsService service;

    public UnsigsController(UnsigsService service) {
        this.service = service;
    }

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

}
