package com.rocket.retrofitwithrxjavatutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val btnClick by lazy {
        findViewById<Button>(R.id.btn_click)
    }

    companion object {

        const val serverUrl = "https://api.thecatapi.com/v1/"

        const val apiKey = "e6c24e33-eb53-4d0d-8c4a-a31019158d2d"
    }

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.bunchOfCats.observe(this, Observer { onResult(it) })
        viewModel.errorMessage.observe(this, Observer { onError(it) })

        btnClick.setOnClickListener {
            viewModel.getSomeCats(serverUrl, BuildConfig.DEBUG, apiKey)
        }
    }

    private fun onResult(bunchOfCats: List<NetCat>) {
        Toast.makeText(this@MainActivity, "Got ${bunchOfCats.size} cats", Toast.LENGTH_SHORT).show()
    }

    private fun onError(errorMessage: String) {
        errorMessage.let {
            if (!it.isBlank()) {
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
