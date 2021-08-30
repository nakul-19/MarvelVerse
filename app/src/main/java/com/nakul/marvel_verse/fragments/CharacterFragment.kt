package com.nakul.marvel_verse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.nakul.marvel_verse.R
import com.nakul.marvel_verse.utils.BackPressedEvent

class CharacterFragment : Fragment(), BackPressedEvent {

    private lateinit var v: View
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_character, container, false)
        initUI()
        return v
    }

    private fun initUI() {
        searchView = v.findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Searched", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
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