package com.rocket.retrofitwithrxjavatutorial

import io.reactivex.Single

class CatsRepository(
    baseUrl: String,
    isDebuEnabled: Boolean,
    apiKey: String
) : Repository(baseUrl, isDebuEnabled, apiKey){

    private val catsDataSource: CatsDataSource = CatsDataSource(retrofit)

    inner class Result(val netCats: List<NetCat>?  = null, val errorMessage: String? = null) {
        fun hasCats(): Boolean {
            return netCats != null && !netCats.isEmpty()
        }

        fun hasError(): Boolean {
            return errorMessage != null
        }
    }

    fun getNumberOfRandomCats(limit: Int, category_ids: Int? ): Single<Result> {

        return catsDataSource.getNumberOfRandomCats(limit, category_ids)
            .map{newCats: List<NetCat> -> Result(netCats = newCats)}
            .onErrorReturn { t: Throwable -> Result(errorMessage =  t.message) }
    }
}