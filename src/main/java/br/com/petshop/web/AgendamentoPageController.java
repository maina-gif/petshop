package br.com.petshop.web;

import br.com.petshop.entity.Agendamento;
import br.com.petshop.repository.AgendamentoRepository;
import br.com.petshop.repository.PetRepository;
import br.com.petshop.service.CrudServices;
import br.com.petshop.web.form.AgendamentoForm;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoPageController {

    private final AgendamentoRepository agendamentos;
    private final PetRepository pets;
    private final CrudServices service;

    public AgendamentoPageController(AgendamentoRepository agendamentos, PetRepository pets, CrudServices service) {
        this.agendamentos = agendamentos;
        this.pets = pets;
        this.service = service;
    }

    @ModelAttribute("menu")
    public String menu() { return "agendamentos"; }

    // opções dos <select> do formulário
    private void opcoes(Model model) {
        model.addAttribute("pets", pets.findAllByOrderByNomeAsc());
        model.addAttribute("servicos", Agendamento.Servico.values());
        model.addAttribute("statusList", Agendamento.Status.values());
    }

    @GetMapping
    public String listar(@RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Model model) {
        model.addAttribute("agendamentos", data == null
                ? agendamentos.findAllByOrderByDataAscHorarioAsc()
                : agendamentos.findByDataOrderByHorarioAsc(data));
        model.addAttribute("data", data);
        return "agendamentos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("agendamento", new AgendamentoForm());
        opcoes(model);
        return "agendamentos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return agendamentos.findComPetById(id).map(a -> {
            model.addAttribute("agendamento", AgendamentoForm.de(a));
            opcoes(model);
            return "agendamentos/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Agendamento não encontrado.");
            return "redirect:/agendamentos";
        });
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("agendamento") AgendamentoForm form, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            try {
                // regras (data no passado, conflito de horário) ficam no service do back
                service.salvarAgendamento(form.getData(), form.getHorario(), form.getServico(),
                        form.getStatus(), form.getPetId(), form.getId());
                ra.addFlashAttribute("sucesso", form.getId() == null
                        ? "Agendamento criado com sucesso!" : "Agendamento atualizado com sucesso!");
                return "redirect:/agendamentos";
            } catch (IllegalArgumentException | java.util.NoSuchElementException e) {
                result.reject("regra", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("conflito", "Já existe um agendamento registrado nesse dia e horário.");
            }
        }
        opcoes(model);
        return "agendamentos/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        agendamentos.deleteById(id);
        ra.addFlashAttribute("sucesso", "Agendamento excluído.");
        return "redirect:/agendamentos";
    }
}
