package com.example.notestakingapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.notestakingapp.R
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlin.concurrent.timerTask

class AddNoteActivity : AppCompatActivity() {
    val dbTable ="Notes"
    var id =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        try{
            val bundle:Bundle = intent.extras!!
            id =bundle.getInt("ID", 0 )
            if(id!= 0){
                titleEt.setText(bundle.getString("name"))
                descEt.setText((bundle.getString("desc")))
            }
        }catch (ex:Exception) {

        }
        }

        fun addFunc(view: View){
            var dbManger= DbManger(this)
            var values = ContentValues()
            values.put("Title", titleEt.text.toString())
            values.put("Description", descEt.text.toString())

            if(id ==0){
                val ID =dbManger.insert(values)
                if(ID>0){
                    Toast.makeText(this, "Note is Added",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "Error Adding a Note",Toast.LENGTH_SHORT).show()

                }

            }else{
                var selectArgs = arrayOf(id.toString())
                val ID = dbManger.update(values, "ID=?", selectArgs)
                if(ID>0){
                    Toast.makeText(this, "Note is Added",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "Error Adding a Note",Toast.LENGTH_SHORT).show()

                }

            }
        }
    }

