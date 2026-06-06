function openModal(id) {
    document.getElementById(id).classList.add('active');
}
function closeModal(id) {
    document.getElementById(id).classList.remove('active');
}

window.onclick = function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.classList.remove('active');
    }
}

document.addEventListener("DOMContentLoaded", function() {

    const textInputs = document.querySelectorAll('input[type="text"]');

    textInputs.forEach(input => {
        input.addEventListener('input', function() {
            const value = this.value.trim();


            if (this.hasAttribute('required') && value.length < 3) {
                this.classList.add('input-error');
                this.setCustomValidity("Este campo deve ter pelo menos 3 caracteres.");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });


    const numberInputs = document.querySelectorAll('input[type="number"]');

    numberInputs.forEach(input => {
        input.addEventListener('input', function() {
            const value = parseFloat(this.value);


            if (isNaN(value) || value <= 0) {
                this.classList.add('input-error');
                this.setCustomValidity("Por favor, insira um número válido e maior que zero.");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });


    const cpfInputs = document.querySelectorAll('.input-cpf');

    cpfInputs.forEach(input => {

        input.addEventListener('input', function() {

            this.value = this.value.replace(/\D/g, '');

            const value = this.value;

            if (value.length < 11 || value.length > 11) {
                this.classList.add('input-error');
                this.setCustomValidity("O CPF deve conter exatamente 11 dígitos.");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });


    const rgInputs = document.querySelectorAll('.input-rg');

    rgInputs.forEach(input => {
        input.addEventListener('input', function() {

            this.value = this.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();

            const value = this.value;


            if (value.length < 5 || value.length > 9) {
                this.classList.add('input-error');
                this.setCustomValidity("O RG deve ter entre 5 e 9 caracteres.");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });


    const placaInputs = document.querySelectorAll('.input-placa');

    placaInputs.forEach(input => {
        input.addEventListener('input', function() {

            this.value = this.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();

            const value = this.value;


            const placaRegex = /^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/;

            if (value.length !== 7) {
                this.classList.add('input-error');
                this.setCustomValidity("A placa deve conter exatamente 7 caracteres.");
            } else if (!placaRegex.test(value)) {
                this.classList.add('input-error');
                this.setCustomValidity("Formato inválido. Utilize o formato antigo (ABC1234) ou Mercosul (ABC1D23).");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });


    const anoInputs = document.querySelectorAll('.input-ano');
    const anoAtual = new Date().getFullYear();

    anoInputs.forEach(input => {
        input.addEventListener('input', function() {
            const value = parseInt(this.value);


            if (isNaN(value) || value < 1900 || value > (anoAtual + 2)) {
                this.classList.add('input-error');
                this.setCustomValidity(`O ano deve ser válido, entre 1900 e ${anoAtual + 1}.`);
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        });
    });

    const numeroInputs = document.querySelectorAll('.input-numero');

    numeroInputs.forEach(input => {
        input.addEventListener('input', function() {

            const value = this.value;

            if (value.length < 8 || value.length > 15) {
                this.classList.add('input-error');
                this.setCustomValidity("O número de telefone deve ter entre 8 e 15 caracteres.");
            } else {
                this.classList.remove('input-error');
                this.setCustomValidity("");
            }
        })
    })

    // 3. Prevenir a submissão de formulários com dados inválidos
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            let formIsValid = true;


            const inputs = form.querySelectorAll('input, select');
            inputs.forEach(input => {
                if (!input.checkValidity()) {
                    input.classList.add('input-error');
                    formIsValid = false;
                }
            });

            if (!formIsValid) {
                event.preventDefault();
                alert("Por favor, preencha corretamente todos os campos destacados em vermelho antes de guardar.");
            }
        });
    });
});

function ValidaCPF(inputId){
    var ao_cpf=document.getElementById(inputId).value;
    var cpfValido = /^(([0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}))$/;
    if (cpfValido.test(ao_cpf) == false)    {

        ao_cpf = ao_cpf.replace( /\D/g , "");

        if (ao_cpf.length==11){
            ao_cpf = ao_cpf.replace( /(\d{3})(\d)/ , "$1.$2");
            ao_cpf = ao_cpf.replace( /(\d{3})(\d)/ , "$1.$2");

            ao_cpf = ao_cpf.replace( /(\d{3})(\d{1,2})$/ , "$1-$2");

            var valorValido = document.getElementById("ao_cpf").value = ao_cpf;
        } else {
            alert("CPF invalido");
        }
    }
}