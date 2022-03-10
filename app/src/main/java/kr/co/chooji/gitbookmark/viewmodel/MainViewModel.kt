package kr.co.chooji.gitbookmark.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.co.chooji.gitbookmark.db.DBAdapter
import kr.co.chooji.gitbookmark.network.RetrofitService
import kr.co.chooji.githubapi.model.search.SearchUser

class MainViewModel: ViewModel() {

    companion object{
        private const val PER_PAGE = 100
        private val getAPI  = RetrofitService.getGithubAPI()
    }

    private var disposable: CompositeDisposable = CompositeDisposable()

    val userList = MutableLiveData<MutableList<SearchUser>>()

    fun getSearchUser(search:String, page: Int){
        disposable.add(getAPI.getSearchUser(search, page, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                userList.value = res.items
            }, { e ->
                e.printStackTrace()
            })
        )
    }

    fun getBookMark(){
        userList.value = DBAdapter.selectBookMark()
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}