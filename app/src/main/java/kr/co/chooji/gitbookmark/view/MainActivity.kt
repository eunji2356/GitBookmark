package kr.co.chooji.gitbookmark.view

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.chooji.gitbookmark.adapter.SearchAdapter
import kr.co.chooji.gitbookmark.databinding.ActivityMainBinding
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
    }

    private fun observerViewModel(){
        mainViewModel.userList.observe(this, { list ->
            adapter.updateUserList(page, list)
        })
    }
}