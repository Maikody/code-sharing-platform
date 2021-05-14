package platform;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code")
public class APIController {

    private final CodeService codeService;

    public APIController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Code> getCodeByUUID(@PathVariable String uuid) {
        Code codeByUUID = codeService.findCodeByUUID(uuid);
        codeService.updateRestrictionData(codeByUUID);
        return ResponseEntity.ok(codeByUUID);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Code>> getLatestCodes() {
        return ResponseEntity.ok().body(codeService.findLatest10Codes());
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postCodeAsJSON(@RequestBody Code code) {
        codeService.save(code);
        return ResponseEntity.ok()
                .body("{\"id\": \"" + code.getUUID() +"\"}");
    }

    @GetMapping("/init")
    public ResponseEntity<?> init() {
        codeService.save(new Code("class Clazz {}"));
        codeService.save(new Code("public static void main(String[] args){}"));
        codeService.save(new Code("interface Device {}",3,200));
        codeService.save(new Code("abstract class World {}"));
        codeService.save(new Code("private int counter() {}"));
        codeService.save(new Code("protected void protection(Long id) {}"));
        codeService.save(new Code("class Main {}"));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clean")
    public ResponseEntity<?> clean() {
        codeService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
