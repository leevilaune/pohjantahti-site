package dev.onesnzeroes.pt.config;

import dev.onesnzeroes.pt.domain.TagUtil;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Settings for the Clash of Clans API ({@code coc.*} in application.yml).
 *
 * @param baseUrl  API base URL
 * @param token    API token (Bearer); empty means the client is disabled
 * @param clanTags the clans this backend tracks
 */
@ConfigurationProperties(prefix = "coc")
public record CocProperties(String baseUrl, String token, List<String> clanTags) {

    public static final String DEFAULT_BASE_URL = "https://api.clashofclans.com/v1";

    public CocProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = DEFAULT_BASE_URL;
        }
        clanTags = clanTags == null ? List.of() : clanTags.stream().map(TagUtil::normalize).toList();
    }
}
