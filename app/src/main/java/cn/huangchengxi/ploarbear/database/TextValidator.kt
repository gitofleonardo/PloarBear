package cn.huangchengxi.ploarbear.database

import java.util.regex.Pattern

class TextValidator {
    companion object{
        fun checkAccount(username:String):Boolean{
            val pattern= Pattern.compile("^[\\S]+@[\\S]+\$")
            val m=pattern.matcher(username)
            val a= m.find()

            val p1=Pattern.compile("^[\\w]{10,20}$")
            val m1=p1.matcher(username)
            val b=m1.find()

            return a || b
        }
        fun checkPassword(password:String):Boolean{
            val pattern=Pattern.compile("^[\\w]{10,20}$")
            val m=pattern.matcher(password)
            return m.find()
        }
    }
}