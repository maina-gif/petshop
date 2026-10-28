package br.com.petshop.web.form;

import br.com.petshop.entity.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Objeto que liga o formulário HTML (th:object) com o back.
// A entidade não tem setters, então o form usa esta classe intermediária.
public class TutorForm {

    private Long id;

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe o telefone")
    @Pattern(regexp = "^[0-9()\\s-]{10,15}$", message = "Telefone inválido")
    private String telefone;

    @NotBlank(message = "Informe o e-mail")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Informe o CPF")
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$", message = "CPF deve ter 11 dígitos")
    private String cpf;

    private String endereco;

    public static TutorForm de(Tutor t) {
        TutorForm f = new TutorForm();
        f.id = t.getId();
        f.nome = t.getNome();
        f.telefone = t.getTelefone();
        f.email = t.getEmail();
        f.cpf = t.getCpf();
        f.endereco = t.getEndereco();
        return f;
    }

    // guarda só os números do CPF
    public String cpfSomenteNumeros() {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
