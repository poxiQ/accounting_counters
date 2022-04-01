package glebova.rsue.countwater.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import glebova.rsue.countwater.databinding.DialogChosePhotoBinding

class ChoosePhotoDialog : BottomSheetDialogFragment() {

    interface ChoosePhotoDialogCallback {
        fun onMakePhotoClick()
        fun onChoosePhotoClick()
    }


    private lateinit var binding: DialogChosePhotoBinding
    var callback: ChoosePhotoDialogCallback? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChosePhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonMakePhoto.setOnClickListener {
            callback?.onMakePhotoClick()
            dismiss()
        }
        binding.buttonChooseWithGallery.setOnClickListener {
            callback?.onChoosePhotoClick()
            dismiss()
        }
    }
}