package dev.onesnzeroes.pt.service;

import dev.onesnzeroes.pt.client.ClashOfClansClient;
import dev.onesnzeroes.pt.client.CocApiException;
import dev.onesnzeroes.pt.config.CocProperties;
import dev.onesnzeroes.pt.domain.CocClan;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Periodically pulls every tracked clan from the Clash of Clans API into the database.
 * The website only ever reads the cached copy, so the API key stays on the server and
 * the official API's rate limits are not affected by site traffic.
 */
@Service
public class ClanSyncService {

    private static final Logger log = LoggerFactory.getLogger(ClanSyncService.class);

    private final ClashOfClansClient client;
    private final ClanService clanService;
    private final CocProperties props;

    public ClanSyncService(ClashOfClansClient client, ClanService clanService, CocProperties props) {
        this.client = client;
        this.clanService = clanService;
        this.props = props;
    }

    @Scheduled(initialDelayString = "PT5S", fixedDelayString = "${coc.refresh-interval:PT10M}")
    public void scheduledRefresh() {
        refreshAll();
    }

    /** Refreshes all tracked clans; one failing clan does not stop the others. Returns "ok" or an error per tag. */
    public synchronized Map<String, String> refreshAll() {
        Map<String, String> results = new LinkedHashMap<>();
        if (props.clanTags().isEmpty()) {
            log.warn("No clan tags configured (coc.clan-tags); nothing to refresh");
            return results;
        }
        if (!client.isConfigured()) {
            log.warn("COC_API_TOKEN is not set; skipping refresh");
            props.clanTags().forEach(tag -> results.put(tag, "COC_API_TOKEN is not configured"));
            return results;
        }
        for (String tag : props.clanTags()) {
            try {
                CocClan clan = client.getClan(tag);
                clanService.save(tag, clan);
                results.put(tag, "ok");
                log.info("Refreshed {} ({})", tag, clan.name());
            } catch (CocApiException e) {
                log.warn("Refresh of {} failed: {}", tag, e.getMessage());
                results.put(tag, e.getMessage());
            } catch (RuntimeException e) {
                log.error("Unexpected error while refreshing {}", tag, e);
                results.put(tag, "unexpected error: " + e.getMessage());
            }
        }
        return results;
    }
}
