package com.example.demo.mappers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.CategoryRequestDTO;
import com.example.demo.dtos.CategoryResponseDTO;
import com.example.demo.entities.Category;

//MAPPER -> TURNS A DTO INTO A BUILDER! = half finished entity !!!
//-> service then takes the entity -> looks up parentid -> adds it -> saves it 
//mapper again takes the saved entity (havd parent ID) -> CatergoryResponseDTO
//-> finally user gets JSON with ID + category TREE
@Component 
//why componenet ? -> to inject it when needed
public class CategoryMapper {
	//function 1-; map entity to repsponse dto
	public CategoryResponseDTO MapToResponseDTO (Category entity) {
		var dto= CategoryResponseDTO.builder()
				.id(entity.getId())
				.nom(entity.getNom())
				.description(entity.getDescription())
				.build();
		
		//partie sous categories -> on the root ones omg ...
		//-> recursively get all sub from subs 
		if (entity.getSousCategories()!=null){
			dto.setSousCategories(entity.getSousCategories().stream()
					.map(this::MapToResponseDTO) //recursive call ehh so that's how.
					.toList());
			//why .stream () ? -> to get the list items ONE BY ONE -> do opertaions on each!
			//operations -> .map ! changes input to output !
			//actually same as .map(cat -> this.MapToResponseDTO(cat))
			//TRANSFORMING DATA -> .map() BEST!
		}else {
			dto.setSousCategories(new ArrayList<>()); //no sous cat		
		}
		return dto;
	}
	
	//fonction 2: RequestDTO -> entity 
	public Category.CategoryBuilder toBuilder (CategoryRequestDTO dto){
		if(dto==null) return null; 
		
		return Category.builder()
				.nom(dto.getNom())
				.description(dto.getDescription());
	}
	
}
