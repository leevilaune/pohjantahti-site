package dev.onesnzeroes.pt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** A player in a tracked clan. */
@Entity
@Table(name = "clan_member", uniqueConstraints = @UniqueConstraint(columnNames = {"clan_tag", "player_tag"}))
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clan_tag")
    private ClanEntity clan;

    @Column(name = "player_tag", length = 20, nullable = false)
    private String playerTag;

    private String name;

    /** Raw API value: leader, coLeader, admin or member. */
    private String role;

    private int townHallLevel;
    private int expLevel;
    private int trophies;
    private int clanRank;
    private int donations;
    private int donationsReceived;

    protected MemberEntity() {
    }

    public MemberEntity(ClanEntity clan, String playerTag) {
        this.clan = clan;
        this.playerTag = playerTag;
    }

    public void apply(CocMember m) {
        this.name = m.name();
        this.role = m.role();
        this.townHallLevel = ClanEntity.n(m.townHallLevel());
        this.expLevel = ClanEntity.n(m.expLevel());
        this.trophies = ClanEntity.n(m.trophies());
        this.clanRank = ClanEntity.n(m.clanRank());
        this.donations = ClanEntity.n(m.donations());
        this.donationsReceived = ClanEntity.n(m.donationsReceived());
    }

    public Long getId() { return id; }
    public ClanEntity getClan() { return clan; }
    public String getPlayerTag() { return playerTag; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getTownHallLevel() { return townHallLevel; }
    public int getExpLevel() { return expLevel; }
    public int getTrophies() { return trophies; }
    public int getClanRank() { return clanRank; }
    public int getDonations() { return donations; }
    public int getDonationsReceived() { return donationsReceived; }
}
