package com.example.solanamobiledappscaffold.presentation.ui.reply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solanamobiledappscaffold.R

class ReplyAdapter(private val itemList: List<Reply>) : RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {

    // Create ViewHolder to hold the views for each item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reply: TextView = view.findViewById(R.id.text_view_reply)
        val date: TextView = view.findViewById(R.id.text_view_current_date)
        val user: TextView = view.findViewById(R.id.reply_username)
    }

    // Create new ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reply_item, parent, false)
        return ViewHolder(view)
    }

    // Bind data to each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.reply.text = item.content
        holder.date.text = item.timestamp.toString()
        holder.user.text = item.author.toString()
        // Bind more data as needed
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return itemList.size
    }
}