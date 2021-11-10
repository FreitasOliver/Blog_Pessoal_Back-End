package org.generation.blogPessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gen.bloguinhopessoal.model.UsuarioModel;
import com.gen.bloguinhopessoal.model.dtos.UsuarioLoginDTO;
import com.gen.bloguinhopessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private @Autowired UsuarioRepository repository;

	public Optional<Object> usuarioCadastro(UsuarioModel usuarioParaCadastrar) {
		return repository.findByEmail(usuarioParaCadastrar.getEmail()).map(usuarioExistente -> {
			return Optional.empty();
		}).orElseGet(() -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuarioParaCadastrar.getSenha());
			usuarioParaCadastrar.setSenha(senhaCriptografada);
			return Optional.ofNullable(repository.save(usuarioParaCadastrar));
		});
	}

	public Optional<UsuarioModel> usuarioAtualizacao(UsuarioModel usuarioParaAtualizar) {
		return repository.findById(usuarioParaAtualizar.getIdUsuario()).map(resp -> {
			resp.setNome(usuarioParaAtualizar.getNome());
			resp.setSenha(usuarioParaAtualizar.getSenha());
			return Optional.ofNullable(repository.save(resp));
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}

	public Optional<?> pegarCredenciais(UsuarioLoginDTO usuarioParaAutenticar) {
		return repository.findByEmail(usuarioParaAutenticar.getEmail()).map(resp -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			if (encoder.matches(usuarioParaAutenticar.getSenha(), resp.getSenha())) {

				String estruturaBasic = usuarioParaAutenticar.getEmail() + ":" + usuarioParaAutenticar.getSenha();
				byte[] estruturaBase64 = Base64.encodeBase64(estruturaBasic.getBytes(Charset.forName("US-ASCII")));
				String tokenBasic = "Basic " + new String(estruturaBase64);

				usuarioParaAutenticar.setToken(tokenBasic);
				usuarioParaAutenticar.setIdUsuario(resp.getIdUsuario());
				usuarioParaAutenticar.setNome(resp.getNome());
				usuarioParaAutenticar.setSenha(resp.getSenha());

				return Optional.ofNullable(usuarioParaAutenticar);
			} else {
				return Optional.empty();
			}
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}
}