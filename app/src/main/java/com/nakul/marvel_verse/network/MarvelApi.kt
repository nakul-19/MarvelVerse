package com.nakul.marvel_verse.network

import com.nakul.marvel_verse.models.CharacterModel
import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.ResponseWrapper
import com.nakul.marvel_verse.utils.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET(characters)
    suspend fun getCharacters(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query(resultOffset) offset: Int,
        @Query(resultLimit) limit: Int = character_page_size
    ): Response<ResponseWrapper<CharacterModel>>

    @GET(characters)
    suspend fun getCharactersByName(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query(searchedName) name: String,
        @Query(resultOffset) offset: Int,
        @Query(resultLimit) limit: Int = character_page_size
    ): Response<ResponseWrapper<CharacterModel>>

    @GET(comics)
    suspend fun getComics(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query(resultLimit) limit: Int = comic_page_size,
        @Query(resultOffset) offset: Int = comic_offset,
        @Query(order_by) orderBy: String = comic_order_by
    ): Response<ResponseWrapper<ComicModel>>
}