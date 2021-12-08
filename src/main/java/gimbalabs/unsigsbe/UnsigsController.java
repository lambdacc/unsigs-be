package gimbalabs.unsigsbe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<String> ping() {
        return ok("Hello world");
    }

    @GetMapping("/offers")
    public ResponseEntity<Map<String, Object>> listOffers(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return ok(service.listOffers(pageNo, pageSize));
    }

    @PutMapping("/offers")
    public ResponseEntity<OfferEntity> saveOffer(
            @RequestBody Offer offer) {

        return accepted().body(service.saveOffer(offer));
    }

    @GetMapping("/unsigs")
    public ResponseEntity<Map<String, Object>> listUnsigs(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return ok(service.listUnsigs(pageNo, pageSize));
    }

    @GetMapping("/unsigs/{unsigId}")
    public ResponseEntity<UnsigDto> listUnsigs(
            @PathVariable String unsigId) {
        return ok(service.getUnsig(unsigId));
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
