package kr.co.chooji.gitbookmark.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.chooji.gitbookmark.R
import kr.co.chooji.gitbookmark.databinding.ItemUserBinding
import kr.co.chooji.gitbookmark.db.DBAdapter
import kr.co.chooji.githubapi.model.search.SearchUser

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.Holder>() {

    val list: MutableList<SearchUser> = mutableListOf()
    private var userNameList = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateUserList(page: Int, userList: MutableList<SearchUser>){
        if(page == 1) {
            list.clear()
        }
        list.addAll(userList)
        list.sortBy { it.login }

        showHeader()
        notifyDataSetChanged()
    }

    /**
     * Header 를 표시해줄 사용자 목록
     * */
    private fun showHeader(){
        userNameList.clear()
        list.groupBy { it.login.substring(0 until 1) }
            .forEach {
                userNameList.add(it.value[0].login)
            }
    }

    inner class Holder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val item = list[position]

            if(userNameList.indexOf(item.login) >= 0){
                binding.userSort.visibility = View.VISIBLE
                binding.userSort.text = item.login.substring(0 until 1)
            } else{
                binding.userSort.visibility = View.GONE
            }

            Glide.with(binding.userImg.context).load(item.avatarUrl)
                .transform(CenterCrop(), RoundedCorners(50))
                .into(binding.userImg)

            binding.user.text = item.login

            if(DBAdapter.selectUser(item.id)){
                binding.userStar.setImageResource(R.drawable.ic_star)
            }
            else{
                binding.userStar.setImageResource(R.drawable.ic_star_off)
            }

            binding.userStar.setOnClickListener {
                onClickListener?.invoke(item)
            }
        }
    }

    var onClickListener:((SearchUser) -> Unit)? = null
    fun setClickListener(callback: (SearchUser) -> Unit){
        onClickListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(position)

    override fun getItemCount() = list.size
}
