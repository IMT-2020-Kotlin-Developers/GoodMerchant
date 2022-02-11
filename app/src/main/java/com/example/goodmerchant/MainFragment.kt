package com.example.goodmerchant

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.goodmerchant.Imgur.ImgurApi
import com.example.goodmerchant.Imgur.ImgurUploadJson
import com.example.goodmerchant.Imgur.imagetoUrl
import com.example.goodmerchant.Recyclerview.ListFragmentDirections
import com.example.goodmerchant.Retrofit.*
import com.example.goodmerchant.ViewModel.productViewmodel
import com.example.goodmerchant.databinding.FragmentMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.common.model.LocalModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.splash.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.URI
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection


class MainFragment : Fragment() {


    private lateinit var viewModel : productViewmodel
    lateinit var imageUri : Uri
    lateinit var binding: FragmentMainBinding
    lateinit var imageLink : URL
    lateinit var imageTag : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(productViewmodel::class.java)
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        //manual search
        var temp : Boolean = false
        binding.searchicon.setOnClickListener {
            if (binding.searchtext.text != null) {
                imageTag =  binding.searchtext.text.toString()
                fillListfragment(imageTag)
            }
        }

        binding.camera.setOnClickListener{

        }
        binding.gallery.setOnClickListener{
            slectimage()

        }

        return binding.root
    }

    private fun slectimage() {
        val intent  = Intent()
        intent.type  = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }
    private fun uploadimage(){
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Analizing")
        progressBar.setCancelable(false)
        progressBar.show()

        val formatter = SimpleDateFormat("yyyy__MM__dd__HH__mm__ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(imageUri).
        addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                if(progressBar.isShowing) progressBar.dismiss()
                imageLink =  URL(it.toString())
                Log.d("%%%%%", imageLink.toString())
                getTags()

            }.addOnFailureListener{
                if(progressBar.isShowing) progressBar.dismiss()
                Log.d("%%%%%", "Failed",it)
            }
        }.addOnFailureListener{
            if(progressBar.isShowing) progressBar.dismiss()
            Log.d("%%%%%", "Failed",it)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            uploadimage()
        }
    }
    fun getTags(){
        val tag = tagservices.tagInstance.getTag(imageLink.toString())
        tag.enqueue(object : Callback<imagetagResult>{
            override fun onResponse(call: Call<imagetagResult>, response: Response<imagetagResult>) {
                val currenttag : imagetagResult? = response.body()
                if(currenttag != null){
                    val tagDetail : String = currenttag.searchInformation!!.query_displayed
                    imageTag = tagDetail
                    fillListfragment(imageTag)

                    Log.d("%%%%%", tagDetail)
                }
            }

            override fun onFailure(call: Call<imagetagResult>, t: Throwable) {
                Log.d("%%%%%", "Failed sharam ati hai?", t)
            }
        })
    }
    fun fillListfragment(tag : String){

        viewModel.repository.getProducts(tag)
        Handler().postDelayed({
            var productDetailList: Array<productModal> = viewModel.repository.getproductsfromlist()
            val directions = MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
            findNavController().navigate(directions)}
            ,6000)
    }
}









