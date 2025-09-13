package evalsv.com

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var btnRegistrarEstudiante: Button
    private lateinit var btnListarEstudiantes: Button
    private lateinit var btnRegistrarNotas: Button
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
        btnRegistrarEstudiante = findViewById(R.id.btn1)
        btnListarEstudiantes = findViewById(R.id.btnListarEstudiantes)
        btnRegistrarNotas = findViewById(R.id.btnRegistrarNotas)

        auth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegistrarEstudiante.setOnClickListener {
            val intent = Intent(this, RegisterStudentsActivity::class.java)
            startActivity(intent)
        }

        btnListarEstudiantes.setOnClickListener {
            val intent = Intent(this, ListStudentsActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarNotas.setOnClickListener {
            val intent = Intent(this, RegisterGradesActivity::class.java)
            startActivity(intent)
        }


    }

}