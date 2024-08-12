package com.pitange.usuariodecarros.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pitange.usuariodecarros.entities.User;
import com.pitange.usuariodecarros.entities.Roles;

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
    
    @NotEmpty(message = "{name.mandatory}")
    private String username;
    
    @Email
    @NotEmpty(message = "{email.mandatory}")
    private String email;
    
    @NotEmpty(message = "{password.mandatory}")
    private String password;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotEmpty(message = "{birthday.mandatory}")
    private LocalDate birthday;
    
    @NotEmpty(message = "{phone.mandatory}")
    private String phone;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;
    
    private Set<Roles> roles;

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
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
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .birthday(userDTO.getBirthday())
                .phone(userDTO.getPhone())
                .dataCadastro(userDTO.getDataCadastro())
                .roles(userDTO.getRoles())
                .build();
    }
}
