package com.mukijidev.board.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 로그인할 떄 security가 이 이름의 사용자 정보를 달라
// User entity -> UserDetails

@Service
public class BoardUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public BoardUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        return new LoginUser(user);
    }
}