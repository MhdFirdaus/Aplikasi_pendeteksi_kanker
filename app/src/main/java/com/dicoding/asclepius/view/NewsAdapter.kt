package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.response.Article

class NewsAdapter(private val articles: List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.newsImageView)
        private val titleTextView: TextView = view.findViewById(R.id.newsTitleTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.newsDescriptionTextView)
        private val readMoreTextView: TextView = view.findViewById(R.id.readMoreTextView)

        fun bind(article: Article) {
            titleTextView.text = article.title
            descriptionTextView.text = article.description ?: "No description available"

            // Menampilkan gambar artikel dengan Glide
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_place_holder) // Placeholder jika gambar tidak tersedia
                .into(imageView)

            // Listener untuk "Read More" yang membuka URL artikel di browser
            readMoreTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                itemView.context.startActivity(intent)
            }
        }
    }
}
