package kr.co.chooji.gitbookmark.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kr.co.chooji.gitbookmark.adapter.SearchAdapter
import kr.co.chooji.gitbookmark.databinding.ActivityMainBinding
import kr.co.chooji.gitbookmark.db.DBAdapter
import kr.co.chooji.gitbookmark.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var tabPosition: Int = 0
    private var page: Int = 1
    var search: String = ""

    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        DBAdapter.init(this)
        initView()
        observerViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(){
        binding.searchBtn.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)

            page = 1
            search = binding.searchEditText.text.toString()
            if(tabPosition == 0){
                mainViewModel.getSearchUser(search, page)
            }
            else{
                mainViewModel.getBookMark(search)
            }

            val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = this.adapter
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        tabClickEvent(0)
                    }
                    1 -> {
                        tabClickEvent(1)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observerViewModel(){
        mainViewModel.userList.observe(this, { list ->
            if(list.size == 0){
                binding.notExistResult.visibility = View.VISIBLE
            }
            else{
                binding.notExistResult.visibility = View.GONE
            }
            adapter.updateUserList(page, list)
        })
    }

    /**
     * tab 변경 시, 사용자 목록 및 검색 박스 초기화
     * */
    @SuppressLint("NotifyDataSetChanged")
    fun tabClickEvent(tabPosition: Int){
        binding.notExistResult.visibility = View.VISIBLE

        adapter.apply {
            list.clear()
            notifyDataSetChanged()
        }
        binding.searchEditText.setText("")
        this.tabPosition = tabPosition
    }
}