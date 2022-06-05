package glebova.rsue.countwater

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
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
import id.zelory.compressor.constraint.default
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
        binding.buttonSend.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                response = send()
            }
            while (response == "") {
                continue
            }
            response = ""
            findNavController().navigate(BlankFragmentDirections.actionBlankFragmentToCountFragment())
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
        Log.d("size", photoFile.length().toString())

        GlobalScope.launch(Dispatchers.IO) {
            val compressedImageFile = Compressor.compress(requireContext(), file) {
                default(width = 640, format = Bitmap.CompressFormat.WEBP)
            }
            Log.d("size", compressedImageFile.length().toString())
            encodedString = Base64.getEncoder().encodeToString(file.readBytes())
            run()
        }
    }

    private fun onGetImage(file: File, uri: Uri) {
        val f = File(file.absolutePath)
        encodedString = Base64.getEncoder().encodeToString(f.readBytes())
        GlobalScope.launch(Dispatchers.IO) {
            response = run()
            while (response == "") {
                continue
            }
            binding.editTextNumber.setText(response)
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
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            Log.d("JSON", result)
            Log.d("JSON", JSONObject(result).getString("555"))
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
            if (!response.isSuccessful) {
                return "Exception"
            }
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