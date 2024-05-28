package com.pitange.usuariodecarros.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	
	public UserDTO(){}

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
	
	private UserRole role;

    public static UserDTO toDTO(User personModel) {
        return UserDTO.builder()
                .id(personModel.getId())
                .firstName(personModel.getFirstName())
                .lastName(personModel.getLastName())
                .email(personModel.getEmail())
                .birthday(personModel.getBirthday())
                .phone(personModel.getPhone())
                .dataCadastro(personModel.getDataCadastro())
                .role(personModel.getRole())
                .build();
    }

    public static User toModel(UserDTO personDTO) {
        return User.builder()
                .id(personDTO.getId())
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .email(personDTO.getEmail())
                .birthday(personDTO.getBirthday())
                .phone(personDTO.getPhone())
                .dataCadastro(personDTO.getDataCadastro())
                .role(personDTO.getRole())
                .build();
    }
}
