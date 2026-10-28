package br.com.petshop.web.form;

import br.com.petshop.entity.Agendamento;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoForm {

    private Long id;

    @NotNull(message = "Informe a data")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull(message = "Informe o horário")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horario;

    @NotNull(message = "Selecione o serviço")
    private Agendamento.Servico servico;

    @NotNull(message = "Selecione o status")
    private Agendamento.Status status = Agendamento.Status.AGENDADO;

    @NotNull(message = "Selecione o pet")
    private Long petId;

    public static AgendamentoForm de(Agendamento a) {
        AgendamentoForm f = new AgendamentoForm();
        f.id = a.getId();
        f.data = a.getData();
        f.horario = a.getHorario();
        f.servico = a.getServico();
        f.status = a.getStatus();
        f.petId = a.getPet().getId();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public Agendamento.Servico getServico() { return servico; }
    public void setServico(Agendamento.Servico servico) { this.servico = servico; }
    public Agendamento.Status getStatus() { return status; }
    public void setStatus(Agendamento.Status status) { this.status = status; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
}
