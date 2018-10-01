package br.com.starwars.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.starwars.models.PlanetaVO;
import br.com.starwars.repositorys.PlanetaRepository;

@Controller
public class PlanetaController {
	
	@Autowired
	
	private PlanetaRepository repository;
	
	@GetMapping("/planeta/cadastrar")
	public String cadastrar(Model model)
	{
		model.addAttribute("planeta", new PlanetaVO());
		return "planeta/cadastrar";
	}
	
	@PostMapping("/planeta/salvar")
	public String salvar(@ModelAttribute PlanetaVO planeta){
		repository.salvar(planeta);
		//System.out.println(planeta);
		return "redirect:/";
	}
	@GetMapping("planeta/listar")
	public String Listar(Model model){
		List<PlanetaVO> planetas = repository.obterTodosPlanetas();
		
		model.addAttribute("planetas",planetas);
		return "planeta/listar";
	}
	
	@GetMapping("/planeta/visualizar/{id}")
	public String visualizar(@PathVariable String id, Model model) {
	  
		PlanetaVO planeta = repository.obterPlanetaPor(id);
	  
		model.addAttribute("planeta", planeta);

	  return "planeta/visualizar";
	}
	
	@GetMapping("/planeta/excluir/{id}")
	public String excluir(@PathVariable String id ,Model model){
		repository.deletaPor(id);
		return "redirect:/planeta/listar";
	}
	
	@GetMapping("/planeta/pesquisarnome")
	public String pesquisarNome(){
		return "planeta/pesquisarnome";
	}
	
	@GetMapping("/planeta/pesquisar")
	public String pesquisar(@RequestParam("nome") String nome, Model model){
		
		List<PlanetaVO> planetas = repository.pesquisarPor(nome);
		model.addAttribute("planetas", planetas);
		return "planeta/pesquisarnome";
	}
}
