package com.example.quotes.RCV_Classes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.quotes.R
import retrofit2.Response

class QuotesAdapter(val context: Context, val list: List<QuotesModel>) :
    RecyclerView.Adapter<QuotesAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_quotes_rcv, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.tvQuote.text = list[position].text
        holder.tvAuthor.text = list[position].author

        holder.shareBtn.setOnClickListener(View.OnClickListener {
            val quote = list[position].text
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT , quote)
            context.startActivity(Intent.createChooser(intent , "Share With"))
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewHolder(itemView: View) : ViewHolder(itemView) {
        var tvQuote = itemView.findViewById<TextView>(R.id.quote)
        var tvAuthor = itemView.findViewById<TextView>(R.id.author)
        var shareBtn = itemView.findViewById<TextView>(R.id.share)
    }

}