package com.example.camerapermission

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.camerapermission.databinding.RecyclerItemRowBinding

class ImagesAdapter(private var imagesList: ArrayList<Bitmap>) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    class ViewHolder(var binding: RecyclerItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = imagesList.size

    private fun removeItem(position: Int) {
        imagesList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemImage.load(imagesList[position])
        holder.binding.itemDelete.setOnClickListener {
            removeItem(position)
        }

    }
}