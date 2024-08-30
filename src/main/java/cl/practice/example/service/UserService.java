package cl.practice.example.service;

import cl.practice.example.dto.UserDto;
import cl.practice.example.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User createUser(UserDto userDto);
    User getUserByEmail(String email);
}
