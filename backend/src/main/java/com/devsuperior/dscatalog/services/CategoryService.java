package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired // injeta automaticamente uma instância gerenciada pelo spring
	private CategoryRepository repository;// acessa o repository e chama as categorias do BD

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) { // findAll é o nome do método, pode ser qlq um.
		Page<Category> list = repository.findAll(pageRequest);// acessa o repository e chama as categorias do BD, coloca na													
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontrado!"));// lança a
																											// excessão
																											// q foi
																											// instanciada
		return new CategoryDTO(entity);

	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);// retornando a entidade para CategoryDTO
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id);// se chamar o getOne e o ID n existir o try catch trata a excessão
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {//exceção específica 
			throw new ResourceNotFoundException("Id não existe! " + id);

		}
	}

	// naõ coloca o @transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {//tratando exceção caso tenha um id inexistente
			throw new ResourceNotFoundException("Id não existe! " + id);//classe criada por nós

		}catch(DataIntegrityViolationException e) {//para garantir a integridade do BD (Não deletar o que não pode ser deletado)
			throw new DatabaseException("Violação de integridade!");
		}

	}

}
