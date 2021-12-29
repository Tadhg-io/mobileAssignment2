package ie.wit.pintmark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.pintmark.databinding.CardMarkerBinding
import ie.wit.pintmark.models.MarkerModel

interface PintmarkListener {
    fun onMarkerClick(marker: MarkerModel)
}

class PintmarkAdapter constructor(private var markers: List<MarkerModel>,
                                  private val listener: PintmarkListener) :
    RecyclerView.Adapter<PintmarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMarkerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val marker = markers[holder.adapterPosition]
        holder.bind(marker, listener)
    }

    override fun getItemCount(): Int = markers.size

    class MainHolder(private val binding : CardMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: MarkerModel, listener: PintmarkListener) {
            binding.markerTitle.text = marker.title
            binding.description.text = marker.description
            binding.category.text = marker.category
            Picasso.get().load(marker.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onMarkerClick(marker) }
        }
    }
}