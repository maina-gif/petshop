package br.com.petshop.web;

import br.com.petshop.entity.Pet;
import br.com.petshop.entity.Tutor;
import br.com.petshop.repository.AgendamentoRepository;
import br.com.petshop.repository.PetRepository;
import br.com.petshop.repository.TutorRepository;
import br.com.petshop.web.form.PetForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pets")
public class PetPageController {

    private final PetRepository pets;
    private final TutorRepository tutores;
    private final AgendamentoRepository agendamentos;

    public PetPageController(PetRepository pets, TutorRepository tutores, AgendamentoRepository agendamentos) {
        this.pets = pets;
        this.tutores = tutores;
        this.agendamentos = agendamentos;
    }

    @ModelAttribute("menu")
    public String menu() { return "pets"; }

    // lista de tutores pro <select> do formulário
    private List<Tutor> listaTutores() {
        return tutores.findAllByOrderByNomeAsc();
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String nome, Model model) {
        model.addAttribute("pets", nome.isBlank()
                ? pets.findAllByOrderByNomeAsc()
                : pets.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim()));
        model.addAttribute("nome", nome);
        return "pets/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pet", new PetForm());
        model.addAttribute("tutores", listaTutores());
        return "pets/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return pets.findComTutorById(id).map(p -> {
            model.addAttribute("pet", PetForm.de(p));
            model.addAttribute("tutores", listaTutores());
            return "pets/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Pet não encontrado.");
            return "redirect:/pets";
        });
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("pet") PetForm form, BindingResult result,
                         Model model, RedirectAttributes ra) {
        Tutor tutor = form.getTutorId() == null ? null : tutores.findById(form.getTutorId()).orElse(null);
        if (form.getTutorId() != null && tutor == null) {
            result.rejectValue("tutorId", "invalido", "Tutor inválido");
        }
        if (result.hasErrors()) {
            model.addAttribute("tutores", listaTutores());
            return "pets/form";
        }
        if (form.getId() == null) {
            pets.save(new Pet(form.getNome().trim(), form.getEspecie(), form.getRaca(), form.getIdade(), tutor));
            ra.addFlashAttribute("sucesso", "Pet cadastrado com sucesso!");
        } else {
            Pet p = pets.findById(form.getId()).orElseThrow();
            p.atualizar(form.getNome().trim(), form.getEspecie(), form.getRaca(), form.getIdade(), tutor);
            pets.save(p);
            ra.addFlashAttribute("sucesso", "Pet atualizado com sucesso!");
        }
        return "redirect:/pets";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        if (agendamentos.existsByPetId(id)) {
            ra.addFlashAttribute("erro", "Não é possível excluir: este pet tem agendamentos.");
        } else {
            pets.deleteById(id);
            ra.addFlashAttribute("sucesso", "Pet excluído.");
        }
        return "redirect:/pets";
    }
}
