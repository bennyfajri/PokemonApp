package com.drsync.pokemonapp.mypokemon

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.drsync.core.data.remote.response.Result
import com.drsync.core.ui.ItemPokemonAdapter
import com.drsync.core.util.ConstantFunction.changeStatusbarColor
import com.drsync.core.util.ConstantFunction.playPopupAnimation
import com.drsync.pokemonapp.databinding.ActivityMyPokemonBinding
import com.drsync.pokemonapp.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPokemonBinding
    private val viewModel: MyPokemonViewModel by viewModels()
    private lateinit var mAdapter: ItemPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.changeStatusbarColor(true, Color.WHITE)
        mAdapter = ItemPokemonAdapter {
            toDetailActivity(it)
        }
        binding.icBack.setOnClickListener {
            finish()
            playBackAnimation()
        }

        getMyPokemon()

    }

    private fun toDetailActivity(result: Result) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.TAG_NAME, result.name)
            putExtra(DetailActivity.TAG_URL, result.url)
            startActivity(this)
            this@MyPokemonActivity.playPopupAnimation()
        }
    }

    private fun getMyPokemon() {
        viewModel.getMyPokemonList().observe(this) { pokemons ->
            mAdapter.submitList(pokemons.map { Result(it.name, it.id) })
            setupViewModel()
            binding.viewEmpty.root.isVisible = pokemons.isEmpty()
        }
    }

    private fun setupViewModel() {
        binding.rvMyPokemon.apply {
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        playBackAnimation()
    }

    private fun playBackAnimation() {
        overridePendingTransition(
            com.drsync.core.R.anim.no_animation,
            com.drsync.core.R.anim.to_bottom
        )
    }
}