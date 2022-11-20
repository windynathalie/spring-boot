package week7.learn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import week7.learn.model.entity.DetailUser;
import week7.learn.model.entity.User;

@Repository
public interface DetailUserRepository extends JpaRepository<DetailUser, Long> {
    Optional<DetailUser> findByUser(User user);
}
