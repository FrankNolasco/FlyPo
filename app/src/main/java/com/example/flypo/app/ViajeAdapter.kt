package com.example.flypo.app
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flypo.R
import androidx.recyclerview.widget.RecyclerView
import com.example.flypo.DetalleDestino
import kotlinx.android.synthetic.main.fragment_viaje_card.view.*

class ViajeAdapter (
    private val listaViajes: List<Viaje>
) : RecyclerView.Adapter<ViajeAdapter.ViewHolder>(){
    private lateinit var ctx: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ctx = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_viaje_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
        val itemUI = holder.card
        val item = listaViajes[position]

        itemUI.textView.text = "Viaje #${(position + 1)}"

        itemUI.textView4.text = item.originName
        itemUI.textView5.text = item.destineName

        itemUI.button.setOnClickListener {
            val intent = Intent(ctx, DetalleDestino::class.java)
            intent.putExtra("Viaje", item)
            ctx.startActivity(intent);
        }
    }

    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return listaViajes.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card: View = itemView
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}