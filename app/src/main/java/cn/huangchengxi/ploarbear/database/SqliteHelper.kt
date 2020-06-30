package cn.huangchengxi.ploarbear.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqliteHelper(context: Context,name:String?,factory:SQLiteDatabase.CursorFactory?,version:Int)
    :SQLiteOpenHelper(context,name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val localSetting="create table local_setting(first_init int(1))"
        val localUser="create table user(account varchar(20) primary key unique,passwd varchar(20),current_user int(1))"
        val localUserProps="create table user_props(uid varchar(20),nickname varchar(20),portrait_url string)"
        val localMessageRecord="create table local_message(uid varchar(20),mid varchar(20),type int,content string,time_sent long)"
        db?.execSQL(localSetting)
        db?.execSQL(localUser)
        db?.execSQL(localUserProps)
        db?.execSQL(localMessageRecord)
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
            cursor.close()
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
            val cursor=db.query("user", arrayOf("account","passwd"),"account=?", arrayOf(user.account),"","","");
            if (cursor.moveToNext()){
                //val sql1="update user set passwd=\"${user.passwd}\" and current_user=1 where account=\"${user.account}\""
                //val sql2="update user set current_user=0"
                //db.execSQL(sql2)
                //db.execSQL(sql1)
                val contentValues=ContentValues()
                contentValues.put("account",user.account)
                contentValues.put("passwd",user.passwd)
                contentValues.put("current_user",1)
                db.update("user",contentValues,"account=?", arrayOf(user.account))
            }else{
                val sql="insert into user values(\"${user.account}\",\"${user.passwd}\",1)"
                db.execSQL(sql)
            }
            cursor.close()
        }
        fun removeLocalUser(ctx: Context,username:String){
            val helper= getAvailableSqliteHelper(ctx)
            val db=helper.writableDatabase
            db.delete("user","account=?", arrayOf(username))
        }
    }
}