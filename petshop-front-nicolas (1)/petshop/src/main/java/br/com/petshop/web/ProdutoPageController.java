package br.com.petshop.web;

import br.com.petshop.entity.Produto;
import br.com.petshop.repository.ProdutoRepository;
import br.com.petshop.web.form.ProdutoForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoPageController {

    private final ProdutoRepository produtos;

    public ProdutoPageController(ProdutoRepository produtos) {
        this.produtos = produtos;
    }

    @ModelAttribute("menu")
    public String menu() { return "produtos"; }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String nome, Model model) {
        model.addAttribute("produtos", nome.isBlank()
                ? produtos.findAllByOrderByNomeAsc()
                : produtos.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim()));
        model.addAttribute("nome", nome);
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new ProdutoForm());
        return "produtos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return produtos.findById(id).map(p -> {
            model.addAttribute("produto", ProdutoForm.de(p));
            return "produtos/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("erro", "Produto não encontrado.");
            return "redirect:/produtos";
        });
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") ProdutoForm form, BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "produtos/form";
        }
        if (form.getId() == null) {
            produtos.save(new Produto(form.getNome().trim(), form.getPreco(), form.getEstoque()));
            ra.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
        } else {
            Produto p = produtos.findById(form.getId()).orElseThrow();
            p.atualizar(form.getNome().trim(), form.getPreco(), form.getEstoque());
            produtos.save(p);
            ra.addFlashAttribute("sucesso", "Produto atualizado com sucesso!");
        }
        return "redirect:/produtos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        produtos.deleteById(id);
        ra.addFlashAttribute("sucesso", "Produto excluído.");
        return "redirect:/produtos";
    }
}
