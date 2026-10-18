package dev.onesnzeroes.pt.service;

import dev.onesnzeroes.pt.domain.ClanEntity;
import dev.onesnzeroes.pt.domain.MemberEntity;
import java.time.Instant;
import java.util.List;

/** JSON shapes returned by the REST API. */
public final class ClanDtos {

    private ClanDtos() {
    }

    public record MemberDto(
            String tag,
            String name,
            String role,
            int townHallLevel,
            int expLevel,
            int trophies,
            int clanRank,
            int donations,
            int donationsReceived,
            Long leagueId,
            String league,
            String leagueIcon) {

        static MemberDto from(MemberEntity m) {
            return new MemberDto(m.getPlayerTag(), m.getName(), m.getRole(), m.getTownHallLevel(),
                    m.getExpLevel(), m.getTrophies(), m.getClanRank(), m.getDonations(),
                    m.getDonationsReceived(), m.getLeagueId(), m.getLeagueName(),
                    m.getLeagueIconUrl());
        }
    }

    public record ClanSummary(
            String tag,
            String name,
            int clanLevel,
            int clanPoints,
            int warWins,
            int warWinStreak,
            int members,
            String badgeUrl,
            Instant updatedAt) {

        static ClanSummary from(ClanEntity c) {
            return new ClanSummary(c.getTag(), c.getName(), c.getClanLevel(), c.getClanPoints(),
                    c.getWarWins(), c.getWarWinStreak(), c.getMemberCount(), c.getBadgeUrl(),
                    c.getUpdatedAt());
        }
    }

    public record ClanDetail(
            String tag,
            String name,
            String description,
            int clanLevel,
            int clanPoints,
            int warWins,
            int warWinStreak,
            int warTies,
            int warLosses,
            int members,
            int requiredTrophies,
            int requiredTownHallLevel,
            String warFrequency,
            String badgeUrl,
            Instant updatedAt,
            List<MemberDto> memberList) {

        static ClanDetail from(ClanEntity c) {
            return new ClanDetail(c.getTag(), c.getName(), c.getDescription(), c.getClanLevel(),
                    c.getClanPoints(), c.getWarWins(), c.getWarWinStreak(), c.getWarTies(),
                    c.getWarLosses(), c.getMemberCount(), c.getRequiredTrophies(),
                    c.getRequiredTownHallLevel(), c.getWarFrequency(), c.getBadgeUrl(),
                    c.getUpdatedAt(), c.getMembers().stream().map(MemberDto::from).toList());
        }
    }

    /** Totals across all tracked clans, for the front page. */
    public record Overview(
            int clanCount,
            int totalMembers,
            int totalWarWins,
            int bestWarWinStreak,
            Instant updatedAt,
            List<ClanSummary> clans) {
    }
}
