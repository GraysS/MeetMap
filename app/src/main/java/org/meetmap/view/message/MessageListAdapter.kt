package org.meetmap.view.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.meetmap.R
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.Message

class MessageListAdapter(val familiar: Familiar)
    : ListAdapter<Message, MessageListAdapter.MessageViewHolder>(MessageDiffUtilCallback()) {

    private val SELF = 100

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view: View = if(viewType == SELF)
            inflater.inflate(R.layout.item_self_message, parent, false)
        else
            inflater.inflate(R.layout.item_familiar_message, parent, false)

        return MessageViewHolder(view)
    }


    override fun getItemViewType(position: Int): Int {
        val message: Message = getItem(position)
        if(familiar.emailCreator == message.emailMyUser) {
            return SELF
        }
        return position
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position).let { familiar ->
            holder.message.text = familiar.message
        }
    }

    class MessageViewHolder(root: View): RecyclerView.ViewHolder(root) {
        val message: TextView = root.findViewById(R.id.tv_message)
    }
}