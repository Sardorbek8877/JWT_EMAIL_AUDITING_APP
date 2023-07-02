package uz.bek.appjwtrealemailauditing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bek.appjwtrealemailauditing.entity.User;
import uz.bek.appjwtrealemailauditing.entity.enums.RoleName;
import uz.bek.appjwtrealemailauditing.payload.ApiResponse;
import uz.bek.appjwtrealemailauditing.payload.RegisterDto;
import uz.bek.appjwtrealemailauditing.repository.RoleRepository;
import uz.bek.appjwtrealemailauditing.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse registerUser(RegisterDto registerDto){
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Email already exist", false);

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));

        user.setEmailCode(UUID.randomUUID().toString());

        userRepository.save(user);

        return new ApiResponse("User registered", true);
    }
}
