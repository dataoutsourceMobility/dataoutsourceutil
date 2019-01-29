package com.dataoutsource.dataoutsourceutils


import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dataoutsource.dataoutsourceutil.data.network.SharepointApi
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.android.synthetic.main.fragment_experiment.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ExperimentFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGetUser.setOnClickListener(clickListener)
        btnGetFormDigest.setOnClickListener(clickListener)
        btnGetListItemsTitle.setOnClickListener(clickListener)
        btnAddListItem.setOnClickListener(clickListener)
        btnCreatePDF.setOnClickListener(clickListener)
        btnReadAssetsFile.setOnClickListener(clickListener)
        btnAddFile.setOnClickListener(clickListener)
    }

    val clickListener = View.OnClickListener { view ->

        val activity = (activity as MainActivity)
        val (rtFa, FedAuth) = activity.getCookies()
        val apiService =
            SharepointApi(activity.traingUrl, rtFa, FedAuth)

        when(view.id){
            R.id.btnGetUser ->{
                GlobalScope.launch(Dispatchers.Main) {
                    val user = apiService.getUser()
                    Log.e("userx", user.await().d.title)
                }
            }
            R.id.btnGetFormDigest ->{
                GlobalScope.launch(Dispatchers.Default) {
                    val formDigest = apiService.getFormDigest().await()
                    Log.e("formDigest", formDigest.d.getContextWebInformation.formDigestValue)
                }
            }
            R.id.btnGetListItemsTitle ->{
                //callService { apiService.getListItemsByTitle("Labor Used") }
                GlobalScope.launch {
                    var listItems = apiService.getListItemsByTitle("DummyList")
                    Log.e("listItems", listItems.await().string())
                }
            }
            R.id.btnAddListItem -> {

                GlobalScope.launch {

                    val params = ArrayList<Pair<String,String>>()
                    params.add(Pair("Title","breakfast"))
                    params.add(Pair("food","egg rice"))
                    params.add(Pair("drinks","water"))

                    val body = activity.createJsonBody(
                        params,"SP.Data.DummyListListItem"
                    )
                    Log.e("body", body)

                    val formDigest = apiService.getFormDigest()
                    val response = apiService.addListItemByListTitle(
                        formDigest.await().d.getContextWebInformation.formDigestValue,
                        "DummyList",
                        body
                    ).await()

                    Log.e("adding",response.string())
                }
            }
            R.id.btnCreatePDF -> {
                var pdfContent = ""
                activity.application.assets.open("sample.txt").apply {
                    pdfContent = this.readBytes().toString(Charsets.UTF_8)
                }.close()

                CreatePdf(requireContext())
                    .setPdfName("Sample PDF1")
                    .setContent(pdfContent)
                    .setCallbackListener(object : CreatePdf.PdfCallbackListener{
                        override fun onFailure(errorMsg: String) {
                            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
                        }

                        override fun onSuccess(filePath: String) {
                            Toast.makeText(requireContext(), "Pdf Saved at: $filePath", Toast.LENGTH_SHORT).show()
                            Log.e("savedAt", "Pdf Saved at: $filePath")
                        }
                    })
                    .create()
            }
            R.id.btnReadAssetsFile -> {

                val source = File("/data/data/com.dataoutsource.dataoutsourceutils/cache/Sample PDF4.pdf")
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"/PDFS/sample2.pdf")
                file.createNewFile()
                val dest = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"/PDFS/sample2.pdf")

                source.inputStream().use {input ->
                    dest.outputStream().use {output ->
                        input.copyTo(output)
                    }
                }

                Log.e("pdf", "copying done")
            }
            R.id.btnAddFile -> {
                GlobalScope.launch {
                    val file = File("/data/data/com.dataoutsource.dataoutsourceutils/cache/Sample PDF4.pdf")
                    if(file.exists()){
                        Log.e("filex","existing")
                        val uri = Uri.fromFile(file)
                        Log.e("filex",uri.toString())

                        val requestFile = RequestBody.create(
                            MediaType.parse("application/pdf"),
                            file
                        )
                        val body = MultipartBody.Part.createFormData("SAMPLE","TESTING PDF.pdf",requestFile)

                        val formDigest = apiService.getFormDigest()
                        val addFile = apiService.uploadFileToDocLibrary(
                            "Testing",
                            "TEST2.pdf",
                            formDigest.await().d.getContextWebInformation.formDigestValue,
                            body
                        ).await()

                        Log.e("adding",addFile.string())
                    }
                    else
                        Log.e("filex","not existing")
                }
            }
            else -> {

            }
        }
    }

    private fun callService(apiCall: ()-> Deferred<ResponseBody>) : String{
        var response = ""

        GlobalScope.launch(Dispatchers.Main) {
            try{
                val userJson = apiCall().await()
                Log.e("user", userJson.string())
                response = userJson.string()
            }
            catch (e: Exception){
                Log.e("error", e.message)
            }
        }

        return response
    }

}
