package com.drsync.pokemonapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.drsync.core.data.remote.response.Result
import com.drsync.core.ui.ItemPokemonPagingAdapter
import com.drsync.core.ui.LoadingStateAdapter
import com.drsync.core.util.Constant.getId
import com.drsync.core.util.ConstantFunction.changeStatusbarColor
import com.drsync.core.util.ConstantFunction.isUsingNightModeResources
import com.drsync.core.util.ConstantFunction.playPopupAnimation
import com.drsync.pokemonapp.R
import com.drsync.pokemonapp.databinding.ActivityMainBinding
import com.drsync.pokemonapp.detail.DetailActivity
import com.drsync.pokemonapp.detail.DetailActivity.Companion.TAG_NAME
import com.drsync.pokemonapp.detail.DetailActivity.Companion.TAG_URL
import com.drsync.pokemonapp.mypokemon.MyPokemonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mAdapter: ItemPokemonPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.changeStatusbarColor(!isUsingNightModeResources(resources), ContextCompat.getColor(this, com.drsync.core.R.color.white_black))
        mAdapter = ItemPokemonPagingAdapter {
            toDetailActivity(it)
        }
        binding.icMenu.setOnClickListener {
            showPopupMenu()
        }
        getPokemonList()
    }

    private fun showPopupMenu() {
        PopupMenu(this, binding.icMenu).run {
            menuInflater.inflate(R.menu.home_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_my_pokemon -> {
                        toMyPokemonScreen()
                    }
                }
                true
            }
            gravity = Gravity.END
            show()
        }
    }

    private fun toMyPokemonScreen() {
        startActivity(Intent(this, MyPokemonActivity::class.java))
        playPopupAnimation()
    }

    private fun toDetailActivity(result: Result) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(TAG_NAME, result.name)
            putExtra(TAG_URL, result.url.getId)
            startActivity(this)
            this@MainActivity.playPopupAnimation()
        }
    }

    private fun getPokemonList() {
        viewModel.getPokemonList().observe(this) { result ->
            mAdapter.submitData(lifecycle, result)
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        binding.rvPokemon.apply {
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    mAdapter.retry()
                }
            )
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            setHasFixedSize(true)
        }
    }
}