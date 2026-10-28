package br.com.petshop.web;

import br.com.petshop.entity.Agendamento;
import org.springframework.stereotype.Component;

// Usado nos templates como ${@fmt.metodo(...)}
@Component("fmt")
public class Formatador {

    // Na listagem o CPF aparece mascarado: ***.***.789-00
    public String cpfMascarado(String cpf) {
        if (cpf == null) return "";
        String n = cpf.replaceAll("\\D", "");
        if (n.length() != 11) return "***";
        return "***.***." + n.substring(6, 9) + "-" + n.substring(9);
    }

    public String servico(Agendamento.Servico s) {
        if (s == null) return "";
        return switch (s) {
            case BANHO -> "Banho";
            case TOSA -> "Tosa";
            case BANHO_E_TOSA -> "Banho e tosa";
        };
    }

    public String status(Agendamento.Status s) {
        if (s == null) return "";
        return switch (s) {
            case AGENDADO -> "Agendado";
            case EM_ANDAMENTO -> "Em andamento";
            case CONCLUIDO -> "Concluído";
            case CANCELADO -> "Cancelado";
        };
    }

    // cor do badge do Bootstrap pra cada status
    public String corStatus(Agendamento.Status s) {
        if (s == null) return "secondary";
        return switch (s) {
            case AGENDADO -> "primary";
            case EM_ANDAMENTO -> "warning";
            case CONCLUIDO -> "success";
            case CANCELADO -> "secondary";
        };
    }
}
