package dev.onesnzeroes.pt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** One entry of {@code memberList} in the Clash of Clans clan response. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CocMember(
        String tag,
        String name,
        String role,
        Integer townHallLevel,
        Integer expLevel,
        Integer trophies,
        Integer clanRank,
        Integer donations,
        Integer donationsReceived) {
}
