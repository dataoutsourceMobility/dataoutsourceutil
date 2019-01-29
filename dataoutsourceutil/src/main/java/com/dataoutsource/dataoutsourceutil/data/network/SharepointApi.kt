package com.dataoutsource.dataoutsourceutil.data.network

import android.util.Log
import com.dataoutsource.dataoutsourceutil.data.data.User.UserRoot
import com.dataoutsource.dataoutsourceutil.data.data.formdigest.FormDigestRoot
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers

interface SharepointApi {

    companion object {

        operator fun invoke(baseUrl: String, rtFa: String, fedAuth: String): SharepointApi {
            val interceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Cookie","$rtFa; $fedAuth")
                    .header("Accept","application/json;odata=verbose")
                    .build()

                Log.e("request", request.url().toString())

                return@Interceptor chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(SharepointApi::class.java)
        }
    }

    @GET("_api/web/currentUser")
    fun getUser() : Deferred<UserRoot>

    @GET("_api/web/siteuser")
    fun getSiteUsers() : Deferred<ResponseBody>

    @POST("_api/contextinfo")
    fun getFormDigest() : Deferred<FormDigestRoot>

    @GET("_api/web/lists/getbytitle('{listTitle}')/items")
    fun getListItemsByTitle(
        @Path("listTitle") listTitle: String,
        @Query("\$select") selectQuery : String = "",
        @Query("\$expand") expandQuery : String = "") : Deferred<ResponseBody>

    @GET("_api/web/lists(guid'{listGuid}')/items")
    fun getListItemsByGuid(
        @Path("listGuid") listGuid: String,
        @Query("\$select") selectQuery: String = "",
        @Query("\$expand") expandQuery: String = ""):Deferred<ResponseBody>

    @GET("_api/web/GetFolderByServerRelativeUrl('{libraryName}')/Files")
    fun getFileFromDocLibrary(
        @Path("libraryName") libraryName: String
    ): Deferred<ResponseBody>

    @Headers("Content-Type: application/json;odata=verbose")
    @POST("_api/web/lists/getbytitle('{listTitle}')/items")
    fun addListItemByListTitle(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listTitle") listTitle : String,
        @Body params: String
    ) : Deferred<ResponseBody>

    @Headers("Content-Type: application/json;odata=verbose")
    @POST("_api/web/lists(guid'{listGuid}')/items")
    fun addListItemByListGuid(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listGuid") listGuid : String,
        @Body params: String
    ) : Deferred<ResponseBody>

    @Headers("Content-Type: application/json;odata=verbose",
        "X-HTTP-Method:MERGE",
        "IF-MATCH:*")
    @POST("_api/web/lists/getbytitle('{listTitle}')/items({itemId})")
    fun updateListItemBytListTitle(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listTitle") listTitle : String,
        @Path("itemId") itemId: String,
        @Body params: String
    ): Deferred<ResponseBody>

    @Headers("Content-Type: application/json;odata=verbose",
        "X-HTTP-Method:MERGE",
        "IF-MATCH:*")
    @POST("_api/web/lists/getbytitle('{listGuid}')/items({itemId})")
    fun updateListItemBytListGuid(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listGuid") listGuid : String,
        @Path("itemId") itemId: String,
        @Body params: String
    ): Deferred<ResponseBody>

    @Headers("X-HTTP-Method:DELETE",
        "IF-MATCH:*")
    @POST("_api/web/lists/getbytitle('{listTitle}')/items({itemId})")
    fun deleteListItemBytListTitle(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listTitle") listTitle : String,
        @Path("itemId") itemId: String,
        @Body params: String
    ): Deferred<ResponseBody>

    @Headers("X-HTTP-Method:DELETE",
        "IF-MATCH:*")
    @POST("_api/web/lists/getbytitle('{listGuid}')/items({itemId})")
    fun deleteListItemBytListGuid(
        @Header("X-RequestDigest") formDigest : String,
        @Path("listGuid") listGuid : String,
        @Path("itemId") itemId: String,
        @Body params: String
    ): Deferred<ResponseBody>

    @Multipart
    @Headers("Content-Type: application/json;odata=verbose")
    @POST("_api/web/GetFolderByServerRelativeUrl('{libraryName}')/Files/Add(url='{fileName}', overwrite=true)")
    fun uploadFileToDocLibrary(
        @Path("libraryName") libraryName: String,
        @Path("fileName") fileName: String,
        @Header("X-RequestDigest") formDigest : String,
        @Part document: MultipartBody.Part
    ): Deferred<ResponseBody>

}