package dev.onesnzeroes.pt.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import dev.onesnzeroes.pt.config.CocProperties;
import dev.onesnzeroes.pt.domain.CocClan;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class ClashOfClansClientTest {

    private static final String JSON = """
            {
              "tag": "#2GY2L0UQP",
              "name": "Pohjantähti",
              "clanLevel": 12,
              "clanPoints": 42000,
              "warWins": 180,
              "members": 2,
              "someFieldWeDoNotUse": true,
              "badgeUrls": {"large": "https://example.test/badge.png"},
              "memberList": [
                {"tag": "#P1", "name": "Susi", "role": "leader", "townHallLevel": 16,
                 "trophies": 5400, "clanRank": 1, "donations": 100, "donationsReceived": 50,
                 "league": {"id": 29000022, "name": "Legend League",
                            "iconUrls": {"small": "https://img.test/s.png", "medium": "https://img.test/m.png"}}},
                {"tag": "#P2", "name": "Kuura", "role": "member", "townHallLevel": 15,
                 "trophies": 5000, "clanRank": 2, "donations": 10, "donationsReceived": 90}
              ]
            }
            """;

    private MockRestServiceServer server;
    private ClashOfClansClient client;

    private void setUp(String token) {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        client = new ClashOfClansClient(builder.build(),
                new CocProperties("https://api.test/v1/", token, List.of()));
    }

    @Test
    void fetchesClanWithEncodedTagAndBearerToken() {
        setUp("secret");
        server.expect(requestTo("https://api.test/v1/clans/%232GY2L0UQP"))
                .andExpect(header("Authorization", "Bearer secret"))
                .andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));

        CocClan clan = client.getClan("2gy2l0uqp");

        assertThat(clan.name()).isEqualTo("Pohjantähti");
        assertThat(clan.clanLevel()).isEqualTo(12);
        assertThat(clan.badgeUrls()).containsEntry("large", "https://example.test/badge.png");
        assertThat(clan.memberList()).hasSize(2);
        assertThat(clan.memberList().get(0).role()).isEqualTo("leader");
        assertThat(clan.memberList().get(0).league().name()).isEqualTo("Legend League");
        assertThat(clan.memberList().get(0).league().icon()).isEqualTo("https://img.test/s.png");
        assertThat(clan.memberList().get(1).league()).isNull();
        server.verify();
    }

    @Test
    void mapsForbiddenToHelpfulException() {
        setUp("secret");
        server.expect(requestTo("https://api.test/v1/clans/%232GY2L0UQP"))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));

        assertThatThrownBy(() -> client.getClan("#2GY2L0UQP"))
                .isInstanceOf(CocApiException.class)
                .hasMessageContaining("403")
                .satisfies(e -> assertThat(((CocApiException) e).getStatus()).isEqualTo(403));
    }

    @Test
    void refusesToCallWithoutToken() {
        setUp("");

        assertThat(client.isConfigured()).isFalse();
        assertThatThrownBy(() -> client.getClan("#2GY2L0UQP")).isInstanceOf(CocApiException.class);
    }
}
