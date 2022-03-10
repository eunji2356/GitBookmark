package kr.co.chooji.gitbookmark.db

object DBTable {

    const val USER_BOOKMARK_TABLE = "USER_BOOKMARK"

    private const val CREATE_USER_BOOKMARK =
        """
            CREATE TABLE IF NOT EXISTS 'USER_BOOKMARK' (
                'id' integer NOT NULL,
                'login' text NOT NULL,
                'userImg' text NOT NULL,
                'isBookmark' integer NOT NULL,
                PRIMARY KEY ('id')
            )
        """

    fun getCreateTable(): ArrayList<String>{
        val tableList = arrayListOf<String>()

        tableList.add(CREATE_USER_BOOKMARK)

        return tableList
    }
}