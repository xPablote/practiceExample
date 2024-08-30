package cl.practice.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;
    private String password;

    @ElementCollection
    @CollectionTable(name = "phone",
                     joinColumns = @JoinColumn(name = "user_id"))
    private List<Phone> phones;

    private String created;
    private String modified;
    private String lastLogin;
    private boolean isActive;
}
