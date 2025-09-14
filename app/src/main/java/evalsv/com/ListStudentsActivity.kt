package evalsv.com

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import evalsv.com.adapters.StudentAdapter
import evalsv.com.models.Estudiante

class ListStudentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentList: MutableList<Estudiante>
    private lateinit var adapter: StudentAdapter
    private lateinit var tvNoData: TextView

    private lateinit var btnRegistrarEstudiante: FloatingActionButton
    private val databaseStudents = FirebaseDatabase.getInstance().getReference("students")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_students)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Listar Estudiante"
            setDisplayHomeAsUpEnabled(true)
        }

        tvNoData = findViewById(R.id.tvNoData)
        btnRegistrarEstudiante = findViewById(R.id.btnRegistrarEstudiante)
        btnRegistrarEstudiante.setOnClickListener {
            val intent = Intent(this, RegisterStudentsActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerViewStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        studentList = mutableListOf()
        adapter = StudentAdapter(studentList)
        recyclerView.adapter = adapter

        fetchStudents()
    }

    private fun fetchStudents() {
        databaseStudents.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                for (data in snapshot.children) {
                    val student = data.getValue(Estudiante::class.java)
                    student?.let {
                        // Asignar manualmente el ID desde la clave del nodo
                        it.id = data.key
                        studentList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()

                if (studentList.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListStudentsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}