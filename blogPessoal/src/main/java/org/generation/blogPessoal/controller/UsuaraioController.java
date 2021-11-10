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

import com.gen.bloguinhopessoal.model.UsuarioModel;
import com.gen.bloguinhopessoal.model.dtos.UsuarioLoginDTO;
import com.gen.bloguinhopessoal.repository.UsuarioRepository;
import com.gen.bloguinhopessoal.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin("*")
@Api(tags = "Controlador de Usuários", description = "Utilitário de Usuários.")
@RequestMapping("/usuario")
public class UsuarioController {

	private @Autowired UsuarioRepository repository;
	private @Autowired UsuarioService services;
	
	@ApiOperation(value = "Mostra todos os usuários no sistema.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de usuários."),
			@ApiResponse(code = 204, message = "Lista inexistente.")
	})
	@GetMapping("/pesquisar")
	public ResponseEntity<List<UsuarioModel>> getAll(String lista){
		List<UsuarioModel> objetoLista = repository.findAll();
		
		if (objetoLista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoLista);
		}
	}
	@ApiOperation(value = "Faz uma pesquisa de usuários com o nome similiar ao requisitado.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de usuários."),
			@ApiResponse(code = 204, message = "Lista inexistente.")
	})
	@GetMapping("/pesquisar/{usuario_nome}")
	public ResponseEntity<List<UsuarioModel>> getByTitulo(@PathVariable(value = "usuario_nome") String nomeUsuario){
		List<UsuarioModel> objetoUsuario = repository.findAllByNomeContainingIgnoreCase(nomeUsuario);
		
		if (objetoUsuario.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.ok(objetoUsuario);
		}
	}
	@ApiOperation(value = "Procura um único usuário apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário encontrado."),
			@ApiResponse(code = 204, message = "Usuário inexistente.")
	})
	@GetMapping("pesquisar/id/{usuario_id}")
	public ResponseEntity<UsuarioModel> getByIdPostagem(@PathVariable(value = "usuario_id") Long idUsuario) {
		return repository.findById(idUsuario).map(resp -> ResponseEntity.ok(resp))
			.orElseThrow(() -> {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,
						"Este Úsuario parece não existir!");
			});
	}
	@ApiOperation(value = "Cria um usuário no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Usuário criado com sucesso."),
			@ApiResponse(code = 400, message = "Usuário já existente no sistema.")
	})
	@PostMapping("/criar")
	public ResponseEntity<Object> post(@Valid @RequestBody UsuarioModel novoUsuario){
		return services.usuarioCadastro(novoUsuario).map(resp -> ResponseEntity.status(201).body(resp))
				.orElse(ResponseEntity.status(400).build());
	}
	@ApiOperation(value = "Valida as credenciais do usuário.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Credenciais encontradas."),
			@ApiResponse(code = 400, message = "Credenciais inexistentes.")
	})
	@PutMapping("/credenciais")
	public ResponseEntity<Object> credenciais(@Valid @RequestBody UsuarioLoginDTO usuarioParaAutenticar){
		Optional<?> objetoCredenciais = services.pegarCredenciais(usuarioParaAutenticar);
		
		if(objetoCredenciais.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As credenciais inseridas estão inválidas!");
		} else {
			return ResponseEntity.status(201).body(objetoCredenciais.get());
		}
	}
	@ApiOperation(value = "Atualiza um usuário no sistema usando seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Usuário atualizado."),
			@ApiResponse(code = 400, message = "Usuário inexistente.")
	})
	@PutMapping("/atualizar")
	public ResponseEntity<UsuarioModel> update(@Valid @RequestBody UsuarioModel atualizarUsuario){
		return services.usuarioAtualizacao(atualizarUsuario).map(resp -> ResponseEntity.status(201).body(resp))
				.orElseThrow(() -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"O ID informado é inválido!");
				});
	}
	@ApiOperation(value = "Deleta um único usuário do banco de dados apartir de seu ID.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Usuário deletado."),
			@ApiResponse(code = 204, message = "Usuário não encontrado.")
	})
	@DeleteMapping("/deletar/{id_usuario}")
	public ResponseEntity<UsuarioModel> deleteById(@PathVariable(value = "id_usuario") Long idUsuario){
		Optional<UsuarioModel> objetoDeletar = repository.findById(idUsuario);
		
		if(objetoDeletar.isPresent()) {
			repository.deleteById(idUsuario);
		return ResponseEntity.status(204).build();
		} else {
		return ResponseEntity.status(404).build();
		}
	}
}