package cl.practice.example.dto;

import cl.practice.example.entity.Phone;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}
