package dev.onesnzeroes.pt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/** A league (or league tier) as the Clash of Clans API describes it on a clan member. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CocLeague(Long id, String name, Map<String, String> iconUrls) {

    /** Best icon for a small display size, or null if the API sent none. */
    public String icon() {
        if (iconUrls == null) {
            return null;
        }
        for (String size : new String[] {"small", "medium", "large", "tiny"}) {
            String url = iconUrls.get(size);
            if (url != null && !url.isBlank()) {
                return url;
            }
        }
        return null;
    }
}
