package com.zubeyirtaruz.quotable.view

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement
import com.huawei.agconnect.cloud.storage.core.ListResult
import com.huawei.hmf.tasks.Task
import com.zubeyirtaruz.quotable.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var userUri: Uri? = null
    private val user = AGConnectAuth.getInstance().currentUser

    private var mAGCStorageManagement: AGCStorageManagement? = null
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private val TAG = "CLOUDSTORAGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCloudStorage(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            val savedUri: Uri? = savedInstanceState.getParcelable("imageUri")
            Glide.with(requireActivity()).load(savedUri).into(binding.userPhotoImageView)
        }
            getImageFromCloudStorage(requireContext())

        binding.cardViewPhoto.setOnClickListener {

            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), permissions, 1)
            }
            else{
                val photo = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photo,2)
            }
        }

        binding.buttonLogout.setOnClickListener {
            AGConnectAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.userUidTextView.text = user.uid

        return binding.root
    }

     private fun initCloudStorage(context: Context){
        val cnOptions = AGConnectOptionsBuilder().setRoutePolicy(AGCRoutePolicy.SINGAPORE).build(context)
        val cnInstance = AGConnectInstance.buildInstance(cnOptions)
        AGConnectInstance.initialize(context)
        mAGCStorageManagement = AGCStorageManagement.getInstance(cnInstance,"userphoto-f2vws")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode==RESULT_OK && data!=null){

            val imageUri = data.data
            Glide.with(requireActivity()).load(imageUri).into(binding.userPhotoImageView)

            if (imageUri != null) {
                saveImageToCloudStorage(imageUri)
            }

        }

    }

    private fun saveImageToCloudStorage(imageUri: Uri){

        val userUid = user.uid
        val path = "/images/${userUid}.png"
        val file = convertImageUriToFile(imageUri,requireActivity())

            val storageReference = mAGCStorageManagement!!.getStorageReference(path)
            val uploadTask = storageReference.putFile(file)

            uploadTask.addOnSuccessListener {
                Log.i(TAG,"UPLOAD Successful")
            }
                .addOnFailureListener { e: Exception ->
                    Log.e(TAG,"UPLOAD Fail: $e")
                }
    }

    private fun getImageFromCloudStorage(context: Context){

        val userUid = user.uid
        val path = "images/"
        val storageReference = mAGCStorageManagement?.getStorageReference(path)
        val listResultTask: Task<ListResult>? = storageReference?.list(100)
        listResultTask?.addOnSuccessListener {

            val fileList = ArrayList(it.fileList)

            for (i in fileList.indices) {
                if (fileList[i].name.equals("${userUid}.png")){

                    fileList[i].downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(context).load(uri).into(binding.userPhotoImageView)
                        userUri = uri
                    }.addOnFailureListener { e ->
                        Log.e(TAG,"FAIL: ${e.printStackTrace()}")
                    }
                }
            }
        }?.addOnFailureListener { e ->
            Log.e(TAG,"FAIL: ${e.printStackTrace()}")
        }
    }

    private fun convertImageUriToFile(imageUri: Uri?, activity: Activity): File? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION
            )
            cursor = activity
                .managedQuery(imageUri, proj, null, null, null)
            val file_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val orientation_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
            if (cursor.moveToFirst()) {
                val orientation: String = cursor.getString(orientation_ColumnIndex)
                return File(cursor.getString(file_ColumnIndex))
            }
            null
        } finally {
            cursor?.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("imageUri", userUri)
    }


}