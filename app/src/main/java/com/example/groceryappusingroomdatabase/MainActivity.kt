package com.example.groceryappusingroomdatabase

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.grocery_add_dialog.*

class MainActivity : AppCompatActivity(), GroceryRecyclerViewAdapter.GroceryItemClickListener {

    private lateinit var list: List<GroceryItems>
    private lateinit var groceryRecyclerViewAdapter: GroceryRecyclerViewAdapter
    private lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = ArrayList<GroceryItems>()
        id_rv_items.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            groceryRecyclerViewAdapter = GroceryRecyclerViewAdapter(list, this@MainActivity)
            adapter = groceryRecyclerViewAdapter
        }

        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this, factory)
            .get(GroceryViewModel::class.java)

        groceryViewModel.getAllGroceryItems().observe(this, {
            groceryRecyclerViewAdapter.list = it
            groceryRecyclerViewAdapter.notifyDataSetChanged()
        })
        
        id_fab_add.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_add.setOnClickListener {
            val itemName: String = dialog.tv_edit_name.text.toString()
            val itemPrice: Int = dialog.tv_edit_quantity.text.toString().toInt()
            val itemQuantity: Int = dialog.tv_edit_price.text.toString().toInt()

            if (itemName.isNotEmpty() && itemPrice.toString().isNotEmpty() && itemQuantity.toString().isNotEmpty()) {
                val items = GroceryItems(itemName, itemQuantity, itemPrice)
                groceryViewModel.insert(items)
                Toast.makeText(
                    applicationContext,
                    "Item inserted .. ",
                    Toast.LENGTH_SHORT
                ).show()
                groceryRecyclerViewAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "Please enter all data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        dialog.show()
    }

    override fun onItemClick(groceryItem: GroceryItems) {
        groceryViewModel.delete(groceryItem)
        groceryRecyclerViewAdapter.notifyDataSetChanged()
        Toast.makeText(
            applicationContext,
            "Item Deleted ... ",
            Toast.LENGTH_SHORT
        ).show()
    }
}