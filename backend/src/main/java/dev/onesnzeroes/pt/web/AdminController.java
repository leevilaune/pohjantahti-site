package dev.onesnzeroes.pt.web;

import dev.onesnzeroes.pt.service.ClanSyncService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Manual refresh trigger, protected by the {@code X-Admin-Key} header ({@code app.admin-key}). */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ClanSyncService sync;
    private final String adminKey;

    public AdminController(ClanSyncService sync, @Value("${app.admin-key:}") String adminKey) {
        this.sync = sync;
        this.adminKey = adminKey;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @RequestHeader(value = "X-Admin-Key", required = false) String key) {
        if (adminKey.isBlank() || key == null || !MessageDigest.isEqual(
                adminKey.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(sync.refreshAll());
    }
}
