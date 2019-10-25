package com.rocket.retrofitwithrxjavatutorial

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {

    private val compositeDisposableOnDestroy = CompositeDisposable()
    private var latestCatsCall: Disposable? = null

    //the list, that will be observed by the activity
    val bunchOfCats = MutableLiveData<List<NetCat>>()
    //the error message observed
    val errorMessage = MutableLiveData<String>()

    fun getSomeCats(serverUrl: String, isDebug: Boolean, apiKey: String) {
        val catsRepository = CatsRepository(serverUrl, BuildConfig.DEBUG, apiKey)

        latestCatsCall?.dispose()

        latestCatsCall =
            catsRepository.getNumberOfRandomCats(10, null).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnDestroy.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            errorMessage.postValue("Error getting cats $it")
                        }
                            ?: kotlin.run {
                                errorMessage.postValue("Null error :(")
                            }

                        result.hasCats() -> result.netCats?.let {
                            bunchOfCats.postValue(it)
                            errorMessage.postValue("")
                        } ?: kotlin.run {
                            errorMessage.postValue("Null list of cats :(")
                        }

                        else-> errorMessage.postValue("No cats available :(")
                    }
                }
    }

    override fun onCleared() {
        compositeDisposableOnDestroy.clear()
        super.onCleared()
    }
}