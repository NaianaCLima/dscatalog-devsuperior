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

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired // injeta automaticamente uma instância gerenciada pelo spring
	private ProductRepository repository;// acessa o repository e chama as categorias do BD

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) { // findAll é o nome do método, pode ser qlq um.
		Page<Product> list = repository.findAll(pageRequest);// acessa o repository e chama as categorias do BD, coloca na													
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontrado!"));																											
		return new ProductDTO(entity, entity.getCategories());

	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);// retornando a entidade para ProductDTO
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);// se chamar o getOne e o ID n existir o try catch trata a excessão
			//entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTO(entity);
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
