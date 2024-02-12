package ru.netology.nprofflink.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nprofflink.databinding.FragmentEventsFeedBinding

class EventsFeedFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }
}