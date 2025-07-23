package gift.service;

import gift.dto.LoginRequestDTO;
import gift.jwt.JwtTokenProvider;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public String login(LoginRequestDTO login) {
        Optional<User> userOpt = userDao.findByUserid(login.getUserid());
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        return jwtTokenProvider.createToken(user.getUserid(),user.getPassword());
    }

    public User findByUserId(String userId) {
        Optional<User> userOpt = userDao.findByUserid(userId);
        User user = userOpt.orElseThrow(() -> new RuntimeException("user 찾을 수 없음"));
        return user;
    }

    public void createUser(User user) {
        userDao.save(user);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void removeUser(Long id) {
        userDao.deleteById(id);
    }

    public Optional<User> findUserById(Long id) {
        return userDao.findById(id);
    }

    public void updateUser(Long id, User user) {
        userDao.save(user);
    }
}