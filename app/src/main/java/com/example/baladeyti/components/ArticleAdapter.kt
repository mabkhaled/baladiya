package com.example.baladeyti.components

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baladeyti.R
import com.example.baladeyti.models.Article
import com.example.baladeyti.models.Municipality
import com.google.android.material.imageview.ShapeableImageView

class ArticleAdapter(
    private val listArticle: List<Article>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {
    lateinit var idName: TextView
    lateinit var idphone: TextView
    lateinit var idEmail: TextView
    lateinit var recyclerImage: ShapeableImageView

    val DayInMilliSec = 86400000

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        @SuppressLint("ResourceAsColor")
        fun bind(property: Article) {

            idName = itemView.findViewById(R.id.idName)
            idEmail = itemView.findViewById(R.id.idEmail)
            recyclerImage = itemView.findViewById(R.id.recyclerImage)
            val picStr: String = property.photos.toString()
            val picStrr="http://192.168.1.3:3000/upload/"+picStr.split("/")[4]
            println(picStrr)
            Glide.with(itemView).load(Uri.parse(picStrr)).into(recyclerImage)
            idName.text = property.text
            idEmail.text = property.designation
            /*  it*/
//            if(property.type.equals("Lost")){
//                itemView.article.setBackgroundColor(Color.RED)
//            }else{
//                itemView.article.setBackgroundColor(Color.GREEN)
//            }
/*
            Glide.with(itemView).load(property.photo).into(itemView.ArticleImage)
*/
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick_a(position, listArticle)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick_a(position: Int, property: List<Article>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.one_item_card_articles, parent, false)
        )
    }

    override fun getItemCount(): Int {
        println(listArticle.size)
        return listArticle.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(listArticle.get(position))
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