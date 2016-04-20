package edu.utn.dlc.searchengine.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.utn.dlc.searchengine.service.Buscador;
import edu.utn.dlc.searchengine.service.BuscadorImpl;
import edu.utn.dlc.searchengine.service.Ranking;

@Controller
public class MotorController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView mostrarBuscador() throws Exception {

		ModelAndView model = new ModelAndView("home");

		return model;
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public ModelAndView obtenerReultadoBusqueda(@RequestParam("cadena") String cadena) throws Exception {

		ModelAndView model = new ModelAndView("home");
		Buscador buscador = new BuscadorImpl();
		List<Ranking> result = buscador.buscar(cadena);
		for (Ranking ranking : result) {
			System.out.println("Ranking desde MotorController: " + ranking.toString());
		}
		model.addObject("resultadoBusqueda", result);

		return model;
	}
}