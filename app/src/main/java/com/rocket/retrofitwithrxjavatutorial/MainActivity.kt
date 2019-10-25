package com.rocket.retrofitwithrxjavatutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val serverUrl = "https://api.thecatapi.com/v1/"

    private val apiKey = "e6c24e33-eb53-4d0d-8c4a-a31019158d2d"

    private val compositeDisposableOnPause = CompositeDisposable()
    private var latestCatCall: Disposable? = null

    private val btnClick by lazy {
        findViewById<Button>(R.id.btn_click)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClick.setOnClickListener {
            getSomeCats()
        }
    }

    private fun getSomeCats() {
        val catsRepository = CatsRepository(serverUrl, BuildConfig.DEBUG, apiKey)

        latestCatCall?.dispose()

        latestCatCall =
            catsRepository.getNumberOfRandomCats(10, null).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnPause.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            Toast.makeText(this@MainActivity, "Error getting cats$it", Toast.LENGTH_SHORT).show()
                        }
                            ?: kotlin.run {
                                Toast.makeText(this@MainActivity, "Null error", Toast.LENGTH_SHORT).show()
                            }

                        result.hasCats() -> result.netCats?.let {
                            Toast.makeText(this@MainActivity, "Cats received!", Toast.LENGTH_SHORT).show()
                        } ?: kotlin.run {
                            Toast.makeText(this@MainActivity, "Null list of cats", Toast.LENGTH_SHORT).show()
                        }

                        else-> Toast.makeText(this@MainActivity, "No cats available :(", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun clearAllJobsOnPause() {
        compositeDisposableOnPause.clear()
    }

    override fun onPause() {
        clearAllJobsOnPause()
        super.onPause()
    }
}
