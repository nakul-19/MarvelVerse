package com.nakul.marvel_verse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.adapters.CharacterAdapter
import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.models.ProcessState
import com.nakul.marvel_verse.utils.BackPressedEvent
import com.nakul.marvel_verse.viewmodel.MainViewModel

class CharacterFragment : Fragment(), BackPressedEvent {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var v: View
    private lateinit var searchView: SearchView
    private lateinit var loader: ImageView
    private lateinit var recyclerView: RecyclerView
    private val list = ArrayList<CharacterModel>()
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_character, container, false)
        initUI()
        return v
    }

    private fun initUI() {
        searchView = v.findViewById(R.id.searchView)
        loader = v.findViewById(R.id.loader)
        recyclerView = v.findViewById(R.id.character_recycler)
        Glide.with(requireContext()).load(R.raw.loading_gif).into(loader)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searching(query)
                    viewModel.loadCharacters()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        characterAdapter = CharacterAdapter(list)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addOnScrollListener(scrollListener)
            adapter = characterAdapter
        }

        viewModel.characterList.observe(viewLifecycleOwner, {
            when (it) {
                is ProcessState.Success -> {
                    if (list.size == it.res.size) {
                        isLastPage = true
                        stopLoading()
                        return@observe
                    }
                    list.clear()
                    list.addAll(it.res)
                    characterAdapter.notifyDataSetChanged()
                }
                is ProcessState.Loading -> {
                    loading()
                }
                is ProcessState.Failure -> {
                    Snackbar.make(v, it.message, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        loadData()
                    }.show()
                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isScrolling

            if (shouldPaginate) {
                loadData()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun loadData() {
        viewModel.loadCharacters()
    }

    private fun loading() = activity?.runOnUiThread {
        isLoading = true
        recyclerView.setPadding(0, 0, 0, 60)
        loader.visibility = View.VISIBLE
    }

    private fun stopLoading() = activity?.runOnUiThread {
        isLoading = false
        recyclerView.setPadding(0, 0, 0, 0)
        loader.visibility = View.INVISIBLE
    }

    override fun onBackPressed(): Boolean {

        return if (!searchView.isIconified) {
            searchView.setQuery(null, false)
            searchView.isIconified = true
            viewModel.searchStopped()
            false
        } else
            true
    }

}