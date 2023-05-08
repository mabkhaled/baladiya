package com.example.baladeyti.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.example.baladeyti.models.Claim
import com.example.baladeyti.models.Municipality
import com.google.android.material.imageview.ShapeableImageView
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class ClaimAdapter(
    private val listClaim: List<Claim>,
    private val listener: OnItemClickListener,
    private val savedInstance: Bundle?,
    private val context: Context?
) :
    RecyclerView.Adapter<ClaimAdapter.MyViewHolder>() {
    lateinit var idTopic: TextView
    lateinit var point: LatLng
    lateinit var map: MapboxMap
    lateinit var idContent: TextView
    lateinit var idPosition: MapView
    lateinit var recyclerImage: ShapeableImageView

    val DayInMilliSec = 86400000

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        @SuppressLint("ResourceAsColor")
        fun bind(property: Claim) {

            idTopic = itemView.findViewById(R.id.idTopic)
            idContent = itemView.findViewById(R.id.idContent)
            idPosition = itemView.findViewById(R.id.idPosition)
            recyclerImage = itemView.findViewById(R.id.recyclerImage)
            val picStr: String = property.photos.toString()
            val picStrr =
                "http://192.168.1.3:3000/upload/" + picStr.split("/")[4]
            println(picStrr)
            Glide.with(itemView).load(Uri.parse(picStrr)).into(recyclerImage)
            idTopic.text = property.name
            idContent.text = property.text

            idPosition.getMapAsync {

                map = it

                map.setStyle(Style.OUTDOORS)

                map.animateCamera(
                    com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(
                        LatLng(property.laltitude!!.toDouble(), property.longitude!!.toDouble()),
                        13.0
                    )
                )
            }
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, listClaim)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, property: List<Claim>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.one_item_card_claim, parent, false)
        )
    }

    override fun getItemCount(): Int {
        println(listClaim.size)
        Mapbox.getInstance(context!!,"pk.eyJ1IjoiZ2hhemktZGV2IiwiYSI6ImNsMnRpazJjajAwYnYzaW14b2pteWl4dG0ifQ.ZCKANo62f6OqWbfT64UXEg")

        return listClaim.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(listClaim.get(position))
    }


    /*@SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/YYYY")
            val netDate = Date(s.toLong()).addDays(1)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun Date.addDays(numberOfDaysToAdd: Int): Date {
        return Date(this.time + numberOfDaysToAdd * DayInMilliSec)
    }*/
}