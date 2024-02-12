package ru.netology.nprofflink.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.nprofflink.R
import ru.netology.nprofflink.databinding.FragmentMyAccountBinding


class MyAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
}