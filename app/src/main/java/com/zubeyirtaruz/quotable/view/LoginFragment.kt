package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.zubeyirtaruz.quotable.databinding.FragmentLoginBinding
import com.zubeyirtaruz.quotable.util.editMessage

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.emailLayout.alpha = 0f
        binding.passwordLayout.alpha = 0f
        binding.buttonLogin.alpha = 0f
        binding.buttonHuaweiId.alpha = 0f

        binding.emailLayout.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        binding.passwordLayout.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(400).start()
        binding.buttonLogin.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        binding.buttonHuaweiId.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(700).start()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonHuaweiId.setOnClickListener {
            val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAccessToken().createParams()
            val service = HuaweiIdAuthManager.getService(requireActivity(), authParams)
            startActivityForResult(service.signInIntent, 1003)
        }

        binding.buttonLogin.setOnClickListener {
            if(binding.email.text.toString().isBlank() || binding.password.text.toString().isBlank()){
                Toast.makeText(requireContext(),"Please fill in the blanks",Toast.LENGTH_SHORT).show()
            }else{
                clickButtonLogin(binding.email.text.toString(),binding.password.text.toString())
            }
        }

    }

    private fun clickButtonLogin(email: String, password: String){

        val credential = EmailAuthProvider.credentialWithPassword(email, password)

        AGConnectAuth.getInstance().signIn(credential)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Successfully logged in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), editMessage(e.message.toString()), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1003) {
            val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)

            if (authHuaweiIdTask.isSuccessful) {

                val huaweiAccount = authHuaweiIdTask.result

                val accessToken = huaweiAccount.accessToken
                val credential = HwIdAuthProvider.credentialWithToken(accessToken)
                AGConnectAuth.getInstance().signIn(credential)
                    .addOnSuccessListener { signInResult ->

                        Toast.makeText(requireContext(),"Successfully logged in", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))

                    }.addOnFailureListener { e ->
                        Toast.makeText(requireContext(), editMessage(e.message.toString()), Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(requireContext(), "Huawei ID signIn failed: " + authHuaweiIdTask.exception.message?.let {
                    editMessage(
                        it
                    )
                }, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}