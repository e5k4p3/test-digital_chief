package com.e5k4p3.digitalchief.user.model;

import com.e5k4p3.digitalchief.user.model.enums.UserGender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "name", nullable = false, length = 30)
    String name;
    @Column(name = "surname", nullable = false, length = 50)
    String surname;
    @Column(name = "email", nullable = false, length = 100, unique = true)
    String email;
    @Column(name = "age", nullable = false)
    Integer age;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    UserGender gender;
}
