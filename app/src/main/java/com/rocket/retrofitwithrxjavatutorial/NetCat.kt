package com.rocket.retrofitwithrxjavatutorial

import com.google.gson.annotations.SerializedName

data class NetCat (

    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("breeds") val breeds: List<Any>,
    @SerializedName("categories") val categories: List<Any>
) {
    override fun toString(): String {
        return "NetCat(id='$id', url='$url', breeds=$breeds, categories=$categories)"
    }
}