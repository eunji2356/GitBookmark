package kr.co.chooji.githubapi.model.search

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("total_count") var totalCount: Int = 0,
    @SerializedName("incomplete_results") var incompleteResults: Boolean = false,
    @SerializedName("items") var items: MutableList<SearchUser> = mutableListOf()
)