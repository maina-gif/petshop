package br.com.petshop.web;

import br.com.petshop.entity.Tutor;
import br.com.petshop.repository.PetRepository;
import br.com.petshop.repository.TutorRepository;
import br.com.petshop.web.form.TutorForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutores")
public class TutorPageController {

    private final TutorRepository tutores;
    private final PetRepository pets;

    public TutorPageController(TutorRepository tutores, PetRepository pets) {
        this.tutores = tutores;
        this.pets = pets;
    }

    @ModelAttribute("menu")
    public String menu() { return "tutores"; }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String nome, Model model) {
        model.addAttribute("tutores", nome.isBlank()
                ? tutores.findAllByOrderByNomeAsc()
                : tutores.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim()));
        model.addAttribute("nome", nome);
        return "tutores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("tutor", new TutorForm());
        return "tutores/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return tutores.findById(id).map(t -> {
            model.addAttribute("tutor", TutorForm.de(t));
            return "tutores/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Tutor não encontrado.");
            return "redirect:/tutores";
        });
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("tutor") TutorForm form, BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "tutores/form";
        }
        if (form.getId() == null) {
            tutores.save(new Tutor(form.getNome().trim(), form.getTelefone(), form.getEmail().trim(),
                    form.cpfSomenteNumeros(), form.getEndereco()));
            ra.addFlashAttribute("sucesso", "Tutor cadastrado com sucesso!");
        } else {
            Tutor t = tutores.findById(form.getId()).orElseThrow();
            t.atualizar(form.getNome().trim(), form.getTelefone(), form.getEmail().trim(),
                    form.cpfSomenteNumeros(), form.getEndereco());
            tutores.save(t);
            ra.addFlashAttribute("sucesso", "Tutor atualizado com sucesso!");
        }
        return "redirect:/tutores";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        if (pets.existsByTutorId(id)) {
            ra.addFlashAttribute("erro", "Não é possível excluir: este tutor ainda tem pets cadastrados.");
        } else {
            tutores.deleteById(id);
            ra.addFlashAttribute("sucesso", "Tutor excluído.");
        }
        return "redirect:/tutores";
    }
}
