package com.etbrady.alchemy.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import com.etbrady.alchemy.models.Class
import kotlinx.android.synthetic.main.card_view_class.view.*

class ClassAdapter: RecyclerView.Adapter<ClassAdapter.ViewHolder>() {


    val classes: MutableList<Class> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_view_class, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindClass(classes[position])
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    fun setClasses(newClasses: List<Class>) {
        classes.clear()
        classes.addAll(newClasses)
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindClass(c: Class) {
            itemView.name_textView.text = c.name
        }
    }
}