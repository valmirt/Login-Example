package br.dev.valmirt.login.repository;

import br.dev.valmirt.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail (String email);

    @Query("SELECT u FROM User u WHERE u.situation = br.dev.valmirt.login.model.Situation.ACTIVE")
    List<User> findAllActive ();
}
