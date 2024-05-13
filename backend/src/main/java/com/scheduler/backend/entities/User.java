package com.scheduler.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Size(max = 100)
    @Column(name = "surname", length = 100, nullable = false)
    private String surname;

    @Getter
    @Setter
    @Size(max = 100)
    @Column(name = "firstname", length = 100)
    private String firstname;

    @Getter
    @Setter
    @Size(max = 100)
    @Column(name = "patronymic", length = 100)
    private String patronymic;

    @Size(max = 100)
    @NotNull
    @Getter
    @Setter
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Size(max = 255)
    @NotNull
    @Getter
    @Setter
    @Column(name = "password", nullable = false)
    private String password;
}
