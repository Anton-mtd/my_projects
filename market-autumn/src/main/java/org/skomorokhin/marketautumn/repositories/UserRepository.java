package org.skomorokhin.marketautumn.repositories;

import org.skomorokhin.marketautumn.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
