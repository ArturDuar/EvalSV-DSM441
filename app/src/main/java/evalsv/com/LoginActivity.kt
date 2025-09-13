package evalsv.com

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnIniciarSesion: Button

    private lateinit var auth: FirebaseAuth

    lateinit var tvRegistrarse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegistrarse = findViewById(R.id.tvRegister)

        tvRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.editTextEmail)
        etPassword = findViewById(R.id.editTextPassword)
        btnIniciarSesion = findViewById(R.id.btnLogin)

        btnIniciarSesion.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser(){
        val email = etEmail.text.toString()
        val pass = etPassword.text.toString()
        auth.signInWithEmailAndPassword(email,
            pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Inicio de sesión exitoso",
                    Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "Inicio de sesíón fallido",
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}