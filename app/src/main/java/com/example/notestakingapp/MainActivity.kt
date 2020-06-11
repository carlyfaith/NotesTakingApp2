package com.example.notestakingapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.notestakingapp.DbManger
import com.example.notestakingapp.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {
    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load db
        LoadQuery("%")
    }
    override fun onResume(){
        super.onResume()
        LoadQuery("%")

    }
    private fun LoadQuery(title:String){
        var dbManager = DbManger(this)
        val projections = arrayOf("ID","Title","Description")
        val selectArgs = arrayOf(title)
        val cursor =dbManager.Query(projections,"Title like?",selectArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title =cursor.getString(cursor.getColumnIndex("Title"))
                val Description =cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))

                }while(cursor.moveToNext())

            }

//        adapter
        var myNotesAdapter = MyNotesAdapter (this, listNotes)
        //set Adapter
        noteLv.adapter =myNotesAdapter


//         get the total number of task in the listView
        val total = noteLv.count
        //actionbar
        val mActionBar = supportActionBar
        if(mActionBar != null){
            //set to actin bar as the subtitle of action bar
            mActionBar.subtitle ="You have $total note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //search view
        val sv:SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm =getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                LoadQuery("%"+query +"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQuery("%"+newText +"%")
                return false
            }
        });
        return super.onCreateOptionsMenu(menu)

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    startActivity(Intent(this, AddNoteActivity::class.java))
                }
                R.id.action_settings -> {
                    Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                }


            }

        }
        return super.onOptionsItemSelected(item)
    }


    inner class MyNotesAdapter :BaseAdapter{
        var listNotesAdapter= ArrayList<Note>()
        var context:Context?=null

        constructor(context: Context, listNotesArray:ArrayList<Note>):super(){
            this.listNotesAdapter =listNotesAdapter
            this.context= context

        }
        override fun getView(position: Int, convertview:View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate( R.layout.row, null)
            val myNote = listNotesAdapter[position]
            myView.tvTitle.text = myNote.nodeName
            myView.tvDesc.text=myNote.nodeDes
            //delete button click
            myView.btnDelete.setOnClickListener{
                var dbManager = DbManger(this.context!!)
                val selectArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectArgs )
                LoadQuery("%")
            }

            //edit update button
            myView.btnEdit.setOnClickListener{
                GoToUpdateFun(myNote)
            }
            myView.btnCopy.setOnClickListener{
                //get title
                val title =myView.tvTitle.text.toString()
                //get the notepad content
                val desc =myView.tvDesc.text.toString()

                val s =title +"\n" +desc
                val cb =getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s //add clipboard
                Toast.makeText(this@MainActivity,"copied..", Toast.LENGTH_SHORT).show()
            }
            //share btn click
            myView.btnShare.setOnClickListener{
                //get title
                val title =myView.tvTitle.text.toString()
                //get the notepad content
                val desc =myView.tvDesc.text.toString()
                val s =title +"\n" +desc
                //share tent
                val shareIntent =Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))

            }
            return myView

        }
        override fun getItem(position: Int):Any{
            return listNotesAdapter[position]

        }

        override fun getItemId(position: Int): Long{
            return position.toLong()
        }
        override fun getCount(): Int{
            return listNotesAdapter.size

        }
    }
    private fun GoToUpdateFun(myNote: Note){
        var intent= Intent(this, AddNoteActivity ::class.java)
        intent.putExtra("ID",myNote.nodeID) //put id
        intent.putExtra("name",myNote.nodeName) //put id
        intent.putExtra("des",myNote.nodeDes) //put description
        startActivity(intent)

    }

}


