package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import com.dicoding.asclepius.ml.CancerClassification
import com.dicoding.asclepius.view.ResultActivity
import org.tensorflow.lite.support.image.TensorImage

class ImageClassifierHelper(private val context: Context) {

    // Holds the model instance
//    private var model: CancerClassification? = null

//    // Initialize the classifier model
//    private fun setupImageClassifier() {
//        model = CancerClassification.newInstance(context)
//    }

    fun classifyStaticImage(bitmap: Bitmap, callback: (ResultActivity.ClassificationResult?, String?) -> Unit) {
        try {
            val model = CancerClassification.newInstance(context)
            val image = TensorImage.fromBitmap(bitmap)
            val outputs = model.process(image)
            val probabilityList = outputs.probabilityAsCategoryList

            // Get classification result with the highest confidence score
            val highestResult = probabilityList.maxByOrNull { it.score }
            highestResult?.let {
                callback(ResultActivity.ClassificationResult(it.label, it.score), null)
            } ?: callback(null, "No results found")

            model.close()
        } catch (e: Exception) {
            callback(null, e.message)
        }
    }

//    // Helper function to convert Uri to Bitmap
//    private fun getBitmapFromUri(uri: Uri): Bitmap? {
//        return try {
//            val inputStream = context.contentResolver.openInputStream(uri)
//            BitmapFactory.decodeStream(inputStream)
//        } catch (e: Exception) {
//            null
//        }
//    }
}


