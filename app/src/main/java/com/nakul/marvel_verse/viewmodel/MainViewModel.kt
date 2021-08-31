package com.nakul.marvel_verse.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.ProcessState
import com.nakul.marvel_verse.models.ResponseWrapper

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val characterResponse = MutableLiveData<ResponseWrapper<CharacterModel>>()

    val characterList = MutableLiveData<ProcessState<ArrayList<CharacterModel>>>()

    //to store originally loaded list before search
    private val unChangedCharacterList = ArrayList<CharacterModel>()
    private val searchedCharacterList = ArrayList<CharacterModel>()

    private var search = false
    private var name = "A"

    fun searching(_name:String) {
        search = true
        name = _name
    }

    fun searchStopped() {
        search = false
        name = ""
        characterList.postValue(ProcessState.Success(unChangedCharacterList))
    }

    fun loadCharacters() {

    }

    private val comicResponse = MutableLiveData<ResponseWrapper<ComicModel>>()
    val comicList = MutableLiveData<ProcessState<ArrayList<ComicModel>>>()
    private val unFilteredList = ArrayList<ComicModel>()

    fun loadComics() {

    }

    fun filterComics(type: Int) {

    }
}