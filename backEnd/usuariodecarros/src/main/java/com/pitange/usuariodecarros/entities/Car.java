package com.pitange.usuariodecarros.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 4)
	private Integer yearCar;
	
	@Column(nullable = false)
	private String licensePlate;
	
	@Column(nullable = false, length = 50)
	private String model;
	
	@Column(nullable = false, length = 100)
	private String color;
	
	@ManyToOne
	@JoinColumn(name = "id_person")
	private User person;
	
	@Column(name = "data_cadastro", updatable = false)
	private LocalDateTime dataCadastro;
	
	@PrePersist
	public void prePersist() {
		dataCadastro = LocalDateTime.now();
	}
	
}
