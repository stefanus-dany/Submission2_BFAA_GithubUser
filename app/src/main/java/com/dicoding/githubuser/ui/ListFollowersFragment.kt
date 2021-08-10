package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FollowersAdapter
import com.dicoding.githubuser.databinding.FragmentListFollowersBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewModel.UserViewModel

class ListFollowersFragment : Fragment() {

    private var _binding: FragmentListFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private lateinit var data: MutableList<User>
    private lateinit var adapter: FollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]
        data = mutableListOf()
        val dataUser = activity?.intent?.getParcelableExtra<User>(DetailActivity.EXTRA_USER)
        if (dataUser != null) {
            getUser(dataUser.username.toString())
        }
    }

    private fun getUser(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDataFollowersFromAPI(username).observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.dataNotFound.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {
                binding.dataNotFound.visibility = View.GONE
                data = it
                adapter = FollowersAdapter(requireContext())
                adapter.setData(data)
                with(binding) {
                    rvFollowers.adapter = adapter
                    rvFollowers.layoutManager = LinearLayoutManager(requireContext())
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}