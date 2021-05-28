package org.meetmap.view.message

import androidx.recyclerview.widget.DiffUtil
import org.meetmap.data.model.domain.Message

class MessageDiffUtilCallback() : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }


}