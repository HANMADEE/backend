package study.project.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("/version")
    public String serverVersionCheck() {
        return "Version 0.0.1";
    }
}
