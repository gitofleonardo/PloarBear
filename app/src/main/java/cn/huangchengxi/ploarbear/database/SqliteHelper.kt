package cn.huangchengxi.ploarbear.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context,name:String?,factory:SQLiteDatabase.CursorFactory?,version:Int)
    :SQLiteOpenHelper(context,name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val localSetting="create table local_setting(first_init int(1))"
        var localUser="create table user(account varchar(20),passwd varchar(20),current_user int(1))"
        db?.execSQL(localSetting)
        db?.execSQL(localUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    companion object{
        fun getAvailableSqliteHelper(ctx:Context):SqliteHelper{
            return SqliteHelper(ctx,"local_db",null,1)
        }
        fun getLocalSetting(ctx:Context):LocalSetting?{
            val helper= getAvailableSqliteHelper(ctx)
            val db=helper.writableDatabase
            val cursor=db.query("local_setting", arrayOf("first_init"),"", arrayOf(),"","","")
            if (cursor.moveToNext()){
                val setting= LocalSetting(cursor.getInt(cursor.getColumnIndex("first_init")))
                cursor.close()
                return setting
            }
            cursor.close()
            return null
        }
        fun getLocalUsers(ctx:Context):List<LocalUser>{
            val helper= getAvailableSqliteHelper(ctx)
            val db=helper.writableDatabase
            val cursor=db.query("user", arrayOf("account","passwd","current_user"),"", arrayOf(),"","","")
            val list= arrayListOf<LocalUser>()
            while (cursor.moveToNext()){
                val user=LocalUser(cursor.getString(cursor.getColumnIndex("account")),cursor.getString(cursor.getColumnIndex("passwd")))
                list.add(user)
            }
            return list
        }
        fun getLocalCurrentUser(ctx: Context):LocalUser?{
            val helper= getAvailableSqliteHelper(ctx)
            val db=helper.writableDatabase
            val cursor=db.query("user", arrayOf("account","passwd","current_user"),"current_user=?", arrayOf("1"),"","","")
            if (cursor.moveToNext()){
                val user=LocalUser(cursor.getString(cursor.getColumnIndex("account")),cursor.getString(cursor.getColumnIndex("passwd")))
                cursor.close()
                return user
            }
            return null
        }
        fun updateUser(ctx: Context,user:LocalUser){
            val helper= getAvailableSqliteHelper(ctx)
            val db=helper.writableDatabase
            var cursor=db.query("user", arrayOf("account"),"", arrayOf(),"","","");
            if (cursor.moveToNext()){
                val sql1="update user set passwd=\"${user.passwd}\" and current_user=1 where account=\"${user.account}\""
                val sql2="update user set current_user=0 where account!=\"${user.account}\""
                db.execSQL(sql2)
                db.execSQL(sql1)
            }else{
                val sql="insert into user values(\"${user.account}\",\"${user.passwd}\",1)"
                db.execSQL(sql)
            }
        }
    }
}