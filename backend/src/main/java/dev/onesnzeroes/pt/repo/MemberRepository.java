package dev.onesnzeroes.pt.repo;

import dev.onesnzeroes.pt.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    List<MemberEntity> findByClan_TagOrderByClanRankAsc(String clanTag);
}
