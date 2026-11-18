package com.murilo.minhaloja.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.murilo.minhaloja.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Já logado? Vai direto para a lista de produtos
        if (auth.currentUser != null) {
            irParaProdutos()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "MinhaLoja"

        binding.btnEntrar.setOnClickListener { entrar() }
        binding.btnCriarConta.setOnClickListener { criarConta() }
    }

    private fun entrar() {
        val (email, senha) = validarCampos() ?: return
        mostrarCarregando(true)

        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { irParaProdutos() }
            .addOnFailureListener { erro ->
                mostrarCarregando(false)
                mostrarErro(traduzirErro(erro))
            }
    }

    private fun criarConta() {
        val (email, senha) = validarCampos() ?: return
        mostrarCarregando(true)

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { irParaProdutos() }
            .addOnFailureListener { erro ->
                mostrarCarregando(false)
                mostrarErro(traduzirErro(erro))
            }
    }

    /** Valida antes de chamar o Firebase. Retorna null se algo estiver errado. */
    private fun validarCampos(): Pair<String, String>? {
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()

        binding.tilEmail.error = null
        binding.tilSenha.error = null
        binding.tvErro.visibility = View.GONE

        var valido = true

        if (email.isEmpty()) {
            binding.tilEmail.error = "Informe o e-mail"
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "E-mail inválido"
            valido = false
        }

        if (senha.length < 6) {
            binding.tilSenha.error = "A senha precisa ter pelo menos 6 caracteres"
            valido = false
        }

        return if (valido) email to senha else null
    }

    /** Converte as exceções do Firebase em mensagens claras em português. */
    private fun traduzirErro(erro: Exception): String = when (erro) {
        // WeakPassword herda de InvalidCredentials, por isso vem antes
        is FirebaseAuthWeakPasswordException -> "Senha fraca: use pelo menos 6 caracteres."
        is FirebaseAuthUserCollisionException -> "Já existe uma conta com este e-mail."
        is FirebaseAuthInvalidUserException -> "Não existe conta com este e-mail."
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos."
        is FirebaseNetworkException -> "Sem conexão com a internet."
        is FirebaseTooManyRequestsException -> "Muitas tentativas. Aguarde um pouco e tente de novo."
        else -> erro.localizedMessage ?: "Erro desconhecido."
    }

    private fun mostrarErro(mensagem: String) {
        binding.tvErro.text = mensagem
        binding.tvErro.visibility = View.VISIBLE
    }

    private fun mostrarCarregando(carregando: Boolean) {
        binding.progressLogin.visibility = if (carregando) View.VISIBLE else View.GONE
        binding.btnEntrar.isEnabled = !carregando
        binding.btnCriarConta.isEnabled = !carregando
    }

    private fun irParaProdutos() {
        startActivity(Intent(this, ProdutosActivity::class.java))
        finish()
    }
}
