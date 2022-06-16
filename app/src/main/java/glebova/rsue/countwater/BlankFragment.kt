package glebova.rsue.countwater

import android.graphics.Bitmap
import android.net.Uri
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentBlankBinding
import glebova.rsue.countwater.dialogs.ChoosePhotoDialog
import glebova.rsue.countwater.extensions.getFileName
import glebova.rsue.countwater.ui.response
import glebova.rsue.countwater.ui.token
import glebova.rsue.countwater.ui.url
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@DelicateCoroutinesApi
class BlankFragment : BaseFragment<FragmentBlankBinding>() {

    override fun initializeBinding() = FragmentBlankBinding.inflate(layoutInflater)

    private lateinit var photoFile: File
    private lateinit var encodedString: String
    private val client = OkHttpClient()

    private val openGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val parcelFileDescriptor = requireContext().contentResolver
                    ?.openFileDescriptor(uri, "r", null)

                parcelFileDescriptor?.let { _ ->
                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val file = File(
                        requireContext().cacheDir,
                        requireContext().contentResolver.getFileName(uri)
                    )
                    val outputStream = FileOutputStream(file)
                    IOUtils.copy(inputStream, outputStream)
                    onGetImage(file = file, uri = uri)
                }
            }
        }

    private val openCameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        onImageTake()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numberView.text = arguments?.getString("count")!!
        setupListeners()
    }

    private fun setupListeners() {
        binding.arrow.setOnClickListener {
            findNavController().navigate(BlankFragmentDirections.actionBlankFragmentToCountFragment())
        }
        binding.buttonSend.setOnClickListener {
            if (binding.editTextNumber.text.length == 5) {
                GlobalScope.launch(Dispatchers.IO) { response = send() }
                while (response == "") { continue }
                response = ""
                findNavController().navigate(BlankFragmentDirections.actionBlankFragmentToCountFragment())
            } else {
                Toast.makeText(activity?.applicationContext, "некорректрое значение", Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonSkan.setOnClickListener {
            ChoosePhotoDialog().let {
                it.callback = object : ChoosePhotoDialog.ChoosePhotoDialogCallback {
                    override fun onChoosePhotoClick() {
                        openGalleryLauncher.launch("image/*")
                    }

                    override fun onMakePhotoClick() {
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            ?.let { file ->
                                createImageFile(file).let { tempFile ->
                                    photoFile = tempFile
                                    openCameraLauncher.launch(
                                        FileProvider.getUriForFile(
                                            requireContext(),
                                            requireContext().packageName + ".provider",
                                            tempFile
                                        )
                                    )
                                }
                            }
                    }
                }
                it.show(childFragmentManager, ChoosePhotoDialog::class.simpleName)
            }
        }
    }


    private fun onImageTake() {
        val file = File(photoFile.absolutePath)
        GlobalScope.launch(Dispatchers.IO) {
            val compressedImageFile = Compressor.compress(requireContext(), file){
                resolution(480, 640)
                format(Bitmap.CompressFormat.WEBP)
                size(1_048_576)
            }
            Log.d("__________", file.length().toString())
            Log.d("___________-", compressedImageFile.length().toString())
            encodedString = if (file.length() >100000)
                Base64.getEncoder().encodeToString(compressedImageFile.readBytes())
            else Base64.getEncoder().encodeToString(file.readBytes())
            Log.d("___________-", encodedString.length.toString())
            GlobalScope.launch(Dispatchers.IO) {
                response = run()
                while (response == "") { continue }
                Log.d("_______________", response)

                if (response != "Exception") {
                    with(binding) {
                        editTextNumber.text.clear()
                        editTextNumber.setText(response)
                    }
                }else{
                    Toast.makeText(activity?.applicationContext, "проверьте подключение к сети и повторите вход", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onGetImage(file: File, uri: Uri) {
        val f = File(file.absolutePath)
        GlobalScope.launch(Dispatchers.IO) {
            val compressedImageFile = Compressor.compress(requireContext(), f){
                resolution(480, 640)
                format(Bitmap.CompressFormat.WEBP)
                size(1_048_576)
            }
            Log.d("__________", f.length().toString())
            Log.d("___________-", compressedImageFile.length().toString())
            encodedString = if (f.length() >100000)
                Base64.getEncoder().encodeToString(compressedImageFile.readBytes())
            else Base64.getEncoder().encodeToString(f.readBytes())
            Log.d("___________-", encodedString.length.toString())
            GlobalScope.launch(Dispatchers.IO) {
                response = run()
                while (response == "") { continue }
                if (response != "Exception") {
                    if (binding.editTextNumber.text.isNotEmpty()) binding.editTextNumber.clearComposingText()
                    binding.editTextNumber.setText(response)
                }
            }
        }
    }

    private fun run(): String {
        val formBody = FormBody.Builder()
            .add("search", encodedString)
            .build()
        val request = Request.Builder()
            .url(url + "/water/sendphoto")
            .addHeader("Authorization", "Token $token")
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) { return "Exception"}
            val result = response.body!!.string()
            return JSONObject(result).getString("555")
        }
    }

    private fun send(): String {
        val formBody = FormBody.Builder()
            .add("id_counter", binding.numberView.text.toString())
            .add("value", binding.editTextNumber.text.toString())
            .build()
        val request = Request.Builder()
            .url(url + "/water/meterdata/" + binding.numberView.text.toString())
            .addHeader("Authorization", "Token $token")
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) { return "Exception" }
            val result = response.body!!.string()
            return result
        }
    }

    private fun createImageFile(directory: File): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            directory
        )
    }
}