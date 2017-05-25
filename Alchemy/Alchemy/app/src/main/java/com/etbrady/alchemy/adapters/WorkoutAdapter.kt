package com.etbrady.alchemy.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.etbrady.alchemy.R
import com.etbrady.alchemy.models.Workout
import kotlinx.android.synthetic.main.card_view_workout.view.*



class WorkoutAdapter: RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    val workouts: MutableList<Workout> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_view_workout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindClass(workouts[position])
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun setWorkouts(newWorkouts: List<Workout>) {
        workouts.clear()
        workouts.addAll(newWorkouts)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindClass(workout: Workout) {
            itemView.title_textView.text = workout.title
            val exerciseListView = itemView.listview_exercises
            exerciseListView.isEnabled = false
            exerciseListView.adapter = ArrayAdapter<String>(itemView.context, android.R.layout.simple_list_item_1, workout.exercises)
            setListViewHeightBasedOnChildren(exerciseListView)
        }

        fun setListViewHeightBasedOnChildren(listView: ListView) {
            val listAdapter = listView.adapter ?:
                    return

            var totalHeight = 0
            for (i in 0..listAdapter.count - 1) {
                val listItem = listAdapter.getView(i, null, listView)
                listItem.measure(0, 0)
                totalHeight += listItem.measuredHeight
            }

            val params = listView.layoutParams
            params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
            listView.layoutParams = params
            listView.requestLayout()
        }

    }
}