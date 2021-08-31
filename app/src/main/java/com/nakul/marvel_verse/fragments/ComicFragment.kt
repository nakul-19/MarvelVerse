package com.nakul.marvel_verse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.adapters.ComicAdapter
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.ProcessState
import com.nakul.marvel_verse.utils.*
import com.nakul.marvel_verse.viewmodel.MainViewModel

class ComicFragment : Fragment(), BackPressedEvent {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var v: View
    private val list = ArrayList<ComicModel>()
    private lateinit var loader: ImageView
    private lateinit var cText: TextView
    private lateinit var comicAdapter: ComicAdapter
    private var filterVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_comic, container, false)
        initUI()
        return v
    }

    private fun initUI() {
        val filterLayout = v.findViewById<Group>(R.id.filter_layout)
        val fThisWeek = v.findViewById<TextView>(R.id.f_this_week)
        val fLastWeek = v.findViewById<TextView>(R.id.f_last_week)
        val fNextWeek = v.findViewById<TextView>(R.id.f_next_week)
        val fThisMonth = v.findViewById<TextView>(R.id.f_this_month)
        val removeFilter = v.findViewById<TextView>(R.id.remove_filters)
        cText = v.findViewById(R.id.comic_text)
        val filter = v.findViewById<LinearLayout>(R.id.filters)
        loader = v.findViewById(R.id.loader)
        Glide.with(requireContext()).load(R.raw.loading_gif).into(loader)
        val recyclerView = v.findViewById<RecyclerView>(R.id.comic_recycler)

        comicAdapter = ComicAdapter(list)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = comicAdapter
        }

        viewModel.comicList.observe(viewLifecycleOwner) {
            when (it) {
                is ProcessState.Success -> {
                    stopLoading()
                    list.clear()
                    list.addAll(it.res)
                    activity?.runOnUiThread {
                        comicAdapter.notifyDataSetChanged()
                        if (list.isEmpty()) {
                            cText.visibility = View.VISIBLE
                        }
                    }
                }
                is ProcessState.Loading -> {
                    list.clear()
                    activity?.runOnUiThread { comicAdapter.notifyDataSetChanged() }
                    loading()
                }
                is ProcessState.Failure -> {
                    Snackbar.make(v, it.message, Snackbar.LENGTH_INDEFINITE).setAction("Retry") {
                        viewModel.loadComics()
                        removeFilters()
                    }.show()
                }

            }
        }

        filter.setOnClickListener {
            filterVisible = true
            filterLayout.visibility = View.VISIBLE
            removeFilter.visibility = View.VISIBLE
        }

        removeFilter.setOnClickListener {
            filterLayout.visibility = View.GONE
            it.visibility = View.INVISIBLE
            removeFilters()
        }

        fThisWeek.setOnClickListener {
            viewModel.filterComics(THIS_WEEK)
            fThisWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
        }

        fNextWeek.setOnClickListener {
            viewModel.filterComics(NEXT_WEEK)
            fNextWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
        }

        fLastWeek.setOnClickListener {
            viewModel.filterComics(LAST_WEEK)
            fLastWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
        }

        fThisMonth.setOnClickListener {
            viewModel.filterComics(THIS_MONTH)
            fThisMonth.setBackgroundResource(R.drawable.selected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
        }

    }

    private fun stopLoading() {
        loader.visibility = View.GONE
    }

    private fun loading() {
        cText.visibility = View.GONE
        loader.visibility = View.VISIBLE
    }

    override fun onBackPressed(): Boolean {

        return if (!filterVisible) {
            true
        } else {
            removeFilters()
            false
        }

    }

    private fun removeFilters() {
        filterVisible = false
        v.findViewById<Group>(R.id.filter_layout).visibility = View.GONE
        v.findViewById<TextView>(R.id.remove_filters).visibility = View.INVISIBLE
        v.findViewById<TextView>(R.id.f_this_week)
            .setBackgroundResource(R.drawable.unselected_filter)
        v.findViewById<TextView>(R.id.f_last_week)
            .setBackgroundResource(R.drawable.unselected_filter)
        v.findViewById<TextView>(R.id.f_next_week)
            .setBackgroundResource(R.drawable.unselected_filter)
        v.findViewById<TextView>(R.id.f_this_month)
            .setBackgroundResource(R.drawable.unselected_filter)
        viewModel.filterComics(NO_FILTER)
    }

}