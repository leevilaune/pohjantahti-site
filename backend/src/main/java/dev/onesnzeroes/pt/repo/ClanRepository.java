package dev.onesnzeroes.pt.repo;

import dev.onesnzeroes.pt.domain.ClanEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClanRepository extends JpaRepository<ClanEntity, String> {

    /** Loads the clan together with its members in one query. */
    @EntityGraph(attributePaths = "members")
    Optional<ClanEntity> findByTag(String tag);

    List<ClanEntity> findAllByOrderByNameAsc();
}
