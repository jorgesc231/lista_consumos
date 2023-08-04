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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.consumobar.R
import com.example.consumobar.databinding.FragmentAddBinding
import com.example.consumobar.domain.model.Product
import com.example.consumobar.ui.viewmodel.FormValidationViewModel
import com.example.consumobar.ui.viewmodel.ProductViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale

class AddFragment : Fragment(), MenuProvider {

    private lateinit var binding : FragmentAddBinding
    private val productViewModel : ProductViewModel by activityViewModels()
    private val formValidationViewModel : FormValidationViewModel by activityViewModels()

    private lateinit var args: AddFragmentArgs
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = AddFragmentArgs.fromBundle(requireArguments())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val number_format = NumberFormat.getNumberInstance(Locale.ITALIAN)
        navController = view.findNavController()

        val name = binding.etName
        val price = binding.etPrice
        val quantity = binding.etQuantity

        val toolBar = activity?.findViewById<MaterialToolbar>(R.id.mainToolbar)

        if (args.product != null) {
            toolBar?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

        name.doOnTextChanged { text, start, before, count ->
            formValidationViewModel.name.postValue(name.text.toString())
        }

        price.doOnTextChanged { text, start, before, count ->
            formValidationViewModel.price.postValue(price.text.toString())
        }

        quantity.doOnTextChanged { text, start, before, count ->
            formValidationViewModel.quantity.postValue(quantity.text.toString())
        }

        formValidationViewModel.valid.observe(viewLifecycleOwner) {
            binding.btSave.isEnabled = it

            if (it && !price.text.isNullOrEmpty() && !quantity.text.isNullOrEmpty()) {
                val subtotal =
                    price.text.toString().toInt() * quantity.text.toString().toInt()
                binding.Total.text =
                    context?.getString(R.string.price_format, number_format.format(subtotal))
            }
            else {
                binding.Total.text = ""
            }
        }

        if (arguments != null) {
            if (args.product != null) {
                name.setText(args.product!!.name)
                price.setText(args.product!!.price.toString())
                quantity.setText(args.product!!.quantity.toString())
            } else {
                // TODO: Debe haber una mejor forma
                name.setText("")
                price.setText("")
                quantity.setText("")
            }
        }

        binding.btSave.setOnClickListener {
            val p = Product(
                id = args.product?.id ?: 0,
                name = binding.etName.text.toString(),
                price = Integer.parseInt(binding.etPrice.text.toString()),
                quantity = Integer.parseInt(binding.etQuantity.text.toString()),
            )

            productViewModel.addProduct(p)

            binding.etName.text?.clear()
            binding.etPrice.text?.clear()
            binding.etQuantity.text?.clear()

            navController.navigateUp()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_menu, menu)
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

    private fun showConfirmationAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Borrar producto")
            .setMessage("¿Estas seguro de que quieres borrar el producto?")
            .setPositiveButton("Borrar") {_, _ ->


                productViewModel.deleteProduct(Product(
                        id = args.product?.id ?: 0,
                        name = binding.etName.text.toString(),
                        price = Integer.parseInt(binding.etPrice.text.toString()),
                        quantity = Integer.parseInt(binding.etQuantity.text.toString()),
                    )
                )



                navController.navigateUp()
            }
            .setNegativeButton("Cancelar") {_, _ -> }
            .show()
    }
}