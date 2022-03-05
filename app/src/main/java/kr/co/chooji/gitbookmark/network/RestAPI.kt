package kr.co.chooji.gitbookmark.network

import io.reactivex.Single
import kr.co.chooji.githubapi.model.search.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAPI {
    @GET("search/users")
    fun getSearchUser(@Query("q") q: String,
                      @Query("page") page: Int,
                      @Query("per_page") perPage:Int): Single<Search>
}