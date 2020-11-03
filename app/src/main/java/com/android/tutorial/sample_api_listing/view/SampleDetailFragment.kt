package com.android.tutorial.sample_api_listing.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.android.tutorial.sample_api_listing.R
import com.android.tutorial.sample_api_listing.models.Shop
import com.android.tutorial.sample_api_listing.util.setDrawable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.f_sample_detail.*
import kotlin.math.abs
import kotlin.math.roundToInt

class SampleDetailFragment : Fragment() {

    private var position: Int = 0
    private var shop: Shop? = null

    private var appBarTitleListener: AppBarLayout.OnOffsetChangedListener? = null
    private var appBarLayout: AppBarLayout? = null

    // For collapsing toolbar layout used to show/hide title
    private var isShow = true
    private var scrollRange = -1

    companion object {
        fun newInstance(pos: Int, shop: Shop): SampleDetailFragment {
            val fragment = SampleDetailFragment()
            val args = Bundle().apply {
                this.putInt("position", pos)
                this.putParcelable("shop", shop)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_sample_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            // Get parameters from arguments of the fragment
            this.position = it.getInt("position")
            this.shop = it.getParcelable("shop")

            // Setup Toolbar
            val collapsingToolbar = view.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
            val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
            toolbar?.title = this.shop?.productName
            toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            val toolbarImage = view.findViewById<ImageView>(R.id.toolbar_image)
            this.appBarLayout = view.findViewById(R.id.appbar)

            this.appBarTitleListener = AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1){
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbar.title = this.shop?.productName
                    isShow = true
                } else if (isShow){
                    collapsingToolbar.title = ""
                    isShow = false
                }
            }
            this.appBarLayout?.addOnOffsetChangedListener(this.appBarTitleListener)

            // Setup view of the body content
            val tvDetail = view.findViewById<TextView>(R.id.tv_detail)
            val tvNearYou = view.findViewById<TextView>(R.id.tv_near_you)

            val ratingHolder = IconTextHolder(view.findViewById(R.id.l_rating))
            val promoHolder = IconTextHolder(view.findViewById(R.id.l_promotion))
            val distanceHolder = IconTextHolder(view.findViewById(R.id.l_distance))

            this.shop?.let { shop ->
                Picasso.get().load(shop.imageUrl).into(toolbarImage)
            }

            tvDetail.text = this.shop?.productDesc ?: "-"
            tvNearYou.text = "${this.shop?.outletAround} 家商家在您附近"

            ratingHolder.ivImage.setDrawable(requireContext(), R.drawable.ic_star)
            ratingHolder.tvText.text = this.shop?.star?.toDouble().toString()

            distanceHolder.ivImage.setDrawable(requireContext(), R.drawable.ic_location)
            distanceHolder.tvText.text = this.shop?.distance ?: "-"

            promoHolder.ivImage.setDrawable(requireContext(), R.drawable.ic_gift_card)
            promoHolder.tvText.text = this.shop?.promoDesc ?: "-"
        }
    }

    override fun onStop() {
        super.onStop()
        this.appBarLayout?.removeOnOffsetChangedListener(this.appBarTitleListener)

    }

    inner class IconTextHolder(var iconTextView: View) {
        var ivImage = iconTextView.findViewById<ImageView>(R.id.iv_image)
        var tvText = iconTextView.findViewById<TextView>(R.id.tv_text)
    }

}

