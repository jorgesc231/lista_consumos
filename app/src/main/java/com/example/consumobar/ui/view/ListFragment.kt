package com.example.consumobar.ui.view

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumobar.R
import com.example.consumobar.databinding.FragmentListBinding
import com.example.consumobar.domain.model.Product
import com.example.consumobar.ui.viewmodel.ProductViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale


class ListFragment : Fragment(), MenuProvider {
    private lateinit var binding : FragmentListBinding
    private lateinit var listAdapter : ListAdapter

    private val productViewModel : ProductViewModel by activityViewModels()

    private lateinit var nav_controller : NavController
    private var showMenu = false

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_menu, menu)

        menu.setGroupVisible(0, showMenu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            // these ids should match the item ids from my_fragment_menu.xml file
            R.id.clearList -> {

                showConfirmationAlert()

                // by returning 'true' we're saying that the event
                // is handled and it shouldn't be propagated further
                true
            }
            else -> false
        }

        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val number_format = NumberFormat.getNumberInstance(Locale.ITALIAN)

        val toolBar = activity?.findViewById<MaterialToolbar>(R.id.mainToolbar)
        toolBar?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val list = binding.rvList

        nav_controller = view.findNavController()

        listAdapter = ListAdapter {itemSelected(it)}
        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this.context)
        list.adapter = listAdapter

        productViewModel.onCreate()

        binding.tvPrice.text = "Total: $ 0"

        productViewModel.productModel.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvEmptyList.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
                binding.tvPrice.text = "Total: $ 0"

                showMenu = false
                toolBar?.invalidateMenu()
            } else {

                binding.rvList.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE

                showMenu = true
                toolBar?.invalidateMenu()

                var total = 0

                for (product in it.listIterator()) {
                    total += product.price * product.quantity
                }

                binding.tvPrice.text = context?.getString(R.string.total_string_format, number_format.format(total))

                listAdapter.updateList(it)
                binding.rvList.smoothScrollToPosition(it.size - 1)
            }
        }

        binding.FABadd.setOnClickListener {
            nav_controller.navigate(ListFragmentDirections.actionListFragmentToAddFragment())
        }
    }

    private fun itemSelected(product: Product) {
        nav_controller.navigate(ListFragmentDirections.actionListFragmentToAddFragment(product))
    }

    private fun showConfirmationAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Borrar toda la lista?")
            .setMessage("¿Estas seguro de que quieres borrar toda la lista?")
            .setPositiveButton("Borrar") {_, _ -> productViewModel.deleteList()}
            .setNegativeButton("Cancelar") {_, _ -> }
            .show()
    }
}