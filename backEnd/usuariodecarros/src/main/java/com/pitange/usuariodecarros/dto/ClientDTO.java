package com.pitange.usuariodecarros.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pitange.usuariodecarros.entities.User;
import com.pitange.usuariodecarros.entities.Client;
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
public class ClientDTO {
    
    public ClientDTO() {}

    private Long id;
    
    @NotEmpty(message = "{name.mandatory}")
    private String name;
    
    @Email
    @NotEmpty(message = "{email.mandatory}")
    private String email;
     
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotEmpty(message = "{birthday.mandatory}")
    private LocalDate birthday;
    
    @NotEmpty(message = "{phone.mandatory}")
    private String phone;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;
    
    public static ClientDTO toDTO(Client client) {
        
    	return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .birthday(client.getBirthday())
                .phone(client.getPhone())
                .dataCadastro(client.getDataCadastro())
                .build();
    }

    public static Client toModel(ClientDTO userDTO) {
        
    	return Client.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .birthday(userDTO.getBirthday())
                .phone(userDTO.getPhone())
                .dataCadastro(userDTO.getDataCadastro())
                .build();
    }
}
