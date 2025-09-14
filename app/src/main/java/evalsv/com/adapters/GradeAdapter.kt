package evalsv.com.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import evalsv.com.EditGradeActivity
import evalsv.com.R
import evalsv.com.models.Nota

class GradesAdapter(private val gradesList: MutableList<Nota>) :
    RecyclerView.Adapter<GradesAdapter.GradeViewHolder>() {

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewEstudiante: TextView = itemView.findViewById(R.id.textViewEstudiante)
        val textViewGrado: TextView = itemView.findViewById(R.id.textViewGrado)
        val textViewMateria: TextView = itemView.findViewById(R.id.textViewMateria)
        val textViewNota: TextView = itemView.findViewById(R.id.textViewNota)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)
        val btnEditar: ImageView = itemView.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val grade = gradesList[position]

        holder.textViewEstudiante.text = grade.nombreEstudiante ?: "Sin nombre"
        holder.textViewGrado.text = grade.grado ?: "Sin grado"
        holder.textViewMateria.text = grade.materia ?: "Sin materia"
        holder.textViewNota.text = String.format("%.2f", grade.notaFinal ?: 0.0)

        // Configurar color de la nota según el valor
        val nota = grade.notaFinal ?: 0.0
        when {
            nota >= 7.0 -> holder.textViewNota.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_green_dark)
            )
            nota >= 6.0 -> holder.textViewNota.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_orange_dark)
            )
            else -> holder.textViewNota.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_red_dark)
            )
        }

        // Configurar botón eliminar
        holder.btnEliminar.setOnClickListener {
            mostrarDialogoEliminar(holder.itemView, grade, position)
        }

        // Configurar botón editar
        holder.btnEditar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditGradeActivity::class.java).apply {
                putExtra(EditGradeActivity.EXTRA_GRADE_ID, grade.id)
                putExtra(EditGradeActivity.EXTRA_STUDENT_ID, grade.estudianteId)
                putExtra(EditGradeActivity.EXTRA_STUDENT_NAME, grade.nombreEstudiante)
                putExtra(EditGradeActivity.EXTRA_GRADE_LEVEL, grade.grado)
                putExtra(EditGradeActivity.EXTRA_SUBJECT, grade.materia)
                putExtra(EditGradeActivity.EXTRA_FINAL_GRADE, grade.notaFinal ?: 0.0)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = gradesList.size

    private fun mostrarDialogoEliminar(itemView: View, grade: Nota, position: Int) {
        val context = itemView.context
        AlertDialog.Builder(context)
            .setTitle("Eliminar Nota")
            .setMessage("¿Estás seguro de que deseas eliminar la nota de ${grade.nombreEstudiante} en ${grade.materia}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarNota(itemView, grade, position)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarNota(itemView: View, grade: Nota, position: Int) {
        val context = itemView.context
        grade.id?.let { gradeId ->
            val databaseGrades = FirebaseDatabase.getInstance().getReference("grades")
            databaseGrades.child(gradeId).removeValue()
                .addOnSuccessListener {
                    gradesList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, gradesList.size)
                    Toast.makeText(context, "Nota eliminada exitosamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Error al eliminar: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}