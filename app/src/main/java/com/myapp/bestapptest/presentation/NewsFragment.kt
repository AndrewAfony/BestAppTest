package com.myapp.bestapptest.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapp.bestapptest.adapters.NewsAdapter
import com.myapp.bestapptest.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: NewsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            viewModel.news.collect { news ->
                newsAdapter.differ.submitList(news.sortedByDescending { it.publishedAt })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collectLatest {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            filterNews.addTextChangedListener { query ->
                viewModel.onFilter(query.toString())
            }

            filterNews.setOnEditorActionListener { v, actionId, event ->
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                        v.clearFocus()
                        true
                    }
                    else -> false
                }
            }

            swipeLayout.setOnRefreshListener {
                viewModel.getNews()
                binding.swipeLayout.isRefreshing = false
            }
        }

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter {
            val action = NewsFragmentDirections.navigateToDetail(it)
            view?.findNavController()?.navigate(action)
        }
        val divider = DividerItemDecoration(activity, RecyclerView.VERTICAL)
        binding.rvNews.apply {
            adapter = newsAdapter
            addItemDecoration(divider)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}