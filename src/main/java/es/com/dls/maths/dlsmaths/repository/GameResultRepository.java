package es.com.dls.maths.dlsmaths.repository;

import es.com.dls.maths.dlsmaths.entity.GameResult;
import es.com.dls.maths.dlsmaths.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByUser(User user);
}
