package com.dicoding.asclepius.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.response.NewsResponse
import com.dicoding.asclepius.retrofit.NewsApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val apiKey = "acbbf00b76384e2887e216d287659545"  // ini API KEY yang saya gunakan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Memanggil fungsi untuk mengambil data berita
        fetchNews()
    }

    private fun fetchNews() {
        Log.d("NewsActivity", "fetchNews() called") // Log untuk menandai awal pemanggilan fungsi

        // Menggunakan NewsApiClient untuk memanggil API dan mendapatkan berita kesehatan
        NewsApiClient.instance.getHealthNews(apiKey = apiKey).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.d("NewsActivity", "onResponse() called with response: ${response.code()}") // Log respons kode HTTP

                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    newsResponse?.let {
                        Log.d("NewsActivity", "News loaded successfully, total articles: ${it.articles.size}") // Log jumlah artikel
                        // Mengatur adapter RecyclerView dengan data artikel dari respons
                        val adapter = NewsAdapter(it.articles)
                        binding.newsRecyclerView.adapter = adapter
                    }
                } else {
                    Log.e("NewsActivity", "Failed to load news, response code: ${response.code()}") // Log jika respons tidak berhasil
                    Toast.makeText(this@NewsActivity, "Failed to load news", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("NewsActivity", "onFailure() called, error: ${t.message}") // Log pesan kesalahan
                // Menampilkan pesan kesalahan jika gagal mengambil data
                Toast.makeText(this@NewsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
