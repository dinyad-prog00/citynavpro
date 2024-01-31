package com.dinyad.citynav.loginRepository

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinyad.citynav.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Récupérer les références des vues
        editTextEmail = findViewById(R.id.editTextEmail1)
        editTextPassword = findViewById(R.id.editTextPassword1)
        btnRegister = findViewById(R.id.btnRegister)
        // Récupérer la référence du lien texte pour la connexion
        tvLoginLink = findViewById(R.id.tvLoginLink)

        // Ajouter un écouteur de clic pour le lien texte
        tvLoginLink.setOnClickListener {
            // Rediriger vers l'activité de connexion
            val intent = Intent(this, LoginEmail::class.java)
            startActivity(intent)
        }

        // Ajouter un écouteur de clic pour le bouton d'enregistrement
        btnRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Vérifier que les champs ne sont pas vides
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enregistrer l'utilisateur avec email/mot de passe
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Enregistrement réussi
                        Toast.makeText(baseContext, "Enregistrement réussi.", Toast.LENGTH_SHORT).show()
                        // Vous pouvez rediriger l'utilisateur vers une autre activité si nécessaire
                    } else {
                        // Enregistrement échoué
                        Toast.makeText(baseContext, "Enregistrement échoué. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        // Méthode appelée lors du clic sur le lien texte
        fun goToLoginActivity(view: View) {
            // Rediriger vers l'activité de connexion
            val intent = Intent(this, LoginEmail::class.java)
            startActivity(intent)
        }

    }
}
