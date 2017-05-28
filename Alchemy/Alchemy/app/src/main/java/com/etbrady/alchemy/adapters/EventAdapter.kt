package com.etbrady.alchemy.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import com.etbrady.alchemy.models.Event
import kotlinx.android.synthetic.main.card_view_event.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.ViewHolder>() {


    val events: MutableList<Event> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_view_event, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindClass(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindClass(c: Event) {
            itemView.name_textView.text = c.name
            itemView.instructorName_textView.text = c.instructorName
            itemView.time_textView.text = getFormattedTime(c)
            itemView.location_textView.text = c.locationName
        }

        private fun getFormattedTime(c: Event): String {
            return c.getStartDateString() + " - " + c.getEndDateString()
        }
    }
}