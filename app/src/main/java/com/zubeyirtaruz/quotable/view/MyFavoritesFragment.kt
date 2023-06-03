package com.zubeyirtaruz.quotable.view

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zubeyirtaruz.quotable.adapter.FavoriteAdapter
import com.zubeyirtaruz.quotable.databinding.FragmentMyFavoritesBinding
import com.zubeyirtaruz.quotable.model.FavoriteQuote
import com.zubeyirtaruz.quotable.service.CloudDBZoneWrapper

class MyFavoritesFragment : Fragment(), CloudDBZoneWrapper.UiCallBack  {

    private var _binding: FragmentMyFavoritesBinding? = null
    private val binding get() = _binding!!

    private val mHandler = MyHandler()
    private var mCloudDBZoneWrapper: CloudDBZoneWrapper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyFavoritesBinding.inflate(inflater, container, false)

        mCloudDBZoneWrapper = CloudDBZoneWrapper()
        mHandler.post {
            mCloudDBZoneWrapper?.addCallBacks(this)
            mCloudDBZoneWrapper?.addSubscription()
        }

        return  binding.root
    }

    override fun onSubscribe(userInfoList: List<FavoriteQuote>?) {
        mHandler.post {
            if (activity != null){
                val userInfoRecyclerViewAdapter = FavoriteAdapter(requireActivity(),
                    userInfoList as MutableList<FavoriteQuote>
                )
                binding.frv.layoutManager = LinearLayoutManager(activity)
                binding.frv.adapter = userInfoRecyclerViewAdapter
                Log.w("onSubscribeUserList", "onSubscribeUserList")
            }
        }
    }

    override fun onAddOrQuery(userInfoList: List<FavoriteQuote>) {
    }
    override fun onDelete(userInfoList: List<FavoriteQuote>?) {
    }
    override fun updateUiOnError(errorMessage: String?) {
    }
    class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}