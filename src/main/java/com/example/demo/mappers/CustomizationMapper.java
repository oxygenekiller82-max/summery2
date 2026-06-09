package com.example.demo.mappers;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.CustomizationRequestDTO;
import com.example.demo.dtos.CustomizationResponseDTO;
import com.example.demo.entities.Customization;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomizationMapper {
	
	//entity -> customizationResponseDTO
	public CustomizationResponseDTO toResponseDTO (Customization entity) {
		if(entity==null) return null; 
		
		return CustomizationResponseDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
		        .extraPrice(entity.getExtraPrice())
		        .is_available(entity.is_available())
		        .build();

	}
	
	//dto -> entity
	public Customization toEntity(CustomizationRequestDTO dto) {
		if(dto==null) return null;
		
		//entity
		Customization customization = new Customization();
		customization.setName(dto.getName());
        customization.setExtraPrice(dto.getExtraPrice());
        customization.set_available(dto.is_available());
        return customization;
		
	}

}
