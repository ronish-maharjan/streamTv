package com.streamtv.app.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("filename") val filename: String,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Long?,
    @SerializedName("size") val size: Long,
    @SerializedName("streamUrl") val streamUrl: String,
    @SerializedName("createdAt") val createdAt: String
)

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("error") val error: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("page") val page: Int?,
    @SerializedName("limit") val limit: Int?
)
