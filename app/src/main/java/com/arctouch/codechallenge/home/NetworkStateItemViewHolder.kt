package com.arctouch.codechallenge.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.util.State
import kotlinx.android.synthetic.main.network_state_item.view.*

class NetworkStateItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progressBar.visibility = if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.tvErrorMessage.visibility = if (status == State.ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            view.btRetry.setOnClickListener { retry() }
            return NetworkStateItemViewHolder(view)
        }
    }
}