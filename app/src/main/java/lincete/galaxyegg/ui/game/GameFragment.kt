package lincete.galaxyegg.ui.game

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import lincete.galaxyegg.R
import lincete.galaxyegg.databinding.FragmentGameBinding
import org.koin.android.viewmodel.ext.android.viewModel

class GameFragment : Fragment() {

    private val gameViewModel: GameViewModel by viewModel()

    private lateinit var binding: FragmentGameBinding
    private lateinit var animation: Animation
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        // Set the viewModel for databinding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.gameViewModel = gameViewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = this

        setupAnimation()
        setupSound()
        setupEggImage()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                view?.let {
                    Navigation.findNavController(it).navigate(GameFragmentDirections.actionSettings())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAnimation() {
        animation = AnimationUtils.loadAnimation(context, R.anim.peropot)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                gameViewModel.setAnimationIsFinished()
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })

        gameViewModel.startAnimationEvent.observe(viewLifecycleOwner, Observer { shouldStartAnimation ->
            if (shouldStartAnimation) {
                binding.gameEggImage.animation?.apply {
                    cancel()
                }
                binding.gameEggImage.startAnimation(animation)
            }
        })
    }

    private fun setupSound() {
        gameViewModel.startSoundEvent.observe(viewLifecycleOwner, Observer { shouldStartSound ->
            mediaPlayer = MediaPlayer.create(context, R.raw.blop2)
            mediaPlayer.setOnCompletionListener { it.release() }

            mediaPlayer.apply {
                if (shouldStartSound) {
                    start()
                } else {
                    stop()
                    release()
                }
            }
        })
    }

    private fun setupEggImage() {
        gameViewModel.eggBackground.observe(viewLifecycleOwner, Observer { drawableId ->
            binding.gameEggImage.setImageResource(drawableId)
        })
    }
}
