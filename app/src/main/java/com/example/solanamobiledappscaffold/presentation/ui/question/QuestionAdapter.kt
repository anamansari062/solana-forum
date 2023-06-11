package com.example.solanamobiledappscaffold.presentation.ui.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solanamobiledappscaffold.R

class QuestionAdapter(private val itemList: List<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    // Create ViewHolder to hold the views for each item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById(R.id.text_view_question)
        val date: TextView = view.findViewById(R.id.text_view_date)
        val user: TextView = view.findViewById(R.id.text_view_user)


    }

    // Create new ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return ViewHolder(view)
    }

    // Bind data to each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.question.text = item.content
        holder.date.text = item.timestamp.toString()
        holder.user.text = item.author.toString()

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(itemList[position])
            }
        }
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnItemClickListener {
        fun onItemClick(question: Question)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}