package com.unaj.loginsqlite.ui.slideshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.unaj.loginsqlite.Adapters.ReservAdapter
import com.unaj.loginsqlite.MenuActivity
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.databinding.FragmentSlideshowBinding
import com.unaj.loginsqlite.helpers.UserRolApplication.Companion.prefs
import com.unaj.loginsqlite.model.Reservation
import com.unaj.loginsqlite.sql.DatabaseHelper

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: ReservAdapter
    private lateinit var reservs: List<Reservation>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        databaseHelper = DatabaseHelper(this.requireActivity())
        initRecycler()
        return root
    }


    private fun initRecycler() {
        val userEmail = prefs.getUserEmail()
        reservs = databaseHelper.getAllReservationsByUserEmail(userEmail)
        binding.rvReserv.layoutManager = LinearLayoutManager(MenuActivity().getActivity())
        adapter = ReservAdapter(reservs, object : ReservAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                performOptionsMenuClick(position)
            }

        })
        binding.rvReserv.adapter = adapter

        registerForContextMenu(binding.rvReserv)

        adapter.notifyDataSetChanged()
    }

    private fun performOptionsMenuClick(position: Int) {
        val popupMenu = PopupMenu(this.context, binding.rvReserv[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{

            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.action_settings_reservas -> {
                        val tempReservation = reservs[position]
                        databaseHelper.deleteReservation(tempReservation)
                        adapter.notifyDataSetChanged()
                        initRecycler()
                        return true
                    }
                }
                return false
            }

        })
        popupMenu.show()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu!!.setHeaderTitle("Menu Contextual")
        menu.add(0,v!!.id,0,"Editar")
        menu.add(0,v!!.id,0,"Cancelar")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when{
            item!!.title=="Editar" ->
                Toast.makeText(context, "Se edito tu reserva", Toast.LENGTH_SHORT).show()
            item!!.title=="Cancelar" ->
                Toast.makeText(context, "Se edito tu reserva", Toast.LENGTH_SHORT).show()
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}