package cl.practice.example.service.impl;

import cl.practice.example.dto.UserDto;
import cl.practice.example.entity.User;
import cl.practice.example.repository.UserRepository;
import cl.practice.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${password.regex}")
    private String passwordRegex;

    @Value("${email.regex}")
    private String emailRegex;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDto userDto) {
        if (!isValidEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Correo ya registrado");
        }

        if (!isValidPassword(userDto.getPassword())) {
            throw new IllegalArgumentException("La contraseña tiene que tener: " +
                    "Minimo 8 caracteres, letra mayúscula y minúscula, numeros, carácter especial");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setPhones(userDto.getPhones());
        user.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String fechaHora = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        user.setCreated(fechaHora);
        user.setModified(fechaHora);
        user.setLastLogin(fechaHora);
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email no es válido");
        }
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email inexistente");
        }
        User user = userRepository.findByEmail(email);
        return user;
    }


    private boolean isValidPassword(String password) {
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches(emailRegex, email);
    }
}
