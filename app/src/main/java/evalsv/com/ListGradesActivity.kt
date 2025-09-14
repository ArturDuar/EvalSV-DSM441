package evalsv.com

import android.content.Intent
import android.media.tv.TvView
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
import com.google.firebase.database.*
import evalsv.com.adapters.GradesAdapter
import evalsv.com.models.Nota

class ListGradesActivity : AppCompatActivity() {

    private lateinit var btnRegisterGrade: FloatingActionButton
    private lateinit var tvNoData: TextView
    private lateinit var recyclerViewGrades: RecyclerView
    private lateinit var gradesAdapter: GradesAdapter
    private lateinit var dbRefGrades: DatabaseReference

    private val listaNotas = mutableListOf<Nota>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_grades)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Listar Notas"
            setDisplayHomeAsUpEnabled(true)
        }

        initializeViews()
        setupRecyclerView()
        setupFirebase()
        loadGrades()
        setupFloatingActionButton()
    }

    private fun initializeViews() {
        btnRegisterGrade = findViewById(R.id.btnRegistrarNotas)
        recyclerViewGrades = findViewById(R.id.recyclerViewGrades)
        tvNoData = findViewById(R.id.tvNoData)
    }

    private fun setupRecyclerView() {
        gradesAdapter = GradesAdapter(listaNotas)
        recyclerViewGrades.apply {
            layoutManager = LinearLayoutManager(this@ListGradesActivity)
            adapter = gradesAdapter
        }
    }

    private fun setupFirebase() {
        dbRefGrades = FirebaseDatabase.getInstance().getReference("grades")
    }

    private fun loadGrades() {
        dbRefGrades.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaNotas.clear()

                for (notaSnapshot in snapshot.children) {
                    val nota = notaSnapshot.getValue(Nota::class.java)
                    nota?.let {
                        listaNotas.add(it)
                    }
                }
                gradesAdapter.notifyDataSetChanged()
                if (listaNotas.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListGradesActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun setupFloatingActionButton() {
        btnRegisterGrade.setOnClickListener {
            val intent = Intent(this, RegisterGradesActivity::class.java)
            startActivity(intent)
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