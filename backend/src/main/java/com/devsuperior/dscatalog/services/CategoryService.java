package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired//injeta automaticamente uma instância gerenciada pelo spring
	private CategoryRepository repository;//acessa o repository e chama as categorias do BD
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() { //findAll é o nome do método, pode ser qlq um.
		List<Category> list = repository.findAll();//acessa o repository e chama as categorias do BD, coloca na interface(classe) o @Repository
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

}
