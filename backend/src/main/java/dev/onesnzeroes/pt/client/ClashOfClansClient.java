package dev.onesnzeroes.pt.client;

import dev.onesnzeroes.pt.config.CocProperties;
import dev.onesnzeroes.pt.domain.CocClan;
import dev.onesnzeroes.pt.domain.TagUtil;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/** Thin wrapper around the official Clash of Clans REST API. */
@Component
public class ClashOfClansClient {

    private final RestClient http;
    private final CocProperties props;

    public ClashOfClansClient(RestClient cocRestClient, CocProperties props) {
        this.http = cocRestClient;
        this.props = props;
    }

    public boolean isConfigured() {
        return props.token() != null && !props.token().isBlank();
    }

    /** Fetches a clan including its member list. */
    public CocClan getClan(String tag) {
        if (!isConfigured()) {
            throw new CocApiException(503, "COC_API_TOKEN is not configured");
        }
        // '#' must be sent as %23; build the URI ourselves so encoding is deterministic.
        String encoded = URLEncoder.encode(TagUtil.normalize(tag), StandardCharsets.UTF_8);
        URI uri = URI.create(props.baseUrl().replaceAll("/+$", "") + "/clans/" + encoded);
        try {
            CocClan clan = http.get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + props.token())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(CocClan.class);
            if (clan == null) {
                throw new CocApiException(502, "Clash of Clans API returned an empty response");
            }
            return clan;
        } catch (RestClientResponseException e) {
            int status = e.getStatusCode().value();
            throw new CocApiException(status, explain(status), e);
        } catch (RestClientException e) {
            throw new CocApiException(0, "Could not reach the Clash of Clans API: " + e.getMessage(), e);
        }
    }

    private static String explain(int status) {
        return switch (status) {
            case 400 -> "Clash of Clans API rejected the request (400)";
            case 403 -> "Clash of Clans API refused access (403): check the token and that this server's "
                    + "public IP is allowed for the key";
            case 404 -> "Clan not found (404)";
            case 429 -> "Clash of Clans API rate limit hit (429)";
            case 503 -> "Clash of Clans API is down for maintenance (503)";
            default -> "Clash of Clans API error (" + status + ")";
        };
    }
}
