package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	//how to find all the top highest level categories -> FOR THE side bar ! 
	List<Category> findByParentIsNull(); //parent category = null :) 
	
	//how to find sub catgeories for a aprent by id 
	List<Category> findByParentId(Long parentId);
}
