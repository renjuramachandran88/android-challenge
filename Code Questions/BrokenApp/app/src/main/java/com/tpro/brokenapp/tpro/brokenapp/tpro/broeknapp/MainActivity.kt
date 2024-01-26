package com.tpro.brokenapp.tpro.brokenapp.tpro.broeknapp
import android.app.*
import android.os.*
import android.widget.*
import com.tpro.brokenapp.R









class MainActivity
  : Activity() {
  override fun onCreate(savedInstanceState:Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
      findViewById<Button>(R.id.button)!!.setOnClickListener {
      findViewById<EditText>(R.id.editext1).text.toString().toLowerCase()?.let { text1 ->
          val text2 = findViewById<EditText>(R.id.editext2).text.toString().toLowerCase()
          var sum = value(text1).getValue() + value(text2).getValue()
          findViewById<TextView>(R.id.rsult).text = sum.toString()!!
      }
      }
  }

  override fun onPostCreate(savedInstanceState:Bundle?) { super.onPostCreate(savedInstanceState)
  }
}

class value(val integer:String) {

  fun getValue(): Int {
    if (integer == "0") { return "0".toString().toInt() * 2 } else {
Thread.sleep(50) //haha
return integer.toInt()
    }
  }

}

//class value2(val integer: String) {
//
//  fun getValue(): Int {
//    return integer.toInt()
//  }
//
//}