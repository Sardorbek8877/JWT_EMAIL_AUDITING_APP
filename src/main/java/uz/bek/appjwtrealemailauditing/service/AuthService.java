package uz.bek.appjwtrealemailauditing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bek.appjwtrealemailauditing.entity.User;
import uz.bek.appjwtrealemailauditing.entity.enums.RoleName;
import uz.bek.appjwtrealemailauditing.payload.ApiResponse;
import uz.bek.appjwtrealemailauditing.payload.RegisterDto;
import uz.bek.appjwtrealemailauditing.repository.RoleRepository;
import uz.bek.appjwtrealemailauditing.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;

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
        // Email yuborish metodini chaqiramiz
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Ro'yxatdan muvaffaqiyatli o'tdingiz. Email orqali accoutingizni tasdiqlang", true);
    }

    public boolean sendEmail(String sendingEmail, String emailCode){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sardorbeksafarov8844@gmail.com");
            message.setTo(sendingEmail);
            message.setSubject("Accountni tasdiqlash");
            message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode +
                    "&email=" + sendingEmail +"'>Tasdiqlash</a>");
            javaMailSender.send(message);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkount tasdiqlandi", true);
        }
        return new ApiResponse("Akkount allaqachon tasdiqlangan", false);
    }
}
