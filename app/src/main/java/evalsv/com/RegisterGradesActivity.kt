package evalsv.com

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import evalsv.com.models.Estudiante
import evalsv.com.models.Nota

class RegisterGradesActivity : AppCompatActivity() {

    private lateinit var spinnerEstudiante: Spinner
    private lateinit var spinnerGrado: Spinner
    private lateinit var spinnerMateria: Spinner
    private lateinit var editTextNotaFinal: EditText
    private lateinit var btnRegistrarNota: Button

    private lateinit var dbRef: DatabaseReference
    private val listaEstudiantes = mutableListOf<Estudiante>()
    private val nombresEstudiantes = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_grades)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Registrar Notas"
            setDisplayHomeAsUpEnabled(true)
        }


        spinnerEstudiante = findViewById(R.id.spinnerEstudiante)
        spinnerGrado = findViewById(R.id.spinnerGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        editTextNotaFinal = findViewById(R.id.editTextNotaFinal)
        btnRegistrarNota = findViewById(R.id.btnRegistrarNota)

        dbRef = FirebaseDatabase.getInstance().getReference("students")

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}