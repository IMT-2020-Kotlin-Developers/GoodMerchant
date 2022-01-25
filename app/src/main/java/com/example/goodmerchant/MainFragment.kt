package com.example.goodmerchant

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goodmerchant.Recyclerview.ListFragmentDirections
import com.example.goodmerchant.Retrofit.*
import com.example.goodmerchant.databinding.FragmentMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileDescriptor
import java.io.IOException

class MainFragment : Fragment() {

    private var bitmap: Bitmap? = null
    var c = 1

    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        //switch
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            c = if (isChecked) 2
            else 1
        }

        //manual search
        binding.searchicon.setOnClickListener {
            if (binding.searchtext.text != null) {
                getProducts()
            }
//            findNavController().navigate(R.id.action_mainFragment_to_listFragment)
        }

        //select from gallery
        binding.gallery.setOnClickListener {
            if (c == 2)
                pickImage()
            else
                pickImageob()
        }

        //select from camera not implemented
        binding.camera.setOnClickListener {
            if (c == 2)
                pickImagecam()
            else
                pickImagecamob()
        }

        return binding.root
    }

    //uri from gallery pick text
    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 110)
    }

    //uri from gallery pick object
    private fun pickImageob() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 130)
    }

    //uri from camera pick text not implemented
    private fun pickImagecam() {
        val intentcam = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentcam.type = "image/*"
        intentcam.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intentcam, "Click Picture"), 120)
    }

    //uri from camera pick object not implemented
    private fun pickImagecamob() {
        val intentcam = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentcam.type = "image/*"
        intentcam.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intentcam, "Click Picture"), 140)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            110 -> { // text-gallery
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        data?.data?.let {
                            bitmap = null
                            bitmap = getBitmapFromUri(it);
                        }

                        val recognizer =
                            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        bitmap?.let {
                            val image = InputImage.fromBitmap(it, 0)
                            recognizer.process(image)
                                .addOnSuccessListener { visionText ->
                                    // textBlocks -> will return list of block of detected text
                                    // lines -> will return list of detected lines
                                    // elements -> will return list of detected words
                                    // boundingBox -> will return rectangle box area in bitmap
                                    Toast.makeText(
                                        requireActivity(),
                                        visionText.text,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireActivity(),
                                        "Error: " + e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            }

            120 -> { // text-camera
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        data?.data?.let {
                            bitmap = null
                            bitmap = getBitmapFromUri(it);
                        }

                        val recognizer =
                            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        bitmap?.let {
                            val image = InputImage.fromBitmap(it, 0)
                            recognizer.process(image)
                                .addOnSuccessListener { visionText ->
                                    // textBlocks -> will return list of block of detected text
                                    // lines -> will return list of detected lines
                                    // elements -> will return list of detected words
                                    // boundingBox -> will return rectangle box area in bitmap
                                    Toast.makeText(
                                        requireActivity(),
                                        visionText.text,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireActivity(),
                                        "Error: " + e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }

                        if (bitmap == null)
                            Toast.makeText(
                                requireActivity(),
                                "Please click an image!",
                                Toast.LENGTH_SHORT
                            ).show()

                    }
                }
            }

            130 -> { //object-gallery
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        data?.data?.let {
                            bitmap = null
                            bitmap = getBitmapFromUri(it);
                        }

                        val recognizer =
                            ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                        bitmap?.let {
                            val image = InputImage.fromBitmap(it, 0)
                            recognizer.process(image)
                                .addOnSuccessListener { labels ->
                                    //for (label in labels) {} - when multiple labels needed
                                    Toast.makeText(
                                        requireActivity(),
                                        labels[0].text,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireActivity(),
                                        "Error: " + e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            }

            140 -> { //object-camera
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        data?.data?.let {
                            bitmap = null
                            bitmap = getBitmapFromUri(it);
                        }

                        val recognizer =
                            ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                        bitmap?.let {
                            val image = InputImage.fromBitmap(it, 0)
                            recognizer.process(image)
                                .addOnSuccessListener { labels ->
                                    //for (label in labels) {} - when multiple labels needed
                                    Toast.makeText(
                                        requireActivity(),
                                        labels[0].text,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireActivity(),
                                        "Error: " + e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            }
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

    private fun getProducts() {
        val search = binding.searchtext.text.toString()
        val products = productService.productInstance.getProduct(search)
        products.enqueue(object : Callback<serpApiResult> {
            override fun onResponse(call: Call<serpApiResult>, response: Response<serpApiResult>) {
                val productlist: serpApiResult? = response.body()
                if (productlist != null) {
                    val productDetailList: Array<productModal> = productlist.shopping_results
                    Log.d("@@@@", productlist.toString())
                    val directions = MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
                    findNavController().navigate(directions)
                }
            }

            override fun onFailure(call: Call<serpApiResult>, t: Throwable) {
                Log.d("@@@@", "Failed sharam ati hai?", t)
            }
        })
    }

}