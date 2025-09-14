package es.com.dls.maths.dlsmaths.repository;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameConfigRepository extends JpaRepository<GameConfig, Long> {
    Optional<GameConfig> findFirstByOrderByIdAsc();
    Optional<GameConfig> findByDescriptionIgnoreCase(String description);
}
