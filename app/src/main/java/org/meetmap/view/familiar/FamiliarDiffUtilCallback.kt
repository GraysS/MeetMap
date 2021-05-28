package org.meetmap.view.familiar

import androidx.recyclerview.widget.DiffUtil
import org.meetmap.data.model.domain.Familiar

class FamiliarDiffUtilCallback() : DiffUtil.ItemCallback<Familiar>() {
    override fun areItemsTheSame(oldItem: Familiar, newItem: Familiar): Boolean {
        return oldItem.emailFamiliar == newItem.emailFamiliar
    }

    override fun areContentsTheSame(oldItem: Familiar, newItem: Familiar): Boolean {
        return oldItem.emailFamiliar == newItem.emailFamiliar
    }
}