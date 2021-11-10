package org.generation.blogPessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gen.bloguinhopessoal.model.PostagemModel;
import com.gen.bloguinhopessoal.repository.PostagemRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin("*")
@Api(tags = "Controlador de Postagens", description = "Utilitários de Postagens.")
@RequestMapping("/postagens")
public class PostagemController {

	private @Autowired PostagemRepository repository;
	
	@ApiOperation(value = "Procura em uma lista todas as postagens existentes.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Postagens não existentes na lista."),
			@ApiResponse(code = 200, message = "Lista de postagem encontrada.")
	})
	@GetMapping("/lista")
	public ResponseEntity<List<PostagemModel>> getAll(String lista){
		List<PostagemModel> objetoLista = repository.findAll();
		
		if (objetoLista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoLista);
		}
	}
	@ApiOperation(value = "Procura uma lista com postagens relacionadas.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Postagens não encontradas."),
			@ApiResponse(code = 200, message = "Lista com as postagens.")
	})
	@GetMapping("/pesquisar/{postagem_titulo}")
	public ResponseEntity<List<PostagemModel>> getByTitulo(@PathVariable(value = "postagem_titulo") String tituloPostagem){
		List<PostagemModel> objetoTituloPostagem = repository.findAllByTituloPostagemContainingIgnoreCase(tituloPostagem);
		
		if (objetoTituloPostagem.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoTituloPostagem);
		}
	}
	@ApiOperation(value = "Pesquisa uma única postagem apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Postagem encontrada com sucesso."),
			@ApiResponse(code = 204, message = "Postagem não encontrado no sistema.")
	})
	@GetMapping("pesquisar/id/{postagem_id}")
	public ResponseEntity<PostagemModel> getByIdPostagem(@PathVariable(value = "postagem_id") Long idPostagem) {
		return repository.findByIdPostagem(idPostagem).map(resp -> ResponseEntity.ok(resp))
			.orElseThrow(() -> {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,
						"Esta postagem parece não existir!");
			});
	}
	@ApiOperation(value = "Publica uma postagem nova.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Postagem criada com sucesso.")
	})
	@PostMapping("/publicar")
	public ResponseEntity<PostagemModel> post(@Valid @RequestBody PostagemModel novaPostagem){
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}
	@ApiOperation(value = "Atualiza uma postagem existente.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Postagem atualizada.")
	})
	@PutMapping("/atualizar")
	public ResponseEntity<PostagemModel> update(@Valid @RequestBody PostagemModel atualizarPostagem){
		return ResponseEntity.status(201).body(repository.save(atualizarPostagem));
	}
	@ApiOperation(value = "Deleta uma única postagem apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Postagem deletada com sucesso."),
			@ApiResponse(code = 404, message = "Postagem não encontrada no sistema.")
	})
	@DeleteMapping("/deletar/{id_postagem}")
	public ResponseEntity<PostagemModel> deleteById(@PathVariable(value = "id_postagem") Long idPostagem){
		Optional<PostagemModel> objetoDeletar = repository.findById(idPostagem);
		
		if(objetoDeletar.isPresent()) {
			repository.deleteById(idPostagem);
		return ResponseEntity.status(204).build();
		} else {
		return ResponseEntity.status(404).build();
		}
	}
}