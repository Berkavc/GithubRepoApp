package com.github.repos.presentation.allrepos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.repos.BR
import com.github.repos.databinding.ItemRepositoriesBinding
import com.github.repos.domain.model.AllRepositories

class AllRepositoriesAdapter(
    private val listener: ItemClickListener,
    private val allRepoList: List<AllRepositories>
) : RecyclerView.Adapter<AllRepositoriesAdapter.AllReposViewHolder>() {

    private lateinit var binding: ItemRepositoriesBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllReposViewHolder {
        binding = ItemRepositoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllReposViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AllReposViewHolder,
        position: Int
    ) {
        val reposList = allRepoList[position]
        holder.bind(reposList)
        holder.itemView.setOnClickListener {
            listener.onClick(reposList.owner.login, reposList.name)
        }
    }

    class AllReposViewHolder(
        private val binding: ItemRepositoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allRepositories: AllRepositories) {
            binding.setVariable(BR.allRepos, allRepositories)
            Glide.with(itemView.context)
                .load(allRepositories.owner.avatarUrl)
                .into(binding.ivAvatar)
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return allRepoList.size
    }

    interface ItemClickListener {
        fun onClick(username: String, repoName: String)
    }
}