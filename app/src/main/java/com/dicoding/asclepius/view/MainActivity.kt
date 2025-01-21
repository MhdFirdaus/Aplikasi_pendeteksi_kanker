package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.ByteArrayOutputStream
import java.io.File

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var previewImageView: ImageView
    private lateinit var progressIndicator: LinearProgressIndicator
    private var selectedImageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen UI
        previewImageView = findViewById(R.id.previewImageView)
        progressIndicator = findViewById(R.id.progressIndicator)
        val galleryButton: Button = findViewById(R.id.galleryButton)
        val analyzeButton: Button = findViewById(R.id.analyzeButton)
        val historyButton: ImageButton = findViewById(R.id.historyButton)
        val newsButton: ImageButton = findViewById(R.id.newsButton)

        // Inisialisasi helper untuk klasifikasi gambar
        imageClassifierHelper = ImageClassifierHelper(this)

        // fungsi klik pada button
        newsButton.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        galleryButton.setOnClickListener { selectImageFromGallery() }

        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        analyzeButton.setOnClickListener { analyzeImage() }
    }

    // Pilih gambar dari galeri
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    // Menangani hasil dari UCrop dan galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data?.data ?: return
                    startCrop(imageUri)
                }
            }
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        previewImageView.setImageBitmap(selectedImageBitmap)
                    }
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    val cropError = UCrop.getError(data!!)
                    Toast.makeText(this, cropError?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Untuk proses cropping gambar dengan UCrop
    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val options = UCrop.Options()
        options.setFreeStyleCropEnabled(true) // Mengaktifkan pemotongan bebas
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL) // pemotongan rotasi

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options) // Menggunakan opsi pemotongan
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    // Untuk analisis gambar
    private fun analyzeImage() {
        selectedImageBitmap?.let { bitmap ->
            progressIndicator.visibility = View.VISIBLE

            // Melakukan klasifikasi gambar di latar belakang
            imageClassifierHelper.classifyStaticImage(bitmap) { result, errorMessage ->
                progressIndicator.visibility = View.GONE
                if (result != null) {
                    navigateToResultActivity(result)
                } else {
                    Toast.makeText(this, errorMessage ?: "Klasifikasi gagal", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(this, "Silakan pilih gambar", Toast.LENGTH_SHORT).show()
    }

    // Navigasi ke ResultActivity dengan hasil klasifikasi
    private fun navigateToResultActivity(result: ResultActivity.ClassificationResult) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("classificationResult", result)

        // Mengonversi bitmap ke byte array untuk dikirim melalui intent
        selectedImageBitmap?.let { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("selectedImage", byteArray) // Menyimpan byte array di dalam intent
        }
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_GALLERY = 100
    }
}