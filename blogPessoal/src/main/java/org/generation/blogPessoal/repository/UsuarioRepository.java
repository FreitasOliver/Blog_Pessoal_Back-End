package org.generation.blogPessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gen.bloguinhopessoal.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long>{
	
	Optional<UsuarioModel> findByEmail(String email);
	
	List<UsuarioModel> findAllByNomeContainingIgnoreCase(String nome);
}