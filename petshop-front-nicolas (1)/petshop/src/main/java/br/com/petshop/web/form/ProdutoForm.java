package br.com.petshop.web.form;

import br.com.petshop.entity.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoForm {

    private Long id;

    @NotBlank(message = "Informe o nome do produto")
    private String nome;

    @NotNull(message = "Informe o preço")
    @DecimalMin(value = "0.0", message = "Preço não pode ser negativo")
    private BigDecimal preco;

    @NotNull(message = "Informe o estoque")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    private Integer estoque;

    public static ProdutoForm de(Produto p) {
        ProdutoForm f = new ProdutoForm();
        f.id = p.getId();
        f.nome = p.getNome();
        f.preco = p.getPreco();
        f.estoque = p.getEstoque();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
}
