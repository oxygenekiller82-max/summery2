package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CategoryRequestDTO;
import com.example.demo.dtos.CategoryResponseDTO;
import com.example.demo.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories") // §§§§§§§§§§§§ REMOVE THE AUTH LATER !!!!!!! 
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
	
	//endpoint -> api/categories (GET bcz sure of it!) -> RETOURNE Arbre de categories
	@GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> getCategorytree(){
		return ResponseEntity.ok(categoryService.getRootCategories());
				//dang .ok -> put function inside okay.. 
	}
	
	//api/actegories avec POST + SEULEMENT POUR ADMIN!! 
	@PostMapping
	//@PreAuthorize("hasRole('ADMIN')") !!!! PUT IT BACK! 
	public ResponseEntity <CategoryResponseDTO> create (@Valid @RequestBody CategoryRequestDTO dto ){
		//remmeber the @NotBlank in the DTO !! -> @Valid ! 
		return new ResponseEntity<>(categoryService.createCategory(dto),HttpStatus.CREATED);
	}
	
	//PUT -> POST
	@PutMapping("/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO dto){
		return ResponseEntity.ok(categoryService.updateCategory(id,dto));
	}
	
	@DeleteMapping("/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		//CAPITAL V void -> controller method MUST return a type! 
		//void DOES NOT WORK bruh..
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build(); //204 code = scuess but nothing to show
	}
	
	

}
