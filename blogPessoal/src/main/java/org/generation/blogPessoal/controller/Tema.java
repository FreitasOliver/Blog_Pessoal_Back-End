package org.generation.blogPessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gen.bloguinhopessoal.model.TemaModel;
import com.gen.bloguinhopessoal.repository.TemaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin("*")
@Api(tags = "Controlador de Temas", description = "Utilitário de Temas.")
@RequestMapping("/tema")
public class TemaController {

	private @Autowired TemaRepository repositorio;
	
	@ApiOperation(value = "Procura todos os temas no sistema.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de temas encontrada."),
			@ApiResponse(code = 204, message = "Lista de temas inexistente.")
	})
	@GetMapping("/todos")
	public ResponseEntity<List<TemaModel>> getAll() {
		List<TemaModel> objetoTema = repositorio.findAll();
		
		if (objetoTema.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoTema);
		}
	}
	@ApiOperation(value = "Procura uma lista com temas semelhantes ao requisitado.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de temas encontrada."),
			@ApiResponse(code = 204, message = "Lista de temas inexistente.")
	})
	@GetMapping("/{tema_nome}")
	public ResponseEntity<List<TemaModel>> getByTemaNome(@PathVariable (value = "tema_nome") String temaNome) {
		List<TemaModel> objetoTema = repositorio.findAllByTemaContainingIgnoreCase(temaNome);
		
		if (objetoTema.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoTema);
		}
	}
	@ApiOperation(value = "Cria um novo tema no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Tema criado.")
	})
	@PostMapping("/salvar")
	public ResponseEntity<TemaModel> salvar (@Valid @RequestBody TemaModel novoTema) {
		return ResponseEntity.status(201).body(repositorio.save(novoTema));
	}
	@ApiOperation(value = "Atualiza um tema no banco de dados apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Tema atualizado.")
	})
	@PutMapping("/atualizar")
	public ResponseEntity<TemaModel> atualizar (@Valid @RequestBody TemaModel atualizarTema) {
		return ResponseEntity.status(201).body(repositorio.save(atualizarTema));
	}
	@ApiOperation(value = "Deleta um único tema apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Tema deletado com sucesso."),
			@ApiResponse(code = 404, message = "Tema não encontrado no sistema.")
	})
	@DeleteMapping("/deletar/{id_tema}")
	public ResponseEntity<TemaModel> deletar (@PathVariable (value = "id_tema") Long idTema) {
		Optional<TemaModel> objetoIdTema = repositorio.findByIdTema(idTema);
		
		if (objetoIdTema.isPresent()) {
			repositorio.deleteById(idTema);
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(404).build();
				}
	}
}