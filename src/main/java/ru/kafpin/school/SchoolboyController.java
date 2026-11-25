package ru.kafpin.school;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
class SchoolboyController {
    private final SchoolboyService schoolboys;

    SchoolboyController(SchoolboyService schoolboys) {
        this.schoolboys = schoolboys;
    }

    @GetMapping("/")
    String home() {
        return "redirect:/schoolboys";
    }

    @GetMapping("/schoolboys")
    String list(@Valid @ModelAttribute("filter") SchoolboyFilter filter, BindingResult errors, Model model) {
        if (!errors.hasErrors()) {
            schoolboys.selectionError(filter.schoolId(), filter.classId())
                    .ifPresent(message -> errors.reject("schoolClass", message));
        }
        model.addAttribute("schoolboys", errors.hasErrors() ? List.of() : schoolboys.search(filter));
        addCatalog(model);
        return "list";
    }

    @GetMapping("/schoolboys/{id}")
    String details(@PathVariable long id, Model model) {
        model.addAttribute("schoolboy", schoolboys.find(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Школьник не найден.")));
        return "details";
    }

    @GetMapping("/schoolboys/new")
    String form(Model model) {
        model.addAttribute("schoolboy", SchoolboyData.empty());
        addCatalog(model);
        return "form";
    }

    @PostMapping("/schoolboys")
    String create(@Valid @ModelAttribute("schoolboy") SchoolboyData schoolboy, BindingResult errors, Model model) {
        if (!errors.hasErrors()) {
            schoolboys.selectionError(schoolboy.schoolId(), schoolboy.classId())
                    .ifPresent(message -> errors.reject("schoolClass", message));
        }
        if (errors.hasErrors()) {
            addCatalog(model);
            return "form";
        }
        return "redirect:/schoolboys/" + schoolboys.create(schoolboy).id();
    }

    private void addCatalog(Model model) {
        model.addAttribute("schools", schoolboys.schools());
        model.addAttribute("classes", schoolboys.classes());
    }
}
