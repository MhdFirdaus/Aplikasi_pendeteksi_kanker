package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.local.entity.CancerResult
import com.dicoding.asclepius.local.room.CancerDatabase
import com.dicoding.asclepius.repository.CancerResultRepository
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var repository: CancerResultRepository
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val db = CancerDatabase.getDatabase(this)
        repository = CancerResultRepository(db.cancerResultDao())

        val recyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan list kosong, kemudian pasang ke RecyclerView
        historyAdapter = HistoryAdapter(emptyList()) { result -> deleteResult(result) }
        recyclerView.adapter = historyAdapter

        // Mengambil data dari database dan memperbarui adapter
        lifecycleScope.launch {
            val results = repository.getAllResults()
            historyAdapter = HistoryAdapter(results) { result -> deleteResult(result) }
            recyclerView.adapter = historyAdapter
        }
    }

    // Fungsi untuk menghapus hasil dari database
    private fun deleteResult(result: CancerResult) {
        lifecycleScope.launch {
            repository.deleteResult(result)
            val results = repository.getAllResults()
            historyAdapter.updateData(results)
        }
    }
}
