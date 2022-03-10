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

    private fun initView(){
        binding.searchBtn.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)

            page = 1
            search = binding.searchEditText.text.toString()
            mainViewModel.getSearchUser(search, page)

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
                        setVisibility(0)
                    }
                    1 -> {
                        setVisibility(1)
                        mainViewModel.getBookMark()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        adapter.setClickListener {
            if(DBAdapter.selectUser(it.id)){
                DBAdapter.deleteUserBookmark(it.id)
            }
            else{
                DBAdapter.updateUserBookmark(it)
            }
        }
    }

    private fun observerViewModel(){
        mainViewModel.userList.observe(this, { list ->
            adapter.updateUserList(page, list)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun tabClickEvent(){
        adapter.apply {
            list.clear()
            notifyDataSetChanged()
        }
        binding.searchEditText.setText("")
    }

    fun setVisibility(tabPosition: Int){
        if(tabPosition == 0){
            binding.searchBtn.visibility = View.VISIBLE
            binding.searchEditText.visibility = View.VISIBLE
            binding.searchBottomView.visibility = View.VISIBLE
        }
        else{
            binding.searchBtn.visibility = View.GONE
            binding.searchEditText.visibility = View.GONE
            binding.searchBottomView.visibility = View.GONE
        }
        tabClickEvent()
    }
}