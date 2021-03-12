package music.service.impl;

import music.model.entities.UserEntity;
import music.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MusicDbUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public MusicDbUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username with " + username + "was not found"));
        return mapToUserDetails(userEntity);
    }


    private UserDetails mapToUserDetails(UserEntity userEntity) {

        List<GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name()))
                .collect(Collectors.toList());

        UserDetails result = new User(
                userEntity.getName(),
                userEntity.getPassword(),
                authorities
        );

        return result;
    }
}
