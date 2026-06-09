package com.example.demo.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.ProductRequestDTO;
import com.example.demo.dtos.ProductResponseDTO;
import com.example.demo.entities.Product;

import lombok.RequiredArgsConstructor;

@Component 
//why component ? -> Spring creates ONE instnace of it + kept 
//in APPLICATION CONTEXT! = cotnainer of all MANAGED OBJCETS!
//so can be injceted with constructor inkcetions

//ACTUALLY even @service and @repo and @Controller are all @Compnent LOL
@RequiredArgsConstructor
public class ProductMapper {
	private final CategoryMapper categoryMapper;
    private final ProductVariantMapper variantMapper;
    
    //customization EXTRA SYRUP PUMP! 
    private final CustomizationMapper customizationMapper;

    
    //product -> productResponseDTO
    public ProductResponseDTO toResponseDTO(Product entity) {
    	if (entity == null) return null;
    	
    	return ProductResponseDTO.builder()
    			.id(entity.getId())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .prix(entity.getPrix())
                .prixPromo(entity.getPrixPromo())
                .sellerId(entity.getSeller() != null ? entity.getSeller().getId() : null)
                .stock(entity.getStock())
                .actif(entity.isActif())
                .dateCreation(entity.getDateCreation())
                //categories -> list 
                .categories(entity.getCategories().stream()
                		.map(categoryMapper::MapToResponseDTO)
                		.toList())
                .images(entity.getImages())
                //variants -> List varianttes mapper with stream 
                .variants(entity.getVariants().stream()
                		.map(variantMapper::toResponseDTO)
                		.toList())
                
                //CUSTOMIZATION! 
                .customizations(entity.getCustomizations().stream()
                		.map(customizationMapper::toResponseDTO)
                		.toList()
                		)
                
                .build();
    }
    //Product request dto -> Product 
    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;
        //the product entity fresh
        Product product = new Product();
        product.setNom(dto.getNom());
        product.setDescription(dto.getDescription());
        product.setPrix(dto.getPrix());
        product.setPrixPromo(dto.getPrixPromo());
        product.setStock(dto.getStock());
        product.setImages(dto.getImages());
        product.setActif(true);

        //categories variants -> service ? we need repos.. for linking 
        //VARIANT HAS PRODUCT, but to save variant -> must save product first oh..
        //-> just variant DTOS here ??
        if(dto.getVariants()!=null) {
        	product.setVariants(dto.getVariants().stream()
        			.map(variantMapper::toEntity)//dto -> entity
        			.collect(Collectors.toList()));
        			
        	//.collect is IMMUTABLE list 
        	//.toListis IMMUTABLE
        } 
        
        //customizations 
        if(dto.getCustomizations() !=null) {
        	product.setCustomizations(dto.getCustomizations().stream()
        			.map(customizationMapper::toEntity)
        			.collect(Collectors.toList())
        			);
        }
        return product; 
    }
             
}
