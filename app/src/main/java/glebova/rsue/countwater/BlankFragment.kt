package glebova.rsue.countwater

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentBlankBinding
import glebova.rsue.countwater.databinding.FragmentNavContainerBinding
import glebova.rsue.countwater.dialogs.ChoosePhotoDialog
import glebova.rsue.countwater.extensions.getFileName
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class BlankFragment : BaseFragment<FragmentBlankBinding>() {

    override fun initializeBinding() = FragmentBlankBinding.inflate(layoutInflater)

    private lateinit var photoFile: File

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
        setupListeners()
    }

    private fun setupListeners() {
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun onImageTake() {
        val file = File(photoFile.absolutePath)
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        Log.d("size", photoFile.length().toString())
        binding.viewImg.setImageBitmap(bitmap)

        GlobalScope.launch(Dispatchers.IO) {
            val compressedImageFile = Compressor.compress(requireContext(), file) {
                default(width = 640, format = Bitmap.CompressFormat.WEBP)
            }
            Log.d("size", compressedImageFile.length().toString())
            val encodedString: String = Base64.getEncoder().encodeToString(file.readBytes())
            val client = OkHttpClient()
        }
    }

    private fun onGetImage(file: File, uri: Uri) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        binding.viewImg.setImageBitmap(bitmap)
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