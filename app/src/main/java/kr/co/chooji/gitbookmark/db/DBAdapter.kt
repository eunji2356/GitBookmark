package kr.co.chooji.gitbookmark.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kr.co.chooji.gitbookmark.utils.DeviceUtils
import kr.co.chooji.githubapi.model.search.SearchUser

class DBAdapter(context: Context) : DBHelper(
    context,
    DeviceUtils.getRootPath(context) + DeviceUtils.getBookMark(context) + DB_NAME,
    DB_VERSION
) {

    companion object{
        const val DB_NAME: String = "database.db"
        const val DB_VERSION: Int = 1

        var db: SQLiteDatabase? = null
        var dbAdapter: DBAdapter? = null
        var context: Context? = null

        fun init(context: Context) {
            if(dbAdapter == null){
                dbAdapter = DBAdapter(context)
                db = dbAdapter!!.writableDatabase
                this.context = context
            }
        }

        /**
         * 즐겨찾기한 전체 목록 불러오기
         */
        @SuppressLint("Range")
        fun selectAllBookMark(): ArrayList<SearchUser>{
            val result: ArrayList<SearchUser> = arrayListOf()
            try {
                val sql =
                        """
                       SELECT       id,
                                    login,
                                    userImg,
                                    isBookmark
                            FROM    USER_BOOKMARK
                    """

                val cursor: Cursor = db!!.rawQuery(sql, null)
                while (cursor.moveToNext()){
                    val searchUser = SearchUser()
                    searchUser.id = cursor.getInt(cursor.getColumnIndex("id"))
                    searchUser.login = cursor.getString(cursor.getColumnIndex("login"))
                    searchUser.avatarUrl = cursor.getString(cursor.getColumnIndex("userImg"))
                    result.add(searchUser)
                }
            }
            catch (e: Exception){
                throw Exception("사용자 즐겨찾기 전체 목록을 불러오는 중 오류가 발생했습니다.", e)
            }
            return result
        }

        /**
         * 즐겨찾기한 목록 불러오기
         *
         * @param search 검색할 사용자 이름
         */
        @SuppressLint("Range")
        fun selectBookMark(search:String): ArrayList<SearchUser>{
            val result: ArrayList<SearchUser> = arrayListOf()
            try {
                val sql =
                    """
                       SELECT       id,
                                    login,
                                    userImg,
                                    isBookmark
                            FROM    USER_BOOKMARK
                            WHERE   login
                            LIKE    '%${search}%'
                    """

                val cursor: Cursor = db!!.rawQuery(sql, null)
                while (cursor.moveToNext()){
                    val searchUser = SearchUser()
                    searchUser.id = cursor.getInt(cursor.getColumnIndex("id"))
                    searchUser.login = cursor.getString(cursor.getColumnIndex("login"))
                    searchUser.avatarUrl = cursor.getString(cursor.getColumnIndex("userImg"))
                    result.add(searchUser)
                }
            }
            catch (e: Exception){
                throw Exception("검색어에 해당하는 사용자 즐겨찾기 목록을 불러오는 중 오류가 발생했습니다.", e)
            }
            return result
        }

        /**
         * 해당 유저의 즐겨찾기 여부 체크
         *
         * @param id 사용자 ID
         * */
        @SuppressLint("Range")
        fun selectUser(id: Int): Boolean{
            var result = false
            try {
                val sql =
                    """
                       SELECT       isBookmark 
                            FROM    USER_BOOKMARK 
                            WHERE   id = $id
                    """

                val cursor: Cursor = db!!.rawQuery(sql, null)
                while (cursor.moveToNext()){
                    result = cursor.getInt(cursor.getColumnIndex("isBookmark")) == 1
                }
            }
            catch (e: Exception){
                throw Exception("사용자 즐겨찾기 여부를 체크하는 중 오류가 발생했습니다.", e)
            }
            return result
        }

        /**
         * 해당 사용자 즐겨찾기 삭제
         *
         * @param id 사용자 ID
         * */
        fun deleteUserBookmark(id: Int){
            try {
                val sql =
                    """
                       DELETE FROM USER_BOOKMARK WHERE id = $id
                    """
                db?.execSQL(sql)
            }
            catch (e: Exception){
                throw Exception("사용자 즐겨찾기 삭제하는 중 오류가 발생했습니다.", e)
            }
        }

        /**
         * 즐겨찾기 목록 추가/수정
         *
         * @param user 즐겨찾기에 추가할 사용자 정보
         * */
        fun updateUserBookmark(user: SearchUser){
            try {
                val sql =
                    """
                       INSERT OR REPLACE INTO USER_BOOKMARK(
                            id,
                            login,
                            userImg,
                            isBookmark
                       ) 
                       VALUES(${user.id}, "${user.login}", "${user.avatarUrl}", 1)
                    """
                db?.execSQL(sql)
            }
            catch (e: Exception){
                throw Exception("즐겨찾기 목록에 추가하는 중 오류가 발생했습니다.", e)
            }
        }
    }


}