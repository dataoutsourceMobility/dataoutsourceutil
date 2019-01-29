package com.dataoutsource.dataoutsourceutil.data.data.formdigest

import com.google.gson.annotations.SerializedName

data class GetContextWebInformation(
    @SerializedName("FormDigestTimeoutSeconds")
    val formDigestTimeoutSeconds: Int,
    @SerializedName("FormDigestValue")
    val formDigestValue: String,
    @SerializedName("LibraryVersion")
    val libraryVersion: String,
    @SerializedName("SiteFullUrl")
    val siteFullUrl: String,
    @SerializedName("WebFullUrl")
    val webFullUrl: String
)