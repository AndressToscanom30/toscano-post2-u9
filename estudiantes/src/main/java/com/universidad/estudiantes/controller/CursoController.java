package com.universidad.estudiantes.controller;

import com.universidad.estudiantes.model.Curso;
import com.universidad.estudiantes.service.CursoService;
import com.universidad.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final EstudianteService estudianteService;

    public CursoController(CursoService cursoService,
                           EstudianteService estudianteService) {
        this.cursoService = cursoService;
        this.estudianteService = estudianteService;
    }

    // GET /cursos — lista todos los cursos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos/lista";
    }

    // GET /cursos/nuevo — muestra formulario de creación
    @GetMapping("/nuevo")
    public String mostrarNuevo(Model model) {
        model.addAttribute("curso", new Curso());
        return "cursos/formulario";
    }

    // POST /cursos/guardar — guarda el curso
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("curso") Curso curso,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "cursos/formulario";
        }
        cursoService.guardar(curso);
        return "redirect:/cursos";
    }

    // GET /cursos/{id}/inscribir — muestra formulario de inscripción
    @GetMapping("/{id}/inscribir")
    public String mostrarInscripcion(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        model.addAttribute("estudiantes", estudianteService.listarTodos());
        return "cursos/inscribir";
    }

    // POST /cursos/{cursoId}/inscribir/{estudianteId} — inscribe un estudiante
    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public String inscribir(@PathVariable Long cursoId,
                            @PathVariable Long estudianteId) {
        cursoService.inscribirEstudiante(cursoId, estudianteId);
        return "redirect:/cursos";
    }

    // POST /cursos/{cursoId}/desinscribir/{estudianteId} — desinscribe
    @PostMapping("/{cursoId}/desinscribir/{estudianteId}")
    public String desinscribir(@PathVariable Long cursoId,
                               @PathVariable Long estudianteId) {
        cursoService.desinscribirEstudiante(cursoId, estudianteId);
        return "redirect:/cursos";
    }
}