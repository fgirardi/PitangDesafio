package com.pitange.usuariodecarros.dto;

import java.time.LocalDateTime;

import com.pitange.usuariodecarros.entities.Car;
import com.pitange.usuariodecarros.entities.User;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

private Long id;
	
	@NotNull
	private Integer yearCar;
	
	@NotNull
	private String licensePlate;
	
	@NotNull
	private String model;
	
	@NotNull
	private String color;

    public static CarDTO fromModel(Car carModel) {
	 
    	return CarDTO.builder()
                .id(carModel.getId())
                .yearCar(carModel.getYearCar())
                .licensePlate(carModel.getLicensePlate())
                .model(carModel.getModel())
            	.color(carModel.getColor())
                .build();
    }

    public Car toModel() {
    	
    	return Car.builder()
                .id(this.getId())
                .yearCar(this.getYearCar())
                .licensePlate(this.getLicensePlate())
                .model(this.getModel())
            	.color(this.getColor())
                .build();
    }
}


