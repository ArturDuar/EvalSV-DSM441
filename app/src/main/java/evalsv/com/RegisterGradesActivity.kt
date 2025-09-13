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

    private lateinit var dbRefStudents: DatabaseReference
    private lateinit var dbRefGrades: DatabaseReference
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

        initializeViews()
        setupFirebase()
        setupStaticSpinners()
        loadStudents()
        setupRegisterButton()
    }

    private fun initializeViews() {
        spinnerEstudiante = findViewById(R.id.spinnerEstudiante)
        spinnerGrado = findViewById(R.id.spinnerGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        editTextNotaFinal = findViewById(R.id.editTextNotaFinal)
        btnRegistrarNota = findViewById(R.id.btnRegistrarNota)
    }

    private fun setupFirebase() {
        dbRefStudents = FirebaseDatabase.getInstance().getReference("students")
        dbRefGrades = FirebaseDatabase.getInstance().getReference("grades")
    }

    private fun setupStaticSpinners() {
        // Configurar spinner de grados
        val grados = arrayOf(
            "Seleccionar grado",
            "1° Básico", "2° Básico", "3° Básico",
            "4° Básico", "5° Básico", "6° Básico",
            "7° Básico", "8° Básico", "9° Básico",
            "1° Bachillerato", "2° Bachillerato"
        )

        val adapterGrados = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapterGrados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrado.adapter = adapterGrados

        // Configurar spinner de materias
        val materias = arrayOf(
            "Seleccionar materia",
            "Matemáticas", "Lenguaje", "Ciencias Naturales",
            "Estudios Sociales", "Inglés", "Educación Física",
            "Educación Artística", "Moral y Cívica", "Informática"
        )

        val adapterMaterias = ArrayAdapter(this, android.R.layout.simple_spinner_item, materias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateria.adapter = adapterMaterias
    }

    private fun loadStudents() {
        nombresEstudiantes.clear()
        nombresEstudiantes.add("Seleccionar estudiante")

        dbRefStudents.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEstudiantes.clear()
                nombresEstudiantes.clear()
                nombresEstudiantes.add("Seleccionar estudiante")

                for (estudianteSnapshot in snapshot.children) {
                    val estudiante = estudianteSnapshot.getValue(Estudiante::class.java)
                    estudiante?.let {
                        listaEstudiantes.add(it)
                        nombresEstudiantes.add("${it.nombreCompleto}")
                    }
                }

                // Actualizar adapter del spinner
                val adapterEstudiantes = ArrayAdapter(
                    this@RegisterGradesActivity,
                    android.R.layout.simple_spinner_item,
                    nombresEstudiantes
                )
                adapterEstudiantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstudiante.adapter = adapterEstudiantes
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterGradesActivity,
                    "Error al cargar estudiantes: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRegisterButton() {
        btnRegistrarNota.setOnClickListener {
            registrarNota()
        }
    }

    private fun registrarNota() {
        val selectedStudentPosition = spinnerEstudiante.selectedItemPosition
        val selectedGrado = spinnerGrado.selectedItem.toString()
        val selectedMateria = spinnerMateria.selectedItem.toString()
        val notaText = editTextNotaFinal.text.toString().trim()

        // Validaciones
        if (selectedStudentPosition <= 0) {
            Toast.makeText(this, "Por favor selecciona un estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedGrado == "Seleccionar grado") {
            Toast.makeText(this, "Por favor selecciona un grado", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedMateria == "Seleccionar materia") {
            Toast.makeText(this, "Por favor selecciona una materia", Toast.LENGTH_SHORT).show()
            return
        }

        if (notaText.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa la nota final", Toast.LENGTH_SHORT).show()
            return
        }

        val notaFinal = notaText.toDoubleOrNull()
        if (notaFinal == null || notaFinal < 0 || notaFinal > 10) {
            Toast.makeText(this, "La nota debe ser un número entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el estudiante seleccionado
        val estudianteSeleccionado = listaEstudiantes[selectedStudentPosition - 1]

        // Crear objeto Nota
        val nota = Nota().apply {
            estudianteId = estudianteSeleccionado.id
            nombreEstudiante = "${estudianteSeleccionado.nombreCompleto}"
            grado = selectedGrado
            materia = selectedMateria

        }

        // Generar ID único para la nota
        val notaId = dbRefGrades.push().key ?: return
        nota.id = notaId

        // Guardar en Firebase
        dbRefGrades.child(notaId).setValue(nota)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota registrada exitosamente", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al registrar la nota: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        spinnerEstudiante.setSelection(0)
        spinnerGrado.setSelection(0)
        spinnerMateria.setSelection(0)
        editTextNotaFinal.text.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}