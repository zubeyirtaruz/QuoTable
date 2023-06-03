package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.huawei.secure.android.common.intent.SafeIntent
import com.zubeyirtaruz.quotable.databinding.ActivityVisionBinding

class VisionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cropImageView.setImageBitmap(takeIntent())

        binding.btnCropImage.setOnClickListener {
            sendCroppedImage(binding.cropImageView.croppedImage)
        }

        binding.btnRotate.setOnClickListener {
            binding.cropImageView.rotateClockwise()
        }
    }

    private fun takeIntent(): Bitmap? {
        val intent: Intent = SafeIntent(intent)
        return intent.getParcelableExtra<Parcelable>("bitmap") as Bitmap?
    }

    private fun sendCroppedImage(croppedImage: Bitmap){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("croppedImage", croppedImage)
        startActivity(intent)
        finish()
    }

}