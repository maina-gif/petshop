package br.com.petshop.web.form;

import br.com.petshop.entity.Pet;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PetForm {

    private Long id;

    @NotBlank(message = "Informe o nome do pet")
    private String nome;

    @NotBlank(message = "Informe a espécie")
    private String especie;

    private String raca;

    @Min(value = 0, message = "Idade não pode ser negativa")
    @Max(value = 40, message = "Idade inválida")
    private Integer idade;

    @NotNull(message = "Selecione o tutor")
    private Long tutorId;

    public static PetForm de(Pet p) {
        PetForm f = new PetForm();
        f.id = p.getId();
        f.nome = p.getNome();
        f.especie = p.getEspecie();
        f.raca = p.getRaca();
        f.idade = p.getIdade();
        f.tutorId = p.getTutor().getId();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
}
