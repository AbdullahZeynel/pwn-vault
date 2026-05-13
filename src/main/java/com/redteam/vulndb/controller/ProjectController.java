package com.redteam.vulndb.controller;

import com.redteam.vulndb.entity.Project;
import com.redteam.vulndb.entity.ProjectStatus;
import com.redteam.vulndb.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing pentest projects.
 */
@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("newProject", new Project());
        model.addAttribute("statuses", ProjectStatus.values());
        return "projects";
    }

    @PostMapping("/projects/save")
    public String saveProject(@Valid @ModelAttribute("newProject") Project project,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Proje adı boş bırakılamaz.");
            return "redirect:/projects";
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("successMessage",
                "Proje oluşturuldu: " + project.getName());
        return "redirect:/projects";
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Proje silindi.");
        return "redirect:/projects";
    }
}
