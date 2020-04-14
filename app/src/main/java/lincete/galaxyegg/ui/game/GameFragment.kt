package lincete.galaxyegg.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDatabase
import lincete.galaxyegg.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var animation: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EggDatabase.getInstance(application).eggDao
        val viewModelFactory = GameViewModelFactory(dataSource, application)

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

        return binding.root
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
}
