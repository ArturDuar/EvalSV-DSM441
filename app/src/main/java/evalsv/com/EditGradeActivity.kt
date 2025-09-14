package evalsv.com

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import evalsv.com.models.Estudiante
import evalsv.com.models.Nota

class EditGradeActivity : AppCompatActivity() {

    private lateinit var spinnerEstudiante: Spinner
    private lateinit var spinnerGrado: Spinner
    private lateinit var spinnerMateria: Spinner
    private lateinit var editTextNotaFinal: EditText
    private lateinit var btnActualizarNota: Button
    private lateinit var btnCancelar: Button

    private lateinit var dbRefStudents: DatabaseReference
    private lateinit var dbRefGrades: DatabaseReference
    private val listaEstudiantes = mutableListOf<Estudiante>()
    private val nombresEstudiantes = mutableListOf<String>()

    private var gradeId: String? = null
    private var currentGrade: Nota? = null

    companion object {
        const val EXTRA_GRADE_ID = "grade_id"
        const val EXTRA_STUDENT_ID = "student_id"
        const val EXTRA_STUDENT_NAME = "student_name"
        const val EXTRA_GRADE_LEVEL = "grade_level"
        const val EXTRA_SUBJECT = "subject"
        const val EXTRA_FINAL_GRADE = "final_grade"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_grade)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Editar Nota"
            setDisplayHomeAsUpEnabled(true)
        }

        initializeViews()
        setupFirebase()
        setupStaticSpinners()
        loadStudents()
        loadGradeData()
        setupUpdateButton()
    }

    private fun initializeViews() {
        spinnerEstudiante = findViewById(R.id.spinnerEstudiante)
        spinnerGrado = findViewById(R.id.spinnerGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        editTextNotaFinal = findViewById(R.id.editTextNotaFinal)
        btnActualizarNota = findViewById(R.id.btnActualizarNota)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnCancelar.setOnClickListener {
            finish()
        }
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
                        it.id = estudianteSnapshot.key
                        listaEstudiantes.add(it)
                        nombresEstudiantes.add("${it.nombreCompleto}")
                    }
                }

                // Actualizar adapter del spinner
                val adapterEstudiantes = ArrayAdapter(
                    this@EditGradeActivity,
                    android.R.layout.simple_spinner_item,
                    nombresEstudiantes
                )
                adapterEstudiantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstudiante.adapter = adapterEstudiantes

                // Cargar los datos de la nota después de cargar estudiantes
                setCurrentGradeData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditGradeActivity,
                    "Error al cargar estudiantes: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadGradeData() {
        // Obtener datos del intent
        gradeId = intent.getStringExtra(EXTRA_GRADE_ID)
        val studentId = intent.getStringExtra(EXTRA_STUDENT_ID)
        val studentName = intent.getStringExtra(EXTRA_STUDENT_NAME)
        val gradeLevel = intent.getStringExtra(EXTRA_GRADE_LEVEL)
        val subject = intent.getStringExtra(EXTRA_SUBJECT)
        val finalGrade = intent.getDoubleExtra(EXTRA_FINAL_GRADE, 0.0)

        // Crear objeto nota actual para referencia
        currentGrade = Nota(
            id = gradeId,
            estudianteId = studentId,
            nombreEstudiante = studentName,
            grado = gradeLevel,
            materia = subject,
            notaFinal = finalGrade
        )

        // Establecer nota final
        editTextNotaFinal.setText(if (finalGrade > 0.0) finalGrade.toString() else "")
    }

    private fun setCurrentGradeData() {
        currentGrade?.let { grade ->
            // Seleccionar estudiante
            val studentIndex = nombresEstudiantes.indexOfFirst { name ->
                name == grade.nombreEstudiante
            }
            if (studentIndex > 0) {
                spinnerEstudiante.setSelection(studentIndex)
            }

            // Seleccionar grado
            val gradeAdapter = spinnerGrado.adapter
            for (i in 0 until gradeAdapter.count) {
                if (gradeAdapter.getItem(i).toString() == grade.grado) {
                    spinnerGrado.setSelection(i)
                    break
                }
            }

            // Seleccionar materia
            val subjectAdapter = spinnerMateria.adapter
            for (i in 0 until subjectAdapter.count) {
                if (subjectAdapter.getItem(i).toString() == grade.materia) {
                    spinnerMateria.setSelection(i)
                    break
                }
            }
        }
    }

    private fun setupUpdateButton() {
        btnActualizarNota.setOnClickListener {
            actualizarNota()
        }
    }

    private fun actualizarNota() {
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

        if (gradeId == null) {
            Toast.makeText(this, "Error: ID de la nota no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el estudiante seleccionado
        val estudianteSeleccionado = listaEstudiantes[selectedStudentPosition - 1]

        // Crear objeto Nota actualizado
        val updatedGrade = Nota().apply {
            id = gradeId
            estudianteId = estudianteSeleccionado.id
            nombreEstudiante = "${estudianteSeleccionado.nombreCompleto}"
            grado = selectedGrado
            materia = selectedMateria
            this.notaFinal = notaFinal
        }

        // Actualizar en Firebase
        dbRefGrades.child(gradeId!!).setValue(updatedGrade)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota actualizada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al actualizar la nota: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}