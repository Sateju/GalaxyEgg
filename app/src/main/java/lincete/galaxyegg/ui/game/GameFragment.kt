package lincete.galaxyegg.ui.game

import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import lincete.galaxyegg.BuildConfig
import lincete.galaxyegg.R
import lincete.galaxyegg.databinding.FragmentGameBinding
import lincete.galaxyegg.utils.showToast
import org.koin.android.viewmodel.ext.android.viewModel

class GameFragment : Fragment() {

    private val gameViewModel: GameViewModel by viewModel()

    private lateinit var binding: FragmentGameBinding
    private lateinit var animation: Animation
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var rewardedAd: RewardedAd
    private lateinit var adView: AdView

    private var initialLayoutComplete = false

    private val adSize: AdSize
        get() {
            val display = activity?.windowManager?.defaultDisplay
            val outMetrics = DisplayMetrics()
            display?.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.gameBanner.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }

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
        setupAds()

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

    private fun setupAds() {
        // Banner
        adView = AdView(context)
        binding.gameBanner.addView(adView)
        MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                        .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                        .build()
        )
        binding.gameBanner.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }

        // Rewarded
        rewardedAd = createAndLoadRewardedAd()
        val rewardedAdCallback = object : RewardedAdCallback() {

            override fun onUserEarnedReward(reward: RewardItem) {
                gameViewModel.enableMultiplier(reward.amount)
            }

            override fun onRewardedAdOpened() {
                gameViewModel.resetShouldShowAd()
            }

            override fun onRewardedAdClosed() {
                rewardedAd = createAndLoadRewardedAd()
                gameViewModel.startRewardTimer()
            }
        }

        gameViewModel.shouldShowAdd.observe(viewLifecycleOwner, Observer { shouldShowAdd ->
            if (shouldShowAdd) {
                if (rewardedAd.isLoaded) {
                    rewardedAd.show(activity, rewardedAdCallback)
                } else {
                    context?.showToast(getString(R.string.game_rewarded_ad_not_loaded))
                }
            }
        })
    }

    private fun createAndLoadRewardedAd(): RewardedAd {
        val ad = RewardedAd(context, BuildConfig.REWARDED_AD_ID)

        val rewardedAdLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                rewardedAd = createAndLoadRewardedAd()
            }
        }

        ad.loadAd(AdRequest.Builder().build(), rewardedAdLoadCallback)
        return ad
    }

    private fun loadBanner() {
        adView.adUnitId = BuildConfig.BANNER_AD_ID
        adView.adSize = adSize

        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
    }

    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }
}
