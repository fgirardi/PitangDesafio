package com.pitange.usuariodecarros.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCarDTO {

 
    private Long id;

    private UserDTO personDTO;

    private CarDTO carDTO;
}
