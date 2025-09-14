package es.com.dls.maths.dlsmaths.repository;

import es.com.dls.maths.dlsmaths.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
