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

class RegisterStudentsActivity : AppCompatActivity() {

    lateinit var editTextNombre: EditText
    lateinit var editTextEdad: EditText
    lateinit var editTextDireccion: EditText
    lateinit var editTextTelefono: EditText
    lateinit var btnRegistrar: Button
    private val databaseStudents = FirebaseDatabase.getInstance().getReference("students")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_students)

        //para la toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Registrar Estudiante"
            setDisplayHomeAsUpEnabled(true)
        }

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextDireccion = findViewById(R.id.editTextDireccion)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            registrarEstudiante()
        }
    }

    private fun registrarEstudiante(){
        val nombre = editTextNombre.text.toString().trim()
        val edadStr = editTextEdad.text.toString().trim()
        val direccion = editTextDireccion.text.toString().trim()
        val telefono = editTextTelefono.text.toString().trim()

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

        val id = databaseStudents.push().key
        val student = Estudiante(nombre, edad, direccion, telefono)

        if (id != null) {
            databaseStudents.child(id).setValue(student)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estudiante registrado exitosamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al registrar estudiante", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun limpiarCampos() {
        editTextNombre.text.clear()
        editTextEdad.text.clear()
        editTextDireccion.text.clear()
        editTextTelefono.text.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
