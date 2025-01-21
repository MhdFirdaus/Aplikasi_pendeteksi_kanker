package com.dicoding.asclepius.view

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.local.entity.CancerResult

class HistoryAdapter(
    private var data: List<CancerResult>,
    private val onDelete: (CancerResult) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // Memperbarui data adapter dan memberi tahu RecyclerView untuk refresh
    fun updateData(newData: List<CancerResult>) {
        data = newData
        // Memberi tahu adapter bahwa data telah berubah
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val result = data[position]
        holder.bind(result)
    }

    // Mengembalikan ukuran data
    override fun getItemCount(): Int = data.size

    // ViewHolder untuk mengelola tampilan tiap item
    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.history_image)
        private val labelTextView: TextView = itemView.findViewById(R.id.history_label)
        private val confidenceTextView: TextView = itemView.findViewById(R.id.history_confidence)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(result: CancerResult) {
            // Konversi byte array menjadi Bitmap
            val bitmap = BitmapFactory.decodeByteArray(result.imageData, 0, result.imageData.size)
            imageView.setImageBitmap(bitmap)

            // Menampilkan label dan confidence dari hasil klasifikasi
            labelTextView.text = "Label: ${result.label}"
            confidenceTextView.text = "Confidence: ${(result.confidence * 100).toInt()}%"

            // Mengatur klik listener untuk tombol hapus
            deleteButton.setOnClickListener {
                // Memanggil callback saat item dihapus
                onDelete(result)
            }
        }
    }
}
