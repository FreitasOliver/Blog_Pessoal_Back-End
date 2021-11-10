package org.generation.blogPessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gen.bloguinhopessoal.model.PostagemModel;

@Repository
public interface PostagemRepository extends JpaRepository<PostagemModel, Long>{

	public List<PostagemModel> findAllByTituloPostagemContainingIgnoreCase(String tituloPostagem);
	
	public Optional<PostagemModel> findByIdPostagem(Long idPostagem);
}