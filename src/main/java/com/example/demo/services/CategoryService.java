package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.CategoryRequestDTO;
import com.example.demo.dtos.CategoryResponseDTO;
import com.example.demo.entities.Category;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappers.CategoryMapper;
import com.example.demo.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional
//transactional ? -> if a problem occurs (server crash..) 
//-> Spring will roll it back! so no ghost catggories ever
public class CategoryService {
	private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    //category reqeust -> response using the builder + add parent
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
    
    //1- mapper -> entity 
    Category.CategoryBuilder builder = categoryMapper.toBuilder(dto);
    //2- -> missing Parent entity
    if(dto.getParentId()!=null) {
    	Category parent = categoryRepository.findById(dto.getParentId())
    			.orElseThrow(()->new ResourceNotFoundException("Parent category"+dto.getParentId() +" not found."));
    	builder.parent(parent);
    	
    	
    	//A A A Aa -> we have the custom exceptions handler !! 
    }
    
    //save category
    Category savedCategory = categoryRepository.save(builder.build());
    //IT HAS NO ID-> AUTO GENERATED SO SAVE AHHHH 
    
    //FINALLY RETURN THE CATEGORY REPONSE DTO
    return categoryMapper.MapToResponseDTO(savedCategory);
    
    }
    
    //function -> get JUST the top level categories (parnt_id=null)
    public List<CategoryResponseDTO> getRootCategories(){
    	return categoryRepository.findByParentIsNull().stream().map(
    			categoryMapper::MapToResponseDTO //why recusrsion -> otherwise only top level 
    											//and when user hovers ? .... nothing lol
    			).toList();
    	
    	//findByNull -> get all root ? -> THEN pass it to the  mapper! 
    }//this actually NSTED LIST! List<CategoryResponseDTO> sousCategories.
	
    
    //PUT + DELETE category 
    public CategoryResponseDTO updateCategory(Long id,CategoryRequestDTO dto) {
    	//STEP 1 FIND THE CATEGORY -> not found -> ResourceNotFoundException my  best friend 
    	Category category_found = categoryRepository.findById(id)
    			.orElseThrow(()-> new ResourceNotFoundException("The category with the ID: "+ id + " cannot be found."));
    	
    	//IF FOUND UPDATE IT -> nom - <getNom ,description , parent id..
    	
    	category_found.setNom(dto.getNom());
    	category_found.setDescription(dto.getDescription());
    	
    	//panrent omg.. -> parnet not found again.
    	if(dto.getParentId()!=null) {
    		Category parent=categoryRepository.findById(dto.getParentId())
    				.orElseThrow(() ->new ResourceNotFoundException("Parent category " + dto.getParentId() + " cannot be found."));
    		
    		category_found.setParent(parent);
    	}else {
    		//intentionally parent_id =null -> new root 
    		category_found.setParent(null);
    	}
    	
    	return categoryMapper.MapToResponseDTO(categoryRepository.save(category_found));
    	//categoryRepository.save() returns an OBJECT !!!
    	
    }
    
    //DELTE CATEGORY 
    public void deleteCategory(Long id) {
    	//exists ? -> ResoucreNotFound
    	if(!categoryRepository.existsById(id)) {
    		throw new ResourceNotFoundException("Cannot delete category with " + id + " ,itcannot be found.");
    	}
    	
    	categoryRepository.deleteById(id);
    }
    
    //all of em 
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
