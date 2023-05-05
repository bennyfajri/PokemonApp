package com.drsync.pokemonapp.detail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.drsync.core.data.Resource
import com.drsync.core.data.local.entity.PokemonEntity
import com.drsync.core.data.remote.response.Pokemon
import com.drsync.core.ui.PokemonMovesAdapter
import com.drsync.core.ui.PokemonTypeAdapter
import com.drsync.core.util.Constant.listPokemonImageUrl
import com.drsync.core.util.ConstantFunction.changeStatusbarColor
import com.drsync.core.util.ConstantFunction.createProgress
import com.drsync.core.util.ConstantFunction.dpToInt
import com.drsync.core.util.ConstantFunction.fibonacci
import com.drsync.core.util.ConstantFunction.setInputError
import com.drsync.pokemonapp.R
import com.drsync.pokemonapp.databinding.ActivityDetailBinding
import com.drsync.pokemonapp.databinding.AlertGreenBinding
import com.drsync.pokemonapp.databinding.AlertRedBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var mTypeAdapter: PokemonTypeAdapter
    private lateinit var mMoveAdapter: PokemonMovesAdapter

    private var pokemon: Pokemon? = null
    private var pokemonName: String? = null
    private var pokemonId: String? = null
    private var pokemonEntity: PokemonEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mTypeAdapter = PokemonTypeAdapter()
        mMoveAdapter = PokemonMovesAdapter()

        pokemonName = intent.getStringExtra(TAG_NAME).toString()
        pokemonId = intent.getStringExtra(TAG_URL).toString()
        getPokemonDetail()

        binding.apply {
            icBack.setOnClickListener {
                finish()
                playBackAnimation()
            }
            btnCatch.setOnClickListener {
                catchPokemon()
            }
            btnRelease.setOnClickListener {
                showAlertFail(
                    getString(R.string.attention),
                    getString(R.string.release_message),
                    getString(R.string.finish)
                )
            }
            btnRename.setOnClickListener {
                renamePokemon()
            }
        }
    }

    private fun renamePokemon() {
        var renamed = pokemonEntity?.renamed
        val fibonacciNumber = fibonacci(renamed as Int)

        val newName = buildString {
            append(pokemonEntity?.name?.split("-")?.get(0) ?: "")
            append("-$fibonacciNumber")
        }

        setPokemonName(newName)
        renamed++

        val newEntity = PokemonEntity(
            newName,
            pokemonEntity?.id.toString(),
            renamed
        )
        viewModel.updatePokemon(newEntity)
        checkPokemonCaught(pokemonId.toString()) {}
    }

    private fun catchPokemon() {
        val chance = Math.random()
        if (chance < 0.5) {
            showAlertSuccess()
        } else {
            showAlertFail(
                getString(R.string.attention),
                getString(R.string.release_message),
                getString(R.string.fail_title)
            )
        }
    }

    private fun showAlertSuccess() {
        val builder = MaterialAlertDialogBuilder(this)
        val bindingDialog = AlertGreenBinding.inflate(layoutInflater)
        builder.setView(bindingDialog.root)

        val dialog = builder.show()

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20.dpToInt(this))
        dialog.window?.setBackgroundDrawable(inset)

        bindingDialog.apply {
            dialog.setCancelable(false)
            tvTitle.text = getString(R.string.success_title)
            tvMessage.text = getString(R.string.success_message)
            btnPositive.setOnClickListener {
                val pokemonNickname = etName.text.toString().trim()

                if (pokemonNickname.isNotEmpty()) {
                    viewModel.insertPokemon(
                        PokemonEntity(
                            pokemonNickname, pokemonId.toString()
                        )
                    )
                    checkPokemonCaught(pokemonId.toString()) { isCaught ->
                        if (isCaught) {
                            dialog.dismiss()
                            setPokemonName(pokemonNickname)
                            getPokemonEntity()
                        }
                    }

                } else {
                    ilName.setInputError(getString(R.string.must_not_empty))
                }
            }
            etName.doAfterTextChanged { ilName.isErrorEnabled = false }
        }
    }

    private fun setPokemonName(pokemonName: String?) {
        this.pokemonName = pokemonName
        binding.tvName.text  = pokemonName
    }

    private fun checkPokemonCaught(
        id: String,
        isCaught: (Boolean) -> Unit?
    ) {
        viewModel.isPokemonCatch(
            id,
            isCatched = {
                if (it) {
                    binding.btnCatch.isVisible = false
                    binding.btnRename.isVisible = true
                    binding.btnRelease.isVisible = true
                    isCaught(true)
                } else {
                    isCaught(false)
                }
            }
        )
    }

    private fun showAlertFail(title: String, message: String, action: String) {
        val builder = MaterialAlertDialogBuilder(this)
        val bindingDialog = AlertRedBinding.inflate(layoutInflater)
        builder.setView(bindingDialog.root)

        val dialog = builder.show()

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20.dpToInt(this))
        dialog.window?.setBackgroundDrawable(inset)

        bindingDialog.apply {
            tvTitle.text = title
            tvMessage.text = message
            btnNegative.setOnClickListener {
                dialog.dismiss()
            }
            btnPositive.setOnClickListener {
                dialog.dismiss()
                releasePokemon()
            }

            if (action == getString(R.string.finish)) {
                btnPositive.isVisible = true
            }
        }
    }

    private fun releasePokemon() {
        viewModel.releasePokemon(pokemonId.toString())
        lifecycleScope.launch {
            delay(300)
            checkPokemonCaught(pokemonName.toString()) { isCaught ->
                if (!isCaught) {
                    finish()
                    playBackAnimation()
                }
            }
        }
    }

    private fun getPokemonDetail() {
        viewModel.getPokemonDetail(pokemonId.toString()).observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    setLoading(true)
                }
                is Resource.Success -> {
                    setLoading(false)
                    setPokemonData(result.data)
                }
                is Resource.Error -> {
                    setLoading(false)
                    Toast.makeText(this@DetailActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setPokemonData(data: Pokemon) {
        mTypeAdapter.submitList(data.types)
        mMoveAdapter.submitList(data.moves)
        setupMoveAndTypeRecycler()

        pokemon = data

        binding.apply {
            tvName.text = pokemonName
            checkPokemonCaught(pokemonName.toString()) { isCaught ->
                if(isCaught) {
                    getPokemonEntity()
                }
            }

            Glide.with(this@DetailActivity)
                .load(data.id.toString().listPokemonImageUrl)
                .placeholder(this@DetailActivity.createProgress())
                .error(android.R.color.darker_gray)
                .into(imgPokemon)

            //set statusbar and background color
            imgPokemon.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                val bitmap = Bitmap.createBitmap(
                    imgPokemon.width,
                    imgPokemon.height,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                imgPokemon.draw(canvas)

                val dominantColor = Palette.from(bitmap).generate().getDominantColor(Color.WHITE)
                this@DetailActivity.changeStatusbarColor(
                    false,
                    dominantColor
                )
                binding.root.setBackgroundColor(dominantColor)
            }
        }
    }

    private fun getPokemonEntity() {
        viewModel.getMyPokemon(pokemonId.toString()).observe(this@DetailActivity) { entity ->
            pokemonEntity = entity
        }
    }

    private fun setupMoveAndTypeRecycler() {
        binding.apply {
            rvType.apply {
                adapter = mTypeAdapter
                setHasFixedSize(true)
            }
            rvMoves.apply {
                adapter = mMoveAdapter
                layoutManager = LinearLayoutManager(this@DetailActivity)
                setHasFixedSize(true)
            }
        }
    }

    private fun setLoading(condition: Boolean) {
        binding.apply {
            progressBar.isVisible = condition
            btnCatch.isEnabled = !condition
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

    companion object {
        const val TAG_NAME = "name"
        const val TAG_URL = "url"
    }
}