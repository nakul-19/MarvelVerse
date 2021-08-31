package com.nakul.marvel_verse.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.adapters.CharacterAdapter
import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.utils.BackPressedEvent

class CharacterFragment : Fragment(), BackPressedEvent {

    private lateinit var v: View
    private lateinit var searchView: SearchView
    private lateinit var loader: ImageView
    private lateinit var recyclerView: RecyclerView
    private val list = ArrayList<CharacterModel>()
    private val fList = ArrayList<CharacterModel>()
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
                Toast.makeText(context, "Searched", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        characterAdapter = CharacterAdapter(fList)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addOnScrollListener(scrollListener)
            adapter = characterAdapter
        }
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
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isScrolling

            if (shouldPaginate) {
                loadNextPage()
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

    private fun loadNextPage() {
        loading()
        //Add data here
        stopLoading()
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
            false
        } else
            true
    }

}