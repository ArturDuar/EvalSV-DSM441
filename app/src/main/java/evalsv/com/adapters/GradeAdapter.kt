package evalsv.com.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import evalsv.com.R
import evalsv.com.models.Nota

class GradesAdapter(private val listaNotas: List<Nota>) :
    RecyclerView.Adapter<GradesAdapter.GradeViewHolder>() {

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewEstudiante: TextView = itemView.findViewById(R.id.textViewEstudiante)
        val textViewGrado: TextView = itemView.findViewById(R.id.textViewGrado)
        val textViewMateria: TextView = itemView.findViewById(R.id.textViewMateria)
        val textViewNota: TextView = itemView.findViewById(R.id.textViewNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val nota = listaNotas[position]

        holder.textViewEstudiante.text = nota.nombreEstudiante
        holder.textViewGrado.text = nota.grado
        holder.textViewMateria.text = nota.materia
        holder.textViewNota.text = String.format("%.2f", nota.notaFinal)
    }

    override fun getItemCount(): Int = listaNotas.size
}