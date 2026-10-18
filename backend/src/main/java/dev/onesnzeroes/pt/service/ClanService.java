package dev.onesnzeroes.pt.service;

import dev.onesnzeroes.pt.domain.ClanEntity;
import dev.onesnzeroes.pt.domain.CocClan;
import dev.onesnzeroes.pt.domain.CocMember;
import dev.onesnzeroes.pt.domain.MemberEntity;
import dev.onesnzeroes.pt.domain.TagUtil;
import dev.onesnzeroes.pt.repo.ClanRepository;
import dev.onesnzeroes.pt.repo.MemberRepository;
import dev.onesnzeroes.pt.service.ClanDtos.ClanDetail;
import dev.onesnzeroes.pt.service.ClanDtos.ClanSummary;
import dev.onesnzeroes.pt.service.ClanDtos.MemberDto;
import dev.onesnzeroes.pt.service.ClanDtos.Overview;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Stores clan data fetched from the Clash of Clans API and serves it to the REST layer. */
@Service
public class ClanService {

    private final ClanRepository clans;
    private final MemberRepository members;

    public ClanService(ClanRepository clans, MemberRepository members) {
        this.clans = clans;
        this.members = members;
    }

    // ---- writes ---------------------------------------------------------------------------

    /** Inserts or updates a clan and its members. Players who left the clan are removed. */
    @Transactional
    public void save(String tag, CocClan source) {
        String clanTag = TagUtil.normalize(tag);
        ClanEntity clan = clans.findByTag(clanTag).orElseGet(() -> new ClanEntity(clanTag));
        clan.apply(source);

        Map<String, MemberEntity> existing = clan.getMembers().stream()
                .collect(Collectors.toMap(MemberEntity::getPlayerTag, Function.identity(), (a, b) -> a));
        Set<String> seen = new HashSet<>();

        List<CocMember> incoming = source.memberList() == null ? List.of() : source.memberList();
        for (CocMember m : incoming) {
            if (m.tag() == null) {
                continue;
            }
            seen.add(m.tag());
            MemberEntity entity = existing.get(m.tag());
            if (entity == null) {
                entity = new MemberEntity(clan, m.tag());
                clan.getMembers().add(entity);
            }
            entity.apply(m);
        }
        clan.getMembers().removeIf(m -> !seen.contains(m.getPlayerTag()));

        clans.save(clan);
    }

    // ---- reads ----------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ClanSummary> listClans() {
        return clans.findAllByOrderByNameAsc().stream().map(ClanSummary::from).toList();
    }

    @Transactional(readOnly = true)
    public ClanDetail getClan(String tag) {
        String clanTag = TagUtil.normalize(tag);
        return clans.findByTag(clanTag).map(ClanDetail::from).orElseThrow(() -> new ClanNotFoundException(clanTag));
    }

    @Transactional(readOnly = true)
    public List<MemberDto> listMembers(String tag) {
        String clanTag = TagUtil.normalize(tag);
        if (!clans.existsById(clanTag)) {
            throw new ClanNotFoundException(clanTag);
        }
        return members.findByClan_TagOrderByClanRankAsc(clanTag).stream().map(MemberDto::from).toList();
    }

    @Transactional(readOnly = true)
    public Overview overview() {
        List<ClanSummary> all = listClans();
        Instant latest = all.stream().map(ClanSummary::updatedAt).filter(u -> u != null)
                .max(Comparator.naturalOrder()).orElse(null);
        return new Overview(
                all.size(),
                all.stream().mapToInt(ClanSummary::members).sum(),
                all.stream().mapToInt(ClanSummary::warWins).sum(),
                all.stream().mapToInt(ClanSummary::warWinStreak).max().orElse(0),
                latest,
                all);
    }
}
