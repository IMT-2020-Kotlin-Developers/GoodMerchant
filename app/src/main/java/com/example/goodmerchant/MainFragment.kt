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
import com.example.goodmerchant.Recyclerview.ListFragmentDirections
import com.example.goodmerchant.Retrofit.*
import com.example.goodmerchant.ViewModel.productViewmodel
import com.example.goodmerchant.databinding.FragmentMainBinding

import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
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

    private var bitmap: Bitmap? = null
    var c = 1
    var ch = 1

    private lateinit var viewModel: productViewmodel
    lateinit var imageUri: Uri
    lateinit var binding: FragmentMainBinding
    lateinit var imageLink: URL
    lateinit var imageTag: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(productViewmodel::class.java)
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        //manual search

        var temp: Boolean = false

        binding.searchicon.setOnClickListener {
            if (binding.searchtext.text != null) {
                imageTag = ""
                imageTag = binding.searchtext.text.toString()

                if(imageTag != "")
                    fillListfragment(imageTag)
                else{
                    Toast.makeText(
                        requireActivity(),
                        "Retry",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.searchiconforUrl.setOnClickListener {
           getTags()
        }

        //switch1
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            c = if (isChecked) 2
            else 1
        }

        //switch2
        binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            val u :Int = if (isChecked) 2
            else 1

            if(u==2)
            {
                binding.urlview.visibility = View.VISIBLE
                binding.mainbarview.visibility = View.GONE

            }

            else
            {
                binding.mainbarview.visibility = View.VISIBLE
                binding.urlview.visibility = View.GONE
            }
        }

        binding.camera.setOnClickListener {
                ch = 1
                pickImagecam()

        }
        binding.gallery.setOnClickListener {
                ch = 2
                pickImage()

        }

        return binding.root
    }

    private fun pickImagecam() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 110)
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 110)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 110 && resultCode == RESULT_OK) {
            //var s1 =""
            //var s2 =""

            if (ch == 1) {
                bitmap = null
                bitmap = data?.extras?.get("data") as Bitmap
            }

            else if(ch == 2) {

               /* Toast.makeText(
                    requireActivity(),
                    "ch working",
                    Toast.LENGTH_SHORT
                ).show() */

                data?.data?.let {
                    bitmap = null
                    bitmap = getBitmapFromUri(it);
                }

            }

            if(c==2) {

            val recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            bitmap?.let {
                val image = InputImage.fromBitmap(it, 0)

              /*  Toast.makeText(
                    requireActivity(),
                    "c working",
                    Toast.LENGTH_SHORT
                ).show() */

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->

                        if(visionText.text != "") {

                               fillListfragment(visionText.text)

                            Toast.makeText(
                                requireActivity(),
                                visionText.text,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else{
                            Toast.makeText(
                                requireActivity(),
                                "Retry",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireActivity(),
                            "Error: " + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            if(c==1) {

                val recognizer =
                    ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

                bitmap?.let {
                    val image = InputImage.fromBitmap(it, 0)
                    recognizer.process(image)
                        .addOnSuccessListener { labels ->

                            if(labels[0].text!= "") {

                                fillListfragment(labels[0].text)

                                Toast.makeText(
                                    requireActivity(),
                                    labels[0].text,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else{
                                Toast.makeText(
                                    requireActivity(),
                                    "Retry",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireActivity(),
                                "Error: " + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }


            if (bitmap == null)
                Toast.makeText(
                    requireActivity(),
                    "Please select an image!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    //URI to bitmap
    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    fun fillListfragment(tag: String) {

        viewModel.repository.getProducts(tag)
        binding.progressBarMain.visibility = View.VISIBLE
        binding.frontscreen.visibility = View.GONE
        Handler().postDelayed({
            val productDetailList: Array<productModal> = viewModel.repository.getproductsfromlist()
            val directions =
                MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
            findNavController().navigate(directions)
            binding.progressBarMain.visibility = View.GONE
            binding.frontscreen.visibility = View.VISIBLE
        }, 10000)
    }

    fun getTags() {
        val tag = tagservices.tagInstance.getTag(binding.searchUrl.text.toString())
        tag.enqueue(object : Callback<imagetagResult> {
            override fun onResponse(
                call: Call<imagetagResult>,
                response: Response<imagetagResult>
            ) {
                val currenttag: imagetagResult? = response.body()
                if (currenttag != null) {
                    val tagDetail: String = currenttag.searchInformation!!.query_displayed
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

}









