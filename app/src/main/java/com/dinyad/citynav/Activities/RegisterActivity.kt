package com.dinyad.citynav.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.models.UserModel
import com.dinyad.citynav.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView
    private lateinit var userRepository: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        userRepository = UserRepository()

        // Récupérer les références des vues
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail1)
        editTextPassword = findViewById(R.id.editTextPassword1)
        btnRegister = findViewById(R.id.btnRegister)
        // Récupérer la référence du lien texte pour la connexion
        tvLoginLink = findViewById(R.id.tvLoginLink)

        // Ajouter un écouteur de clic pour le lien texte
        tvLoginLink.setOnClickListener {

            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)
        }


        btnRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enregistrer l'utilisateur avec email/mot de passe
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Enregistrement réussi
                        Toast.makeText(baseContext, "Enregistrement réussi.", Toast.LENGTH_SHORT).show()
                        userRepository.insertUser(UserModel(mAuth.currentUser!!.uid,name,email))
                        mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    mAuth.currentUser?.uid
                                    // Connexion réussie
                                    Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                                    // Rediriger vers MainActivity
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    // Connexion échouée
                                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                }
                            }

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
            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)
        }

        val cityNav : TextView = findViewById(R.id.citynav)
        val paint = cityNav?.paint
        val width = paint?.measureText(cityNav?.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width!!, cityNav?.textSize!!, intArrayOf(
            Color.parseColor("#8BD8F9"),
            Color.parseColor("#5495FF"),

            ), null, Shader.TileMode.REPEAT)

        cityNav?.paint?.setShader(textShader)

    }
}
