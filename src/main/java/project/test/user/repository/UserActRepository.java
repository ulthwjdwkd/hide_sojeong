package project.test.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.test.user.entity.Activity;

import java.time.LocalDateTime;

@Repository
public interface UserActRepository extends JpaRepository<Activity, Long> {

    @Modifying // @Query와 함께 DB 수정할 때 사용
    @Query("UPDATE Activity us SET us.point = 0, us.level = 1, us.lastPointUpdateAt = :resetTime")
    void resetActivity(LocalDateTime resetTime);

    Activity findByUser_UsersId(int usersId);

}
