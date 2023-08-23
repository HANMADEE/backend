package study.project.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.project.backend.global.common.CustomResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VersionController {

    @GetMapping("/version")
    public CustomResponseEntity<Map<String, String>> serverVersionCheck() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("version", "0.0.1");
        stringMap.put("status", "On");
        return CustomResponseEntity.success(stringMap);
    }
}
