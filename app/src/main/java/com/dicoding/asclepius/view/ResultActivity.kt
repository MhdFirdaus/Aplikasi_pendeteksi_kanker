package com.dicoding.asclepius.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.local.entity.CancerResult
import com.dicoding.asclepius.local.room.CancerDatabase
import com.dicoding.asclepius.repository.CancerResultRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {

    // Variabel untuk menyimpan hasil klasifikasi dan byte array gambar yang dipilih
    private var classificationResult: ClassificationResult? = null
    private var byteArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Inisialisasi komponen UI
        val resultImageView: ImageView = findViewById(R.id.result_image)
        val resultTextView: TextView = findViewById(R.id.result_text)
        val saveButton: ImageButton = findViewById(R.id.save_button)
        val backButton: ImageButton = findViewById(R.id.back_button)

        // Mendapatkan hasil klasifikasi dan byte array gambar dari Intent
        classificationResult = intent.getParcelableExtra("classificationResult")
        byteArray = intent.getByteArrayExtra("selectedImage")

        // Log untuk memeriksa data yang diterima
        Log.d("ResultActivity", "Classification Result: $classificationResult")
        Log.d("ResultActivity", "ByteArray Size: ${byteArray?.size}")

        val db = CancerDatabase.getDatabase(this)
        val repository = CancerResultRepository(db.cancerResultDao())

        // Mengubah byte array ke Bitmap dan menampilkannya di ImageView
        byteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            resultImageView.setImageBitmap(bitmap)
        }

        // Menampilkan hasil klasifikasi jika tersedia
        classificationResult?.let {
            resultTextView.text = "Classification: ${it.label}\nConfidence: ${(it.confidence * 100).toInt()}%"
        } ?: run {
            resultTextView.text = "Failed to classify the image."
        }

        // Menyimpan hasil result ke database
        saveButton.setOnClickListener {
            saveResultToDatabase(repository)
            Toast.makeText(this, "Result saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali ke MainActivity
        backButton.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk menyimpan hasil result ke database
    private fun saveResultToDatabase(repository: CancerResultRepository) {
        // Ambil nilai label dan confidence dari hasil klasifikasi
        val label = classificationResult?.label ?: "Unknown"
        val confidence = classificationResult?.confidence ?: 0.0f
        val imageData = byteArray ?: ByteArray(0)

        // Membuat objek CancerResult yang akan disimpan ke dalam database
        val result = CancerResult(
            label = label,
            confidence = confidence,
            imageData = imageData
        )

        // Menyimpan hasil ke database menggunakan coroutine
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertResult(result)
            runOnUiThread {
                Toast.makeText(this@ResultActivity, "Result saved successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Parcelable class untuk mengirim data hasil klasifikasi melalui Intent
    class ClassificationResult(val label: String, val confidence: Float) : Parcelable {
        // Konstruktor untuk membaca data dari Parcel
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readFloat()
        )

        // Menulis data ke Parcel
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(label)
            parcel.writeFloat(confidence)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<ClassificationResult> {
            // Membuat instance dari ClassificationResult dari Parcel
            override fun createFromParcel(parcel: Parcel): ClassificationResult {
                return ClassificationResult(parcel)
            }

            // Membuat array dari ClassificationResult
            override fun newArray(size: Int): Array<ClassificationResult?> = arrayOfNulls(size)
        }
    }
}