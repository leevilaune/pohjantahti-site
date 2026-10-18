package dev.onesnzeroes.pt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.onesnzeroes.pt.domain.CocClan;
import dev.onesnzeroes.pt.domain.CocLeague;
import dev.onesnzeroes.pt.domain.CocMember;
import dev.onesnzeroes.pt.service.ClanDtos.ClanDetail;
import dev.onesnzeroes.pt.service.ClanDtos.MemberDto;
import dev.onesnzeroes.pt.service.ClanDtos.Overview;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ClanService.class)
class ClanServiceTest {

    private static final String TAG = "#2GY2L0UQP";

    @Autowired
    ClanService service;

    @Test
    void secondSaveUpdatesClanAndSyncsMembers() {
        service.save(TAG, clan(12, 180, member("#P1", "Susi", 1), member("#P2", "Kuura", 2)));
        service.save(TAG, clan(13, 181, member("#P2", "Kuura", 1), member("#P3", "Kaamos", 2)));

        ClanDetail detail = service.getClan("2gy2l0uqp");

        assertThat(detail.clanLevel()).isEqualTo(13);
        assertThat(detail.warWins()).isEqualTo(181);
        assertThat(detail.memberList()).extracting(MemberDto::name)
                .containsExactlyInAnyOrder("Kuura", "Kaamos");
        assertThat(service.listMembers(TAG)).extracting(MemberDto::tag)
                .containsExactly("#P2", "#P3");
    }

    @Test
    void overviewSumsAcrossClans() {
        service.save("#2GY2L0UQP", clan(12, 100, member("#P1", "Susi", 1)));
        service.save("#2RJ0VGRCL", clan(10, 50, member("#P9", "Revontuli", 1), member("#P8", "Halla", 2)));

        Overview overview = service.overview();

        assertThat(overview.clanCount()).isEqualTo(2);
        assertThat(overview.totalWarWins()).isEqualTo(150);
        assertThat(overview.clans()).hasSize(2);
    }

    @Test
    void unknownClanIsNotFound() {
        assertThatThrownBy(() -> service.getClan("#2JJGRUPQL")).isInstanceOf(ClanNotFoundException.class);
        assertThatThrownBy(() -> service.listMembers("#2JJGRUPQL")).isInstanceOf(ClanNotFoundException.class);
    }

    @Test
    void leagueTierIsPreferredOverLegacyLeague() {
        CocLeague legacy = new CocLeague(29000022L, "Legend League", Map.of("small", "https://img.test/legacy.png"));
        CocLeague tier = new CocLeague(105000034L, "Electro League 33", Map.of("medium", "https://img.test/tier.png"));
        service.save(TAG, clan(12, 100,
                new CocMember("#P1", "Susi", "leader", 16, 250, 5400, 1, 10, 5, legacy, tier),
                new CocMember("#P2", "Kuura", "member", 15, 240, 5000, 2, 10, 5, legacy, null),
                member("#P3", "Kaamos", 3)));

        List<MemberDto> members = service.listMembers(TAG);

        assertThat(members.get(0).leagueId()).isEqualTo(105000034L);
        assertThat(members.get(0).league()).isEqualTo("Electro League 33");
        assertThat(members.get(0).leagueIcon()).isEqualTo("https://img.test/tier.png");
        assertThat(members.get(1).league()).isEqualTo("Legend League");
        assertThat(members.get(2).league()).isNull();
        assertThat(members.get(2).leagueId()).isNull();
        assertThat(members.get(2).leagueIcon()).isNull();
    }

    private static CocClan clan(int level, int warWins, CocMember... members) {
        return new CocClan("#X", "Pohjantähti", "inviteOnly", "desc", level, 40000, warWins, 3, 1, 2,
                members.length, 3000, 10, "always", null, List.of(members));
    }

    private static CocMember member(String tag, String name, int rank) {
        return new CocMember(tag, name, "member", 15, 200, 4500, rank, 10, 5, null, null);
    }
}
