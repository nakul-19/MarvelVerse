package com.nakul.marvel_verse.dataSource

import android.util.Log
import com.nakul.marvel_verse.BuildConfig
import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.ResponseWrapper
import com.nakul.marvel_verse.network.MarvelApi
import com.nakul.marvel_verse.network.Retrofit
import com.nakul.marvel_verse.utils.SafeApiRequest
import com.nakul.marvel_verse.utils.getTs
import com.nakul.marvel_verse.utils.md5Hash

class NetworkRepository: SafeApiRequest() {

    companion object{
        private val retrofit = Retrofit.getClient().create(MarvelApi::class.java)
    }

    suspend fun getCharacters(offset: Int):ResponseWrapper<CharacterModel> {
        val ts = getTs()
        Log.d("Character",BuildConfig.PRIVATE_KEY+" "+BuildConfig.API_KEY)
        val hash = md5Hash(ts+BuildConfig.PRIVATE_KEY+BuildConfig.API_KEY)
        return apiRequest { retrofit.getCharacters(BuildConfig.API_KEY,hash,ts,offset) }
    }

    suspend fun getCharactersByName(offset: Int, query: String):ResponseWrapper<CharacterModel> {
        val ts = getTs()
        val hash = md5Hash(ts+BuildConfig.PRIVATE_KEY+BuildConfig.API_KEY)
        return apiRequest { retrofit.getCharactersByName(BuildConfig.API_KEY,hash,ts,query,offset) }
    }

    suspend fun getComics():ResponseWrapper<ComicModel> {
        val ts = getTs()
        val hash = md5Hash(ts+BuildConfig.PRIVATE_KEY+BuildConfig.API_KEY)
        return apiRequest { retrofit.getComics(BuildConfig.API_KEY,hash,ts) }
    }
}
