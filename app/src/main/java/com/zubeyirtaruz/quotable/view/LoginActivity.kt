package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.agconnect.auth.AGConnectAuth
import com.zubeyirtaruz.quotable.adapter.ViewPagerFragmentAdapter
import com.zubeyirtaruz.quotable.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val fragmentTitleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AGConnectAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }else{

            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            fragmentTitleList.add("LOG IN")
            fragmentTitleList.add("SIGN UP")

            val adapter = ViewPagerFragmentAdapter(this)
            adapter.addFragment(LoginFragment())
            adapter.addFragment(SignupFragment())
            binding.viewPager2.setAdapter(adapter)

            TabLayoutMediator(binding.tabLayout, binding.viewPager2) {
                    tab: TabLayout.Tab, position: Int ->
                tab.text = fragmentTitleList[position]
            }.attach()

            binding.tabLayout.translationY = 300F
            binding.tabLayout.alpha = 0f
            binding.tabLayout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(100).start()

        }

    }

}