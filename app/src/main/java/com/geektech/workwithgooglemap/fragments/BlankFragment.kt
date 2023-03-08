package com.geektech.workwithgooglemap.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.data.remote.repositories.UserTestRepository
import com.geektech.workwithgooglemap.data.remote.repositories.Users
import com.geektech.workwithgooglemap.databinding.FragmentBlankBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BlankFragment : Fragment(R.layout.fragment_blank) {

    private val binding by viewBinding(FragmentBlankBinding::bind)
    private val viewModel: BlankViewModel by viewModels()
    @Inject
    lateinit var repository: UserTestRepository
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) {
            Log.d("TAGGER", "Users: $it ")
        }
    }
}