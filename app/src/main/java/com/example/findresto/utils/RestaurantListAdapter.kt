package com.example.findresto.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findresto.databinding.ItemRestaurantBinding
import com.example.findresto.network.response.RestaurantsModel

class RestaurantListAdapter(context: Context) :
    RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    private val restaurantList = ArrayList<RestaurantsModel>()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemBinding = ItemRestaurantBinding.inflate(mInflater, parent, false)
        return RestaurantViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = restaurantList[position].restoModel
        holder.itemBinding.model = item
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    fun updateList(userList: ArrayList<RestaurantsModel>) {
        this.restaurantList.clear()
        this.restaurantList.addAll(userList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = restaurantList.size

    class RestaurantViewHolder(val itemBinding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}