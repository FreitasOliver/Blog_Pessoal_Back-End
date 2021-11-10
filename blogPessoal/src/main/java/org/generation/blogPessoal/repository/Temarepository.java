package org.generation.blogPessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gen.bloguinhopessoal.model.TemaModel;

@Repository
public interface TemaRepository extends JpaRepository<TemaModel, Long> {
	
	public List<TemaModel> findAllByTemaContainingIgnoreCase(String tema);
	
	public Optional<TemaModel> findByIdTema(Long idTema);
}