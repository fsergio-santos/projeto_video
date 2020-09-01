package com.projeto.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.config.ConfigProjeto;
import com.projeto.model.Role;
import com.projeto.model.Usuario;
import com.projeto.repository.filtros.UsuarioFiltro;
import com.projeto.repository.pagination.Pagina;
import com.projeto.service.JasperReportsService;
import com.projeto.service.RoleService;
import com.projeto.service.UsuarioService;
import com.projeto.service.exceptions.ConfirmeSenhaNaoInformadaException;
import com.projeto.service.exceptions.EmailCadastradoException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping(value="/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private JasperReportsService jasperReportsService;
	
	@Autowired
	private RoleService roleService;
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView listarUsuarios(UsuarioFiltro usuarioFiltro, HttpServletRequest request,
			 						   @RequestParam(value="size", required=false) Optional<Integer> size,
			 						   @RequestParam(value="page", required=false) Optional<Integer> page,
			 						   @RequestParam(value="sort", required=false) Optional<String> sort, 
			 						   @RequestParam(value="dir",  required=false) Optional<String> dir){
	
		Pageable pageable = null;
    	pageable = gerarPagina(dir.orElse("asc"), sort.orElse("id"), page.orElse(ConfigProjeto.INITIAL_PAGE), size.orElse(ConfigProjeto.INITIAL_PAGE_SIZE));
		Pagina<Usuario> pagina = new Pagina<>(usuarioService.listarUsuarioPaginacao(usuarioFiltro, pageable),size.orElse(ConfigProjeto.INITIAL_PAGE_SIZE),request);
		ModelAndView mv = new ModelAndView("/usuario/lista");
		mv.addObject("pageSizes", ConfigProjeto.PAGE_SIZES);
		mv.addObject("size", size.orElse(ConfigProjeto.INITIAL_PAGE_SIZE));
		mv.addObject("pagina", pagina );
		mv.addObject("sort", sort.orElse("id"));
		mv.addObject("dir",dir.orElse("asc"));
		return mv;
	}
	
	
	@GetMapping("/novo")
	public ModelAndView cadastroUsuario(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/usuario");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView buscarUsuarioAlterar(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("usuario/usuario");
		Usuario usuario = usuarioService.getOne(id);
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public ModelAndView buscarUsuarioExcluir(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("usuario/excluir_usuario");
		Usuario usuario = usuarioService.getOne(id);
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	
	@RequestMapping(value= "/incluir",method = RequestMethod.POST)
	public ModelAndView incluirUsuario(@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) {
			return cadastroUsuario(usuario);
		}
		try {
			usuarioService.save(usuario);	
		} catch(EmailCadastradoException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return cadastroUsuario(usuario);
		} catch( ConfirmeSenhaNaoInformadaException e) {
			result.rejectValue("confirmeSenha", e.getMessage(), e.getMessage());
			return cadastroUsuario(usuario);
		}
		return new ModelAndView("redirect:/usuario/list");
	}
	
	@RequestMapping(value= "/alterar",method = RequestMethod.POST)
	public ModelAndView alterarUsuario(@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) {
			return cadastroUsuario(usuario);
		}
		try {
			usuarioService.update(usuario);	
		} catch(EmailCadastradoException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return cadastroUsuario(usuario);
		}
		return new ModelAndView("redirect:/usuario/list");
	}
		
	@RequestMapping(value= "/excluir",method = RequestMethod.POST)
	public ModelAndView excluirUsuario(Usuario usuario) {
		usuarioService.deleteById(usuario.getId());
		return new ModelAndView("redirect:/usuario/list");
	}
	
	@ModelAttribute("roles")
	public List<Role> listaRoles(){
		return roleService.findAll();
	}
	
	
	private Pageable gerarPagina(String direcao, String atributo, Integer pag, Integer limite) {
		Pageable pageable = null;
		if (direcao.equals("asc")) {
		    pageable = PageRequest.of(pag, limite, Sort.Direction.ASC, atributo);
		} else {
			pageable = PageRequest.of(pag, limite, Sort.Direction.DESC, atributo);
		}
		return pageable;
	}
	
	
	@GetMapping("/relatorio")
	public void imprimirRelatorioUsuarioDownloadPdf(HttpServletResponse response) throws IOException, JRException {
		
		JasperPrint jasperPrint = null;
		jasperPrint = jasperReportsService.imprimeRelatorio("usuario");
	
	    response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"usuario.pdf\""));

	    OutputStream out = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	}
	
	@GetMapping("/relatorio_pdf")
	public ResponseEntity<byte[]> imprimirRelatorioUsuario()  {
	
		byte[] relatorio = jasperReportsService.imprimeRelatorios("usuario");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	  
	}
		
	
}



/*
 * String direcao = dir.orElse("asc"); String atributo = sort.orElse("id");
 * Integer pag = page.orElse(INITIAL_PAGE); Integer limite =
 * size.orElse(INITIAL_PAGE_SIZE);
 */		




