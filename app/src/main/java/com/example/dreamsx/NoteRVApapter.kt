package com.example.dreamsx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteRVApapter(
    val context: Context,
    private val noteClickInterface: NoteClickInterface,
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
) : RecyclerView.Adapter<NoteRVApapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    //Отображение превью снов на главном экране
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noteTV = itemView.findViewById<TextView>(R.id.idTVNoteTitle)
        val timeTV = itemView.findViewById<TextView>(R.id.idTVTimeStamp)
        val deleteTV = itemView.findViewById<ImageView>(R.id.IVDelete)
        val tagTV = itemView.findViewById<TextView>(R.id.idTVTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false);
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.setText(allNotes.get(position).notesTitle)
        holder.timeTV.setText("Last updated : "+allNotes.get(position).timeStamp)
        holder.tagTV.setText(allNotes.get(position).noteTags)

        holder.deleteTV.setOnClickListener{
            noteClickDeleteInterface.onDeleteClick(allNotes.get(position))
        }

        holder.itemView.setOnClickListener{
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
    fun updateList(newList : List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

    fun getCountOfDreams(newList : List<Note>): Int {
        return newList.count()
    }

    fun getAllTags(newList : List<Note>) : Array<String>{
        var tags : Array<String> = arrayOf()
        for(element in newList)
            tags += element.noteTags
        return tags
    }

    fun getAllNotes() : List<Note>{
//        var notes: Array<Note> = arrayOf()
//        for(element in allNotes)
//            notes += element
        //return notes
        return allNotes
    }



}

interface NoteClickDeleteInterface{
    fun onDeleteClick(note: Note)
}

interface NoteClickInterface{
    fun onNoteClick(note: Note)
}
