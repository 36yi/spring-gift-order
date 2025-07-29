package gift.service;

import gift.dto.LoginRequestDTO;
import gift.jwt.JwtTokenProvider;
import gift.model.Role;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public String login(LoginRequestDTO login) {
        Optional<User> userOpt = userRepository.findByUserid(login.getUserid());
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        return jwtTokenProvider.createToken(user.getUserid(),user.getPassword(),null);
    }

    public String kakaoLogin(LoginRequestDTO login, String kakaoAccessToken) {
        Optional<User> userOpt = userRepository.findByUserid(login.getUserid());
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        return jwtTokenProvider.createToken(user.getUserid(), user.getPassword(), kakaoAccessToken);
    }

    public User findByUserId(String userId) {
        Optional<User> userOpt = userRepository.findByUserid(userId);
        User user = userOpt.orElseThrow(() -> new RuntimeException("user 찾을 수 없음"));
        return user;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUser(Long id, User user) {
        userRepository.save(user);
    }
}