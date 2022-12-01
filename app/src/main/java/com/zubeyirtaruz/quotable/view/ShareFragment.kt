package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zubeyirtaruz.quotable.databinding.FragmentShareBinding
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShareFragment : Fragment() {

    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!

    val viewModel: QuoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShareBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner



        binding.shareButton.setOnClickListener {
            onShareButtonClick(binding)
        }

        return binding.root
    }

    private  fun onShareButtonClick(binding: FragmentShareBinding){
        val bitmap = binding.quoteLayout.drawToBitmap()

        try {
            val file = bitmap.savetoFile()
            file.share()
        }catch (ex: IOException){
            ex.printStackTrace()
            Toast.makeText(requireContext(), com.zubeyirtaruz.quotable.R.string.saveError, Toast.LENGTH_LONG).show()
        }
    }

    private fun Bitmap.savetoFile(): File {

            //Create a file to write bitmap data
            val path = requireContext().externalCacheDir.toString() + File.separator + "quote.png"
            val file = File(path)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()

            //Write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            return file

    }

    private fun File.share(){
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            this
        )
        val shareIntent: Intent = Intent().apply {
            type = "image/png"
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}