package com.taibe.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.taibe.mytasks.R
import com.taibe.mytasks.databinding.ActivityLoginBinding
import com.taibe.mytasks.extension.hasValue
import com.taibe.mytasks.extension.value
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        initPhoneAuthCallbacks()

        initComponents()
    }

    private fun initComponents() {
        binding.btLogin.setOnClickListener {
            if (validate()) {
                login()
            }
        }

        binding.btCreateAccount.setOnClickListener {
            if (validate()) {
                createAccount()
            }
        }

        binding.btShowPhoneLogin.setOnClickListener {
            binding.layoutPhone.visibility = View.VISIBLE
            binding.btSendCode.visibility = View.VISIBLE
            binding.layoutCode.visibility = View.VISIBLE
            binding.btVerifyCode.visibility = View.VISIBLE
        }

        binding.btSendCode.setOnClickListener {
            sendCode()
        }

        binding.btVerifyCode.setOnClickListener {
            verifyCode()
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        binding.layoutEmail.error = null
        binding.layoutPassword.error = null

        if (!binding.etEmail.hasValue()) {
            isValid = false
            binding.layoutEmail.error = ContextCompat.getString(this, R.string.empty_email)
        }

        if (!binding.etPassword.hasValue()) {
            isValid = false
            binding.layoutPassword.error = ContextCompat.getString(this, R.string.empty_password)
        }

        return isValid
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword(binding.etEmail.value(), binding.etPassword.value())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    val message =
                        task.exception?.message ?: ContextCompat.getString(this, R.string.account_created_fail)
                    Log.e("auth", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun login() {
        auth.signInWithEmailAndPassword(binding.etEmail.value(), binding.etPassword.value())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    val message =
                        task.exception?.message ?: ContextCompat.getString(this, R.string.login_fail)
                    Log.e("auth", "loginUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun initPhoneAuthCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(this@LoginActivity, "Código enviado!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendCode() {
        val phone = binding.etPhone.value()

        if (phone.isEmpty()) {
            Toast.makeText(this, "Informe o telefone", Toast.LENGTH_SHORT).show()
            return
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode() {
        val code = binding.etCode.value()

        if (code.isEmpty()) {
            Toast.makeText(this, "Informe o código recebido", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}