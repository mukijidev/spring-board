package com.mukijidev.board.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if(userRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException(request.username());
        }

        String encoded = passwordEncoder.encode(request.password());
        try {
            User user = userRepository.save(new User(request.username(), encoded));
            return UserResponse.from(user);
        } catch (DataIntegrityViolationException e) { // 동시 가입으로 UNIQUE 위반
            throw new DuplicateUsernameException(request.username());
        }
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(()-> new IllegalStateException("Logged-in user not found: "+ id));
    }

}
