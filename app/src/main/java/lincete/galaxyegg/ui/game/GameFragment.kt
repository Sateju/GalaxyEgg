package lincete.galaxyegg.ui.game

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDatabase
import lincete.galaxyegg.databinding.FragmentGameBinding
import org.koin.android.ext.android.inject

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var animation: Animation
    private lateinit var mediaPlayer: MediaPlayer

    private val viewModelFactory : GameViewModelFactory by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        gameViewModel = ViewModelProvider(this, viewModelFactory)
                .get(GameViewModel::class.java)

        // Set the viewModel for databinding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.gameViewModel = gameViewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Volume Button
        gameViewModel.isVolumeActive.observe(viewLifecycleOwner, Observer { isVolumeActive ->
            val volumeDrawable = if (isVolumeActive) R.drawable.volume_on else R.drawable.volume_off
            binding.gameVolumeButton.setImageResource(volumeDrawable)
        })

        setupAnimation()
        setupSound()

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
        mediaPlayer = MediaPlayer.create(context, R.raw.blop2)
        gameViewModel.startSoundEvent.observe(viewLifecycleOwner, Observer { shouldStartSound ->
            if (shouldStartSound) {
                mediaPlayer.start()
            } else {
                mediaPlayer.stop()
            }
        })
    }

    override fun onStop() {
        mediaPlayer.release()
        super.onStop()
    }
}
