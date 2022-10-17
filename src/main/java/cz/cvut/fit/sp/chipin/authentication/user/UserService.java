package cz.cvut.fit.sp.chipin.authentication.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String USER_NOT_FOUND = "User with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String saveUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new IllegalStateException("Email already taken");

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        //TODO: send e-mail

        return "Saved";
    }

    public User getUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null)
            return user;
        throw new Exception("user doesn't exists(getUser() method in UserService)");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
