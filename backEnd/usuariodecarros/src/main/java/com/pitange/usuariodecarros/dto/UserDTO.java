package com.pitange.usuariodecarros.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pitange.usuariodecarros.entities.User;
import com.pitange.usuariodecarros.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Component
@AllArgsConstructor
public class UserDTO {
    
    public UserDTO() {}

    private Long id;
    
    @NotEmpty(message = "{first.name.mandatory}")
    private String firstName;
    
    @NotEmpty(message = "{field.mandatory}")
    private String lastName;
    
    @Email
    @NotEmpty(message = "{field.mandatory}")
    private String email;
    
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotEmpty(message = "{field.mandatory}")
    private LocalDate birthday;
    
    @NotEmpty(message = "{field.mandatory}")
    private String phone;
    
    @NotEmpty(message = "{field.mandatory}")
    private String login;
    
    @NotEmpty(message = "{field.mandatory}")
    private String password;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;
    
    private Set<UserRole> roles;

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .dataCadastro(user.getDataCadastro())
                .roles(user.getRoles())
                .build();
    }

    public static User toModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .birthday(userDTO.getBirthday())
                .phone(userDTO.getPhone())
                .dataCadastro(userDTO.getDataCadastro())
                .roles(userDTO.getRoles())
                .build();
    }
}
