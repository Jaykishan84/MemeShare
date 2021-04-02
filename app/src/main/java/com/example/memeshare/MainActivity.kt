package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentimageurl :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme(){
        progressmeme.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"
//        val url = "https://www.google.com"
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    currentimageurl = response.getString("url")
                    Glide.with(this).load(currentimageurl).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressmeme.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressmeme.visibility = View.GONE
                            return false
                        }

                    }).into(imageview)
                },
                Response.ErrorListener{
                    Log.d("error", it.localizedMessage)
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }

    fun sharememe(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"$currentimageurl")
        val chooser = Intent.createChooser(intent, "share this meme")
        startActivity(chooser)
    }


}