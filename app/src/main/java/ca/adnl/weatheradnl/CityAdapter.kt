package ca.adnl.weatheradnl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.adnl.weatheradnl.databinding.ViewholderCityBinding
import ca.adnl.weatheradnl.models.City

class CityAdapter(val items: ArrayList<City>, val callback: CityCallback): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    fun setNewItems(newItems: List<City>){
        this.items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding  = ViewholderCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class CityViewHolder(private val binding: ViewholderCityBinding, private val callback:CityCallback): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                callback.onCityClicked(adapterPosition)
            }
            binding.edit.setOnClickListener {
                callback.onEditClicked(adapterPosition)
            }
        }
        fun bind(city: City){
            binding.tvCity.text = city.name
            binding.tvDescription.text = city.description
        }
    }

    interface CityCallback {
        fun onCityClicked(pos: Int)
        fun onEditClicked(pos: Int)
    }
}

