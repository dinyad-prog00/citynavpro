package com.dinyad.citynav.loginRepository
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.google.firebase.auth.FirebaseAuth

class LoginEmail : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLoginEmail: Button
    private lateinit var tvRegisterLink: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_email_activity)

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Récupérer les références des vues
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnLoginEmail = findViewById(R.id.btnLoginEmail)

        // Récupérer la référence du lien texte pour l'enregistrement
        tvRegisterLink = findViewById(R.id.tvRegisterLink)

        // Ajouter un écouteur de clic pour le lien texte
        tvRegisterLink.setOnClickListener {
            // Rediriger vers l'activité d'enregistrement
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        // Ajouter un écouteur de clic pour le bouton de connexion par email/mdp
        btnLoginEmail.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Pour la connexion
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Connexion réussie
                        Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                        // Rediriger vers MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)



                    } else {
                        // Connexion échouée
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    // Méthode appelée lors du clic sur le lien texte
    fun goToRegisterActivity(view: View) {
        // Rediriger vers l'activité d'enregistrement
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
