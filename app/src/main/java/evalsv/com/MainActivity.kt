package evalsv.com

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var btnGestionarEstudiantes: Button
    private lateinit var btnGestionarNotas: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 2. Habilitar la flecha de retroceso (up button)
        supportActionBar?.apply {
            title = "Menu Principal"
        }


        btnLogout = findViewById(R.id.btnLogout)
        btnGestionarEstudiantes = findViewById(R.id.btnGestionarEstudiantes)
        btnGestionarNotas = findViewById(R.id.btnGestionarNotas)

        auth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnGestionarEstudiantes.setOnClickListener {
            val intent = Intent(this, ListStudentsActivity::class.java)
            startActivity(intent)
        }
        btnGestionarNotas.setOnClickListener {
            val intent = Intent(this, ListGradesActivity::class.java)
            startActivity(intent)
        }


    }

}