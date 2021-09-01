package com.nakul.marvel_verse.viewmodel

import android.app.Application
import android.util.EventLog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nakul.marvel_verse.dataSource.NetworkRepository
import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.ProcessState
import com.nakul.marvel_verse.models.ResponseWrapper
import com.nakul.marvel_verse.utils.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NetworkRepository()


    val characterList = MutableLiveData<ProcessState<ArrayList<CharacterModel>>>()

    //to store originally loaded list before search
    private val unChangedCharacterList = ArrayList<CharacterModel>()
    private val searchedCharacterList = ArrayList<CharacterModel>()

    private var search = false
    private var name = ""

    fun searching(_name: String) {
        Log.d("CharacterFragment","Searching")
        search = true
        name = _name
        searchedCharacterList.clear()
        characterList.postValue(ProcessState.Success(searchedCharacterList))
        loadCharacters()
    }

    fun searchStopped() {
        Log.d("CharacterFragment","Searched")
        search = false
        name = ""
        characterList.postValue(ProcessState.Success(unChangedCharacterList))
    }

    fun loadCharacters() {
        characterList.postValue(ProcessState.Loading())
        viewModelScope.launch {
            try {
                if (!search) {
                    val result = repository.getCharacters(unChangedCharacterList.size)
                    handleCharacterWrapper(result)
                } else {
                    val result = repository.getCharactersByName(searchedCharacterList.size, name)
                    handleCharacterWrapper(result)
                }
            } catch (e: Exception) {
                Log.d("CharacterResult",e.message.toString())
                if (e is ApiException)
                    characterList.postValue(ProcessState.Failure(e.message.toString()))
                else
                    characterList.postValue(ProcessState.Failure("No Internet."))
            }
        }
    }

    private fun handleCharacterWrapper(wrapper: ResponseWrapper<CharacterModel>) {
        val arrayList = wrapper.data.results
        if (search) {
            searchedCharacterList.addAll(arrayList)
            characterList.postValue(ProcessState.Success(searchedCharacterList))
        } else {
            unChangedCharacterList.addAll(arrayList)
            characterList.postValue(ProcessState.Success(unChangedCharacterList))
        }
    }

    private fun handleComicWrapper(wrapper: ResponseWrapper<ComicModel>) {
        val arrayList = wrapper.data.results
        unFilteredList.clear()
        unFilteredList.addAll(arrayList)
        if (filter == NO_FILTER)
            comicList.postValue(ProcessState.Success(unFilteredList))
        else
            filterComics(filter)
    }

    val comicList = MutableLiveData<ProcessState<ArrayList<ComicModel>>>()
    private val unFilteredList = ArrayList<ComicModel>()
    private val filteredList = ArrayList<ComicModel>()
    var filter: Int = NO_FILTER

    fun loadComics() {
        comicList.postValue(ProcessState.Loading())
        viewModelScope.launch {
            try {
                val result = repository.getComics()
                handleComicWrapper(result)
            } catch (e: Exception) {
                Log.d("ComicResult",e.message.toString())
                if (e is ApiException)
                    comicList.postValue(ProcessState.Failure(e.message.toString()))
                else
                    comicList.postValue(ProcessState.Failure("No Internet."))
            }
        }
    }

    fun filterComics(type: Int) {
        filter = type
        when(type) {
            NO_FILTER -> {
                comicList.postValue(ProcessState.Success(unFilteredList))
                return
            }
            THIS_WEEK -> {
                filteredList.clear()
                for (i in unFilteredList)
                    if (inThisWeek(i.getDate()))
                        filteredList.add(i)
            }
            LAST_WEEK -> {
                filteredList.clear()
                for (i in unFilteredList)
                    if (inLastWeek(i.getDate()))
                        filteredList.add(i)
            }
            NEXT_WEEK -> {
                filteredList.clear()
                for (i in unFilteredList)
                    if (inNextWeek(i.getDate()))
                        filteredList.add(i)
            }
            THIS_MONTH -> {
                filteredList.clear()
                for (i in unFilteredList)
                    if (inThisMonth(i.getDate()))
                        filteredList.add(i)
            }
        }
        comicList.postValue(ProcessState.Success(filteredList))
    }
}