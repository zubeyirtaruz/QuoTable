package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.*
import com.zubeyirtaruz.quotable.databinding.FragmentSignupBinding
import com.zubeyirtaruz.quotable.util.editMessage
import java.util.*

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getVerifyCodeTextView.setOnClickListener {
            getVerifyCode(binding.email.text.toString())
        }

        binding.buttonSignup.setOnClickListener {
            if( binding.email.text.toString().isBlank() || binding.password.text.toString().isBlank() ||
                binding.confirmPassword.text.toString().isBlank() || binding.verifyCode.text.toString().isBlank()){

                Toast.makeText(requireContext(),"Please fill in the blanks", Toast.LENGTH_SHORT).show()

            }else if(binding.password.text.toString().equals(binding.confirmPassword.text.toString())){

                registerUser(binding.email.text.toString(),binding.password.text.toString(),binding.verifyCode.text.toString())

            }else{
                Toast.makeText(requireContext(),"Passwords don't match", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(email: String, password: String, verifyCode: String) {

        val emailUser = EmailUser.Builder()
            .setEmail(email)
            .setPassword(password)
            .setVerifyCode(verifyCode)
            .build()

        AGConnectAuth.getInstance().createUser(emailUser)
            .addOnSuccessListener {
                Log.i("Verification", "User successfully created")
                Toast.makeText(requireContext(),"Successfully logged in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            .addOnFailureListener { e ->
                Log.i("Verification", e.message.toString())
                Toast.makeText(requireContext(), editMessage(e.message.toString()), Toast.LENGTH_SHORT).show()
            }

    }


    private fun getVerifyCode(email: String){
            val verifyCodeSettings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30)
                .locale(Locale.getDefault())
                .build()

            val task = EmailAuthProvider.requestVerifyCode(email, verifyCodeSettings)

            task.addOnSuccessListener {
                Log.i("Verification","Sent")
                Toast.makeText(requireContext(),"Verification code sent to your mail", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener { e ->
                Log.i("Verification", e.message.toString())
                Toast.makeText(requireContext(), editMessage(e.message.toString()), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


