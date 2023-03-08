package com.mymealapp.ui.fragment.area.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.databinding.ItemAllAreasBinding
import com.mymealapp.model.data.Area

class AllAreasAdapter(
    private var areaList: MutableList<Area>,
    private val itemClickListener: OnAreaClickListener
) : RecyclerView.Adapter<AllAreasAdapter.VieHolder>() {

    fun setAllAreaList(area: MutableList<Area>) {
        this.areaList = area
        areaList.sortBy { it.area }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemAllAreasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(areaList[position])
    }

    override fun getItemCount(): Int = areaList.size

    inner class VieHolder(private val binding: ItemAllAreasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(area: Area) {
            binding.btnArea.text = area.area

            binding.btnArea.setOnClickListener {
                itemClickListener.onAreaClick(area.area.toString())
            }
        }
    }

    interface OnAreaClickListener {
        fun onAreaClick(area: String)
    }
}