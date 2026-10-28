/* Comportamentos gerais das telas */
(function () {
    'use strict';

    // Modal de exclusão: o mesmo modal serve pra todos os botões "Excluir".
    // O botão leva a URL e o nome em data-url / data-nome.
    const modalEl = document.getElementById('modalExcluir');
    if (modalEl && window.bootstrap) {
        const modal = new bootstrap.Modal(modalEl);
        document.querySelectorAll('.btn-excluir').forEach(function (btn) {
            btn.addEventListener('click', function () {
                document.getElementById('formExcluir').action = btn.dataset.url;
                document.getElementById('nomeExcluir').textContent = btn.dataset.nome || 'este registro';
                modal.show();
            });
        });
    }

    // Máscara simples de CPF: 000.000.000-00
    document.querySelectorAll('.mask-cpf').forEach(function (campo) {
        campo.addEventListener('input', function () {
            let v = campo.value.replace(/\D/g, '').slice(0, 11);
            v = v.replace(/(\d{3})(\d)/, '$1.$2')
                 .replace(/(\d{3})(\d)/, '$1.$2')
                 .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            campo.value = v;
        });
    });

    // Máscara de telefone: (31) 99999-9999 ou (31) 3333-3333
    document.querySelectorAll('.mask-telefone').forEach(function (campo) {
        campo.addEventListener('input', function () {
            let v = campo.value.replace(/\D/g, '').slice(0, 11);
            if (v.length > 10) {
                v = v.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
            } else if (v.length > 6) {
                v = v.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
            } else if (v.length > 2) {
                v = v.replace(/(\d{2})(\d{0,5})/, '($1) $2');
            }
            campo.value = v;
        });
    });

    // Validação no navegador antes de enviar (o servidor valida de novo)
    document.querySelectorAll('form[novalidate]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Alertas de sucesso somem sozinhos depois de 4s
    document.querySelectorAll('.alert-success').forEach(function (alerta) {
        setTimeout(function () {
            if (window.bootstrap) bootstrap.Alert.getOrCreateInstance(alerta).close();
        }, 4000);
    });
})();
