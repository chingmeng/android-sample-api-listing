package com.android.tutorial.sample_api_listing.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.tutorial.sample_api_listing.R
import com.android.tutorial.sample_api_listing.models.Shop
import com.android.tutorial.sample_api_listing.presenter.APIManager
import com.android.tutorial.sample_api_listing.util.setDrawable
import com.android.tutorial.sample_api_listing.util.show
import com.squareup.picasso.Picasso

class SampleListingFragment : Fragment() {

    private var adapter: SampleAdapter? = null
    private var tvBlank: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_sample_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
        toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Under construction....", Toast.LENGTH_SHORT).show()
        }

        // Setup blank view and listing view
        this.tvBlank = view.findViewById(R.id.tv_blank)

        view.findViewById<RecyclerView>(R.id.list).apply {

            this.layoutManager = LinearLayoutManager(requireContext())

            val dividerItemDecoration = DividerItemDecoration(requireContext(), (this.layoutManager as LinearLayoutManager).orientation)
            this.addItemDecoration(dividerItemDecoration)
            this@SampleListingFragment.adapter = SampleAdapter(requireActivity()).apply {
                this.clickListener = { pos ->
                    SampleDetailFragment.newInstance(pos, this.itemAt(pos)).apply {
                        this.show(this@SampleListingFragment.requireActivity())
                    }
                }
            }
            this.adapter = this@SampleListingFragment.adapter
        }
    }

    override fun onResume() {
        super.onResume()

        // Get listing from server via APIManager
        APIManager.getListing { success, shops ->
            if (success) {
                shops?.let {
                    this.adapter?.list = shops.toSet().toList()
                    this.adapter?.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(requireContext(), "Connection error.", Toast.LENGTH_SHORT).show()
            }
           this.tvBlank?.visibility = View.GONE
        }
    }

}

class SampleAdapter(private var context: Context, var list: List<Shop> = emptyList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListener: (Int) -> Unit = {}

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivShop: ImageView = view.findViewById(R.id.iv_shop)
        var tvShopClosed: TextView = view.findViewById(R.id.tv_shop_close)
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvDetail: TextView = view.findViewById(R.id.tv_detail)
        var tvNearYou: TextView = view.findViewById(R.id.tv_near_you)

        var lRating: View = view.findViewById(R.id.l_rating)
        var lPromotion: View = view.findViewById(R.id.l_promotion)
        var lDistance: View = view.findViewById(R.id.l_distance)

        var ratingHolder = IconTextHolder(lRating)
        var promoHolder = IconTextHolder(lPromotion)
        var distanceHolder = IconTextHolder(lDistance)

        init {
            itemView.setOnClickListener {
                this@SampleAdapter.clickListener.invoke(adapterPosition)
            }
        }

        inner class IconTextHolder(var iconTextView: View) {
            var ivImage = iconTextView.findViewById<ImageView>(R.id.iv_image)
            var tvText = iconTextView.findViewById<TextView>(R.id.tv_text)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.i_sample, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ItemHolder) {

            val item = list[position]

            Picasso.get().load(item.imageUrl).into(holder.ivShop)

            holder.tvTitle.text = item.productName
            holder.tvDetail.text = item.productDesc

            holder.ratingHolder.ivImage.setDrawable(context, R.drawable.ic_star)
            holder.ratingHolder.tvText.text = item.star.toDouble().toString()

            holder.promoHolder.ivImage.setDrawable(context, R.drawable.ic_gift_card)
            holder.promoHolder.tvText.text = item.promoDesc

            holder.distanceHolder.ivImage.setDrawable(context, R.drawable.ic_location)
            holder.distanceHolder.tvText.text = item.distance

            if (item.outletAround != 0) {
                holder.tvNearYou.visibility = View.VISIBLE
                holder.tvNearYou.text = "${item.outletAround} 家商家在您附近"
            } else {
                holder.tvNearYou.visibility = View.GONE
            }

            if (item.closeLabel.isNotBlank()) {
                holder.tvShopClosed.visibility = View.VISIBLE
                holder.tvShopClosed.text = item.closeLabel
            } else {
                holder.tvShopClosed.visibility = View.GONE
            }
        }
    }


    fun itemAt(position: Int): Shop {
        return this.list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}