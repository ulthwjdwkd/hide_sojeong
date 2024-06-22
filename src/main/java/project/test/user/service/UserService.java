package project.test.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.test.user.dto.req.SignUpReqDto;
import project.test.user.dto.res.ActivityInfoResDto;
import project.test.user.dto.res.UserInfoResDto;
import project.test.user.entity.Activity;
import project.test.user.jwt.JWTUtil;
import project.test.user.repository.RefreshTokenRepository;
import project.test.user.repository.UserActRepository;
import project.test.user.repository.UserRepository;
import project.test.user.entity.User;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserActRepository userActRepository;

    private final Map<String, String> emailCodeMap = new HashMap<>();

    public boolean isUseridExist(String userId){
        return userRepository.existsByUserId(userId);
    }

    public boolean isNicknameExist(String nickname){
        return userRepository.existsByUserNickname(nickname);
    }

    public boolean isPasswordExist(String userId, String password){
        User user = userRepository.findByUserId(userId);
        String userPassword = user.getUserPassword();
        return bCryptPasswordEncoder.matches(password, userPassword);
    }

    @Transactional
    public void signUp(SignUpReqDto signUpReqDto) {
        User user = new User();
        user.setUserId(signUpReqDto.getUserid());
        user.setUserName(signUpReqDto.getUsername());
        user.setUserNickname(signUpReqDto.getNickname());
        user.setUserPassword(bCryptPasswordEncoder.encode(signUpReqDto.getPassword()));
        user.setUserEmail(signUpReqDto.getEmail());
        user.setUserRole("ROLE_ADMIN");

        Activity activity = new Activity(user);

        userRepository.save(user);
        userActRepository.save(activity);

    }

    public Optional<User> findUserId(String userId, String email){
        return userRepository.findByUserNameAndUserEmail(userId, email);

    }

    public boolean findPassword(String username, String userId, String email){
        return userRepository.existsByUserNameAndUserIdAndUserEmail(username, userId, email);
    }

    public UserInfoResDto getUserInfo(String userId){
        User user = userRepository.findByUserId(userId);
        return UserInfoResDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .nickname(user.getUserNickname())
                .email(user.getUserEmail())
                .build();
    }

    public ActivityInfoResDto getActivity(String userId){
        User user = userRepository.findByUserId(userId);
        Activity activity = userActRepository.findByUser_UsersId(user.getUsersId());
        return ActivityInfoResDto.builder()
                .level(activity.getLevel())
                .point(activity.getPoint())
                .alarmCycle(user.getAlarmCycle())
                .build();
    }

    @Transactional
    public void updateNickname(String userId, String nickname){
        User user = userRepository.findByUserId(userId);
        if (userRepository.existsByUserNickname(user.getUserNickname())) {
            throw new RuntimeException("이미 존재하는 닉네임 입니다. : " + user.getUserNickname());
        }
        user.setUserNickname(nickname);
    }

    @Transactional
    public void updatePassword(String userId, String password){
        User user = userRepository.findByUserId(userId);
        user.setUserPassword(bCryptPasswordEncoder.encode(password));
    }

    @Transactional
    public void updateEmail(String userId, String email){
        User user = userRepository.findByUserId(userId);
        user.setUserEmail(email);
    }

    @Transactional
    public void removeUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            userRepository.delete(user); // 회원 정보 삭제
        } else {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + userId);
        }
    }

    public String getLoginIdFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return authentication.getName();

    }

    @Transactional
    public void removeRefreshToken(String refresh) {

        jwtUtil.isExpired(refresh);

        String category = jwtUtil.getCategory(refresh);

        Boolean isExist = refreshTokenRepository.existsByRefresh(refresh);

        refreshTokenRepository.deleteByRefresh(refresh);

    }

    @Transactional
    public void incrementPoint(String userId){
        User user = userRepository.findByUserId(userId);
        Activity activity = userActRepository.findByUser_UsersId(user.getUsersId());
        if(activity != null){
            activity.setPoint(activity.getPoint() + 1);
            userActRepository.save(activity);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * ?") // 매 1시간마다 갱신
    public void updateLevel() {
        List<Activity> activities = userActRepository.findAll();
        for (Activity activity : activities) {
            int currentPoints = activity.getPoint();
            int newLevel = determineLevel(currentPoints);
            if (newLevel != activity.getLevel()) {
                activity.setLevel(newLevel);
                userActRepository.save(activity);
            }
        }
    }

    private int determineLevel(int points) {
        if (points >= 10) {
            return 5;
        } else if (points >= 8) {
            return 4;
        } else if (points >= 6) {
            return 3;
        } else if (points >= 4) {
            return 2;
        } else if (points >= 2) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 초기화
    public void resetActivity(){
        LocalDateTime now = LocalDateTime.now();
        userActRepository.resetActivity(now);
    }

}
