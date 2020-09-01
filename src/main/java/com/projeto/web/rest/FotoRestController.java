package com.projeto.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.model.Usuario;
import com.projeto.service.FotoStorageService;
import com.projeto.service.UsuarioService;
import com.projeto.service.storage.Foto;
import com.projeto.util.dto.ExcluirFoto;

@RestController
@RequestMapping(value="/foto") 
public class FotoRestController {
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@PostMapping(value="/gravar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<Foto> uploadFoto(@RequestParam MultipartFile foto ) throws IOException {
		String nomeFoto = fotoStorageService.gerarNomeArquivo(foto.getOriginalFilename());
		Foto fileFoto = new Foto();
		fileFoto.setNomeArquivo(nomeFoto);
		fileFoto.setInputStream(foto.getInputStream());
		fileFoto.setContentType(foto.getContentType());
		fotoStorageService.armazenar(fileFoto);
		return ResponseEntity.ok().body(fileFoto);
	}
	
	@GetMapping("/{nome_foto}")
	public byte[] recuperarFoto(@PathVariable String nome_foto) {
		fotoStorageService.recuperar(nome_foto);
		return fotoStorageService.recuperarFoto(nome_foto);
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ExcluirFoto> excluirFoto(@RequestBody ExcluirFoto excluirFoto) {
		System.out.println("passando pelo delete <<<<<<<<<<<<<<<<<<< "+excluirFoto);
		if (fotoStorageService.remover(excluirFoto.getNomeArquivo())) {
			System.out.println("passando pelo if >>>>>>>>>>>>>>>>>>>");
			Usuario usuario = usuarioService.clearFoto(Long.valueOf(excluirFoto.getId()));
			excluirFoto = new ExcluirFoto();
			excluirFoto.setNomeArquivo(usuario.getFoto());
			excluirFoto.setId(usuario.getId().toString());
			excluirFoto.setContentType(usuario.getContentType());
		};
		return ResponseEntity.ok().body(excluirFoto);
	}
	
	

}
