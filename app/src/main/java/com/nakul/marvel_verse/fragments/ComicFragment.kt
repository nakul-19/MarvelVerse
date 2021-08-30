package com.nakul.marvel_verse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.utils.BackPressedEvent

class ComicFragment : Fragment(), BackPressedEvent {

    companion object {
        private const val NO_FILTER = 0
        private const val THIS_WEEK = 1
        private const val NEXT_WEEK = 2
        private const val LAST_WEEK = 3
        private const val THIS_MONTH = 4
    }

    private lateinit var v: View
    private var filters = MutableLiveData<Int>()
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
        val filter = v.findViewById<LinearLayout>(R.id.filters)

        filter.setOnClickListener {
            if (filterVisible)
                return@setOnClickListener
            filterVisible = true
            filterLayout.visibility = View.VISIBLE
            removeFilter.visibility = View.VISIBLE
        }

        removeFilter.setOnClickListener {
            if (!filterVisible)
                return@setOnClickListener
            filterLayout.visibility = View.GONE
            it.visibility = View.INVISIBLE
            removeFilters()
        }

        fThisWeek.setOnClickListener {
            if (filters.value == THIS_WEEK)
                return@setOnClickListener
            fThisWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
            filters.postValue(THIS_WEEK)
        }

        fNextWeek.setOnClickListener {
            if (filters.value == NEXT_WEEK)
                return@setOnClickListener
            fNextWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
            filters.postValue(NEXT_WEEK)
        }

        fLastWeek.setOnClickListener {
            if (filters.value == LAST_WEEK)
                return@setOnClickListener
            fLastWeek.setBackgroundResource(R.drawable.selected_filter)
            fThisMonth.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
            filters.postValue(LAST_WEEK)
        }

        fThisMonth.setOnClickListener {
            if (filters.value == THIS_MONTH)
                return@setOnClickListener
            fThisMonth.setBackgroundResource(R.drawable.selected_filter)
            fThisWeek.setBackgroundResource(R.drawable.unselected_filter)
            fLastWeek.setBackgroundResource(R.drawable.unselected_filter)
            fNextWeek.setBackgroundResource(R.drawable.unselected_filter)
            filters.postValue(THIS_MONTH)
        }

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
        filters.postValue(NO_FILTER)
    }

}