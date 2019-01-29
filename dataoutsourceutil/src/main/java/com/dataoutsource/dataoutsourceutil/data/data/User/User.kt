package com.dataoutsource.dataoutsourceutil.data.data.User

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("IsEmailAuthenticationGuestUser")
    val isEmailAuthenticationGuestUser: Boolean,
    @SerializedName("IsHiddenInUI")
    val isHiddenInUI: Boolean,
    @SerializedName("IsShareByEmailGuestUser")
    val isShareByEmailGuestUser: Boolean,
    @SerializedName("IsSiteAdmin")
    val isSiteAdmin: Boolean,
    @SerializedName("LoginName")
    val loginName: String,
    @SerializedName("PrincipalType")
    val principalType: Int,
    @SerializedName("Title")
    val title: String
)