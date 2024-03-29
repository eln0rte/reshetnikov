package ru.elnorte.tinkoffeduapp.ui.overview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import ru.elnorte.tinkoffeduapp.R
import ru.elnorte.tinkoffeduapp.data.movierepository.MovieRepository
import ru.elnorte.tinkoffeduapp.databinding.OverviewFragmentBinding

class OverviewFragment : Fragment() {


    lateinit var viewModel: OverviewViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: OverviewFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.overview_fragment, container, false)

        val repo = MovieRepository()
        val viewModelFactory = OverviewViewModelFactory(repo)

        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = MovieListAdapter(MovieClickListener {
            viewModel.showMovie(it)
        })
        binding.moviesList.adapter = adapter

        try {
            viewModel.update()
            Log.d("ellog3", "hi")
        } catch (ex: Exception) {
            Log.d("ellog3", ex.message.orEmpty())
        }

        viewModel.navigateToMovie.observe(this) {
            if (it != null) {
                this.findNavController().navigate(OverviewFragmentDirections.actionShowMovie(it))
                viewModel.showMovieComplete()
            }
        }
        viewModel.model.observe(this) {
            adapter.submitList(it)
        }

        return binding.root
    }
}