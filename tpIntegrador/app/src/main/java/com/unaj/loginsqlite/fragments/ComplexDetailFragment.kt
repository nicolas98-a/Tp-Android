package com.unaj.loginsqlite.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.model.Complex

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var detailComplexName: TextView
private lateinit var detailComplexPhone: TextView

/**
 * A simple [Fragment] subclass.
 * Use the [ComplexDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComplexDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_complex_detail, container, false)

        detailComplexName = vista.findViewById(R.id.tvComplexName)
        detailComplexPhone = vista.findViewById(R.id.tvComplexPhone)

        val bundleComplex = arguments
        var complex: Complex

        if (bundleComplex != null) {
            complex = bundleComplex.getSerializable("objeto") as Complex

            detailComplexPhone.text = complex.name
            detailComplexPhone.text = complex.phone
        }

        return vista
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComplexDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ComplexDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}