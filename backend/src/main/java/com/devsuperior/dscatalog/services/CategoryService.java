package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired//injeta automaticamente uma instância gerenciada pelo spring
	private CategoryRepository repository;//acessa o repository e chama as categorias do BD
	
	public List<Category> findAll() { //findAll é o nome do método, pode ser qlq um.
		return repository.findAll();//acessa o repository e chama as categorias do BD, coloca na interface(classe) o @Repository
	}

}
