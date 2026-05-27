function openModal(id) {
    document.getElementById(id).classList.add('active');
}
function closeModal(id) {
    document.getElementById(id).classList.remove('active');
}
// Fecha o modal se o usuário clicar na área escura fora dele
window.onclick = function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.classList.remove('active');
    }
}


function ValidaCPF(inputId){
    var ao_cpf=document.getElementById(inputId).value;
    var cpfValido = /^(([0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}))$/;
    if (cpfValido.test(ao_cpf) == false)    {

        ao_cpf = ao_cpf.replace( /\D/g , ""); //Remove tudo o que não é dígito

        if (ao_cpf.length==11){
            ao_cpf = ao_cpf.replace( /(\d{3})(\d)/ , "$1.$2"); //Coloca um ponto entre o terceiro e o quarto dígitos
            ao_cpf = ao_cpf.replace( /(\d{3})(\d)/ , "$1.$2"); //Coloca um ponto entre o terceiro e o quarto dígitos
            //de novo (para o segundo bloco de números)
            ao_cpf = ao_cpf.replace( /(\d{3})(\d{1,2})$/ , "$1-$2"); //Coloca um hífen entre o terceiro e o quarto dígitos

            var valorValido = document.getElementById("ao_cpf").value = ao_cpf;
        } else {
            alert("CPF invalido");
        }
    }
}