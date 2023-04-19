package org.skomorokhin.marketautumn.services;

import lombok.AllArgsConstructor;
import org.skomorokhin.marketautumn.dto.RegistRequest;
import org.skomorokhin.marketautumn.model.entities.Role;
import org.skomorokhin.marketautumn.model.entities.User;
import org.skomorokhin.marketautumn.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByLogin(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }


    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByLogin(username);
    }


    public void addNewUser(RegistRequest registRequest) {
        User user = User.builder()
                .login(registRequest.getLogin())
                .password(registRequest.getPassword())
                .name(registRequest.getName())
                .surname(registRequest.getSurname())
                .email(registRequest.getEmail())
                .role(Role.builder()
                        .id(3)
                        .name("ROLE_CLIENT")
                        .build())
                .build();
        userRepository.save(user);
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

}
