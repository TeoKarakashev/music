package music.service.impl;

import music.model.entities.UserEntity;
import music.model.entities.UserRoleEntity;
import music.model.entities.enums.UserRole;
import music.model.service.UserRegisterServiceModel;
import music.repository.UserRepository;
import music.repository.UserRoleRepository;
import music.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MusicDbUserService musicDbUserService;

    public UserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, MusicDbUserService musicDbUserService) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.musicDbUserService = musicDbUserService;
    }

    @Override
    public void seedUsers() {

        if (userRepository.count() == 0) {

            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);

            userRoleRepository.saveAll(List.of(adminRole, userRole));

            UserEntity admin = new UserEntity().setName("admin").setFullName("admin").setPassword(passwordEncoder.encode("123"));
            UserEntity user = new UserEntity().setName("user").setFullName("user").setPassword(passwordEncoder.encode("123"));

            admin.setRoles(List.of(adminRole, userRole));
            user.setRoles(List.of(userRole));

            userRepository.saveAll(List.of(admin, user));
        }
    }

    @Override
    public void registerAndLoginUser(UserRegisterServiceModel serviceModel) {
        UserEntity newUser = this.modelMapper.map(serviceModel, UserEntity.class);

        UserRoleEntity userRoleEntity = this.userRoleRepository.findByRole(UserRole.USER).orElseThrow(() -> new IllegalStateException("USER role not found"));
        newUser.addRole(userRoleEntity);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        this.userRepository.save(newUser);

        UserDetails principal = musicDbUserService.loadUserByUsername(newUser.getName());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                newUser.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public boolean userExists(String username) {
        return this.userRepository.findByName(username).isPresent();
    }
}
