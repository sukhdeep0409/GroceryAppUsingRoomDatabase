package com.example.groceryappusingroomdatabase

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grocery_rv_item.view.*

class GroceryRecyclerViewAdapter
constructor(
    var list: List<GroceryItems>,
    private val groceryClickListener: GroceryItemClickListener
): RecyclerView.Adapter<GroceryRecyclerViewAdapter.GroceryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.grocery_rv_item,
                parent,
                false
            )
        return GroceryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.id_tv_item_name.text = model.itemName
        holder.itemView.id_tv_quantity.text = model.itemQuantity.toString()
        holder.itemView.id_tv_rate.text = "₹ ${model.itemPrice}"

        val itemTotal: Int = model.itemPrice  * model.itemQuantity
        holder.itemView.id_tv_total_amount.text = "₹ $itemTotal"

        holder.itemView.id_iv_delete.setOnClickListener {
            groceryClickListener.onItemClick(model)
        }
    }

    override fun getItemCount() = list.size

    interface GroceryItemClickListener {
        fun onItemClick(groceryItem: GroceryItems)
    }

    inner class GroceryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}