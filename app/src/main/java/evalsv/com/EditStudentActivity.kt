package evalsv.com

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.FirebaseDatabase
import evalsv.com.models.Estudiante

class EditStudentActivity : AppCompatActivity() {

    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextDireccion: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnCancelar: Button

    private val databaseStudents = FirebaseDatabase.getInstance().getReference("students")
    private var studentId: String? = null
    private var currentStudent: Estudiante? = null

    companion object {
        const val EXTRA_STUDENT_ID = "student_id"
        const val EXTRA_STUDENT_NAME = "student_name"
        const val EXTRA_STUDENT_AGE = "student_age"
        const val EXTRA_STUDENT_ADDRESS = "student_address"
        const val EXTRA_STUDENT_PHONE = "student_phone"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        // Configurar toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Editar Estudiante"
            setDisplayHomeAsUpEnabled(true)
        }

        initializeViews()
        loadStudentData()
        setupUpdateButton()
    }

    private fun initializeViews() {
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextDireccion = findViewById(R.id.editTextDireccion)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun loadStudentData() {
        // Obtener datos del intent
        studentId = intent.getStringExtra(EXTRA_STUDENT_ID)
        val studentName = intent.getStringExtra(EXTRA_STUDENT_NAME)
        val studentAge = intent.getIntExtra(EXTRA_STUDENT_AGE, 0)
        val studentAddress = intent.getStringExtra(EXTRA_STUDENT_ADDRESS)
        val studentPhone = intent.getStringExtra(EXTRA_STUDENT_PHONE)

        // Llenar los campos con los datos actuales
        editTextNombre.setText(studentName ?: "")
        editTextEdad.setText(if (studentAge > 0) studentAge.toString() else "")
        editTextDireccion.setText(studentAddress ?: "")
        editTextTelefono.setText(studentPhone ?: "")

        // Crear objeto estudiante actual para referencia
        currentStudent = Estudiante(
            id = studentId,
            nombreCompleto = studentName,
            edad = studentAge,
            direccion = studentAddress,
            celular = studentPhone
        )
    }

    private fun setupUpdateButton() {
        btnActualizar.setOnClickListener {
            actualizarEstudiante()
        }
    }

    private fun actualizarEstudiante() {
        val nombre = editTextNombre.text.toString().trim()
        val edadStr = editTextEdad.text.toString().trim()
        val direccion = editTextDireccion.text.toString().trim()
        val telefono = editTextTelefono.text.toString().trim()

        // Validaciones
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(edadStr) ||
            TextUtils.isEmpty(direccion) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = try {
            edadStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Ingresa una edad válida", Toast.LENGTH_SHORT).show()
            return
        }

        if (studentId == null) {
            Toast.makeText(this, "Error: ID del estudiante no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear estudiante actualizado
        val updatedStudent = Estudiante(
            id = studentId,
            nombreCompleto = nombre,
            edad = edad,
            direccion = direccion,
            celular = telefono
        )

        // Actualizar en Firebase
        databaseStudents.child(studentId!!).setValue(updatedStudent)
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante actualizado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al actualizar estudiante: ${error.message}", Toast.LENGTH_SHORT).show()
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