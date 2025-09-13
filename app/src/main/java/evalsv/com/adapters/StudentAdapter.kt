package evalsv.com.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import evalsv.com.R
import evalsv.com.models.Estudiante

class StudentAdapter(private val studentList: List<Estudiante>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccion)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvNombre.text = "Nombre: ${student.nombreCompleto}"
        holder.tvEdad.text = "Edad: ${student.edad}"
        holder.tvDireccion.text = "Dirección: ${student.direccion}"
        holder.tvTelefono.text = "Teléfono: ${student.celular}"
    }

    override fun getItemCount(): Int = studentList.size
}