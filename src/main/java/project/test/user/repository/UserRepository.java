package project.test.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.test.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(String userid);

    Boolean existsByUserNickname(String nickname);

    Boolean existsByUserNameAndUserIdAndUserEmail(String username, String userId, String email);

    User findByUserId(String userId);

    Optional<User> findByUserNameAndUserEmail(String userId, String email);

}
