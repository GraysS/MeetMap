package org.meetmap.view.familiar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.meetmap.Event
import org.meetmap.R
import org.meetmap.data.model.domain.Familiar

class FamiliarListAdapter()
    : ListAdapter<Familiar, FamiliarListAdapter.FamiliarViewHolder>(FamiliarDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_familiar, parent, false)
        //view.setOnClickListener(itemClick)
        return FamiliarViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FamiliarViewHolder, position: Int) {

        getItem(position).let { familiar ->
            holder.familiarNick.text = familiar.nickname + "-" + familiar.emailFamiliar
        }

       /* holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }*/
    }


    class FamiliarViewHolder(root: View): RecyclerView.ViewHolder(root) {

        val familiarNick: TextView = root.findViewById(R.id.tv_familiar_nick)

    }

  /*  fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }*/
}