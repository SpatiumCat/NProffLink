package ru.netology.nprofflink.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.adapter.PostLoadingStateAdapter
import ru.netology.nprofflink.adapters.OnInteractionListener
import ru.netology.nprofflink.adapters.PostAdapter
import ru.netology.nprofflink.databinding.FragmentPostsFeedBinding
import ru.netology.nprofflink.dto.Post
import ru.netology.nprofflink.viewmodel.PostViewModel

class PostsFeedFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsFeedBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostAdapter (object : OnInteractionListener {
            override fun onLike(post: Post) {
            }

            override fun onShare(post: Post) {
            }
        })

        binding.postListRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PostLoadingStateAdapter { adapter.retry() },
            footer = PostLoadingStateAdapter { adapter.retry() },
        )

        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

//        viewModel.data.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }


        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefresh.isRefreshing =
                    it.refresh is LoadState.Loading
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        return binding.root
    }
}