package kr.co.chooji.gitbookmark.utils

import android.content.Context
import kr.co.chooji.gitbookmark.R

object DeviceUtils {

    fun getRootPath(context: Context): String = context.filesDir.absolutePath

    fun getBookMark(context: Context): String = context.getString(R.string.bookmark)
}