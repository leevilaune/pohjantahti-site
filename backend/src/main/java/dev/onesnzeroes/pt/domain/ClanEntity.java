package dev.onesnzeroes.pt.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Latest known state of a tracked clan, refreshed from the Clash of Clans API. */
@Entity
@Table(name = "clan")
public class ClanEntity {

    @Id
    @Column(length = 20)
    private String tag;

    private String name;

    @Column(length = 2000)
    private String description;

    private int clanLevel;
    private int clanPoints;
    private int warWins;
    private int warWinStreak;
    private int warTies;
    private int warLosses;
    private int memberCount;
    private int requiredTrophies;
    private int requiredTownHallLevel;
    private String warFrequency;

    @Column(length = 500)
    private String badgeUrl;

    private Instant updatedAt;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("clanRank ASC")
    private List<MemberEntity> members = new ArrayList<>();

    protected ClanEntity() {
    }

    public ClanEntity(String tag) {
        this.tag = tag;
    }

    /** Copies the clan-level fields from an API response. Members are handled by the service. */
    public void apply(CocClan c) {
        this.name = c.name();
        this.description = c.description();
        this.clanLevel = n(c.clanLevel());
        this.clanPoints = n(c.clanPoints());
        this.warWins = n(c.warWins());
        this.warWinStreak = n(c.warWinStreak());
        this.warTies = n(c.warTies());
        this.warLosses = n(c.warLosses());
        this.memberCount = n(c.members());
        this.requiredTrophies = n(c.requiredTrophies());
        this.requiredTownHallLevel = n(c.requiredTownhallLevel());
        this.warFrequency = c.warFrequency();
        this.badgeUrl = c.badgeUrls() == null ? null : c.badgeUrls().get("large");
        this.updatedAt = Instant.now();
    }

    static int n(Integer value) {
        return value == null ? 0 : value;
    }

    public String getTag() { return tag; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getClanLevel() { return clanLevel; }
    public int getClanPoints() { return clanPoints; }
    public int getWarWins() { return warWins; }
    public int getWarWinStreak() { return warWinStreak; }
    public int getWarTies() { return warTies; }
    public int getWarLosses() { return warLosses; }
    public int getMemberCount() { return memberCount; }
    public int getRequiredTrophies() { return requiredTrophies; }
    public int getRequiredTownHallLevel() { return requiredTownHallLevel; }
    public String getWarFrequency() { return warFrequency; }
    public String getBadgeUrl() { return badgeUrl; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<MemberEntity> getMembers() { return members; }
}
