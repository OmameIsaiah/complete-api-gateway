package com.complete.api.gateway.security.user;

import com.complete.api.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.complete.api.gateway.util.AppMessages.EMAIL_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(
                userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format(EMAIL_NOT_FOUND, username)))
        );
    }

}
