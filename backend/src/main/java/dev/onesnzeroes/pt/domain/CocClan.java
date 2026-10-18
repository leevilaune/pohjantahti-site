package dev.onesnzeroes.pt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/** The parts of the Clash of Clans {@code GET /clans/{tag}} response we use. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CocClan(
        String tag,
        String name,
        String type,
        String description,
        Integer clanLevel,
        Integer clanPoints,
        Integer warWins,
        Integer warWinStreak,
        Integer warTies,
        Integer warLosses,
        Integer members,
        Integer requiredTrophies,
        Integer requiredTownhallLevel,
        String warFrequency,
        Map<String, String> badgeUrls,
        List<CocMember> memberList) {
}
