package br.com.petshop.web;

import br.com.petshop.repository.AgendamentoRepository;
import br.com.petshop.repository.PetRepository;
import br.com.petshop.repository.ProdutoRepository;
import br.com.petshop.repository.TutorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class PainelController {

    private final TutorRepository tutores;
    private final PetRepository pets;
    private final ProdutoRepository produtos;
    private final AgendamentoRepository agendamentos;

    public PainelController(TutorRepository tutores, PetRepository pets,
                            ProdutoRepository produtos, AgendamentoRepository agendamentos) {
        this.tutores = tutores;
        this.pets = pets;
        this.produtos = produtos;
        this.agendamentos = agendamentos;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate hoje = LocalDate.now();
        model.addAttribute("hoje", hoje);
        model.addAttribute("totalTutores", tutores.count());
        model.addAttribute("totalPets", pets.count());
        model.addAttribute("totalProdutos", produtos.count());
        model.addAttribute("agendamentosHoje", agendamentos.findByDataOrderByHorarioAsc(hoje));
        model.addAttribute("menu", "dashboard");
        return "dashboard";
    }
}
