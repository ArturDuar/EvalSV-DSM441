package evalsv.com.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import evalsv.com.R
import evalsv.com.models.Estudiante

class StudentAdapter(private val studentList: MutableList<Estudiante>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccion)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]

        // Verificar valores nulos y mostrar texto por defecto
        holder.tvNombre.text = "Nombre: ${student.nombreCompleto ?: "No disponible"}"
        holder.tvEdad.text = "Edad: ${student.edad?.toString() ?: "No disponible"}"
        holder.tvDireccion.text = "Dirección: ${student.direccion ?: "No disponible"}"
        holder.tvTelefono.text = "Teléfono: ${student.celular ?: "No disponible"}"

        holder.btnEliminar.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Eliminar Estudiante")
            builder.setMessage("¿Estás seguro de que deseas eliminar este estudiante?")
            builder.setPositiveButton("Sí") { dialog: DialogInterface, _: Int ->
                removeStudent(holder, student, position)
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            builder.show() // ¡Importante! Faltaba esta línea
        }
    }

    private fun removeStudent(holder: StudentViewHolder, student: Estudiante, position: Int) {
        val id = student.id
        if (!id.isNullOrEmpty()) {
            val dbRef = FirebaseDatabase.getInstance().getReference("students")
            dbRef.child(id).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                    // Verificar que la posición sigue siendo válida
                    if (position < studentList.size) {
                        studentList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, studentList.size)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(holder.itemView.context, "Error: ID del estudiante no válido", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = studentList.size
}