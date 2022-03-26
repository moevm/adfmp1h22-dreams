package com.example.dreamdiary.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamdiary.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment (_fromMainAct : AppCompatActivity) :  Fragment(), View.OnClickListener, NoteClickInterface,
    NoteClickDeleteInterface {
    val fromMainAct: AppCompatActivity = _fromMainAct

    lateinit var notesRV: RecyclerView // ----------------------------
    lateinit var addFAB: FloatingActionButton // --------------------------
    lateinit var viewModal: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // запуск нового активити из фрагмента.
//        activity?.let{
//            val intent = Intent (it, NotesX::class.java)
//            it.startActivity(intent)
//        }

//        val intent = Intent(activity, SettingsActivty::class.java)
//        startActivity(intent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dashboard2, container, false)

        notesRV = v.findViewById<RecyclerView>(R.id.idRVNotes)
        addFAB = v.findViewById<FloatingActionButton>(R.id.idFABAddNote)

        addFAB.setOnClickListener(this);
        addFAB.setOnClickListener{
            val intent = Intent(activity, SettingsActivty::class.java)
            startActivity(intent)
        }
        //val noteRVApapter = NoteRVApapter(this, this, this)


        return inflater.inflate(R.layout.fragment_dashboard2, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment DashboardFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DashboardFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        notesRV.layoutManager = LinearLayoutManager(activity)
        val noteRVApapter = NoteRVApapter(this,this, this)
        notesRV.adapter = noteRVApapter

        viewModal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(fromMainAct.application)).get(NoteViewModel::class.java)
        viewModal.allNotes.observe(viewLifecycleOwner, Observer{list ->
            list?.let{
                noteRVApapter.updateList(it)
            }
        })

        addFAB.setOnClickListener{
            val intent = Intent(activity, SettingsActivty::class.java)
            startActivity(intent)
        }


//        addFAB.setOnClickListener{
//           activity?.let{
//            val intent = Intent (it, NotesX::class.java)
//            it.startActivity(intent)
//        }
//        }

    }



    override fun onClick(p0: View?) {

    }

    override fun onNoteClick(note: Note) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(note: Note) {
        TODO("Not yet implemented")
    }
}