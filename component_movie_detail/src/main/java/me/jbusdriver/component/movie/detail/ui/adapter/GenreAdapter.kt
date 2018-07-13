package me.jbusdriver.component.movie.detail.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.adapter.base.BaseViewHolder
import me.jbusdriver.base.KLog
import me.jbusdriver.base.data.AppConfiguration
import me.jbusdriver.base.data.contextMenu.LinkMenu
import me.jbusdriver.base.mvp.bean.Genre
import me.jbusdriver.base.mvp.bean.ILink
import me.jbusdriver.base.mvp.bean.convertDBItem
import me.jbusdriver.base.mvp.bean.des
import me.jbusdriver.base.mvp.model.CollectModel
import me.jbusdriver.base.mvp.ui.adapter.BaseAppAdapter
import me.jbusdriver.component.movie.detail.R

/**
 * Created by Administrator on 2017/7/30.
 */
open class GenreAdapter : BaseAppAdapter<Genre, BaseViewHolder>(R.layout.layout_genre_item) {


    override fun convert(holder: BaseViewHolder, item: Genre) {
        holder.setText(R.id.tv_movie_genre, item.name)
        (holder.getView<TextView>(R.id.tv_movie_genre).background as? GradientDrawable)?.apply {
            setColor(holder.itemView.resources.getColor(R.color.colorPrimary))
        }
    }

    init {


        setOnItemClickListener { _, view, position ->
            data.getOrNull(position)?.let { genre ->
                KLog.d("genre : $genre")
                //todo
                //MovieListActivity.start(view.context, genre)
            }
        }

        setOnItemLongClickListener { adapter, view, position ->
            (adapter.data.getOrNull(position) as? Genre)?.let { item ->
                val action = (if (CollectModel.has((item as ILink).convertDBItem())) LinkMenu.linkActions.minus("收藏")
                else LinkMenu.linkActions.minus("取消收藏")).toMutableMap()

                if (AppConfiguration.enableCategory) {
                    val ac = action.remove("收藏")
                    if (ac != null) {
                        action["收藏到分类..."] = ac
                    }
                }

                MaterialDialog.Builder(view.context).title(item.name).content(item.des)
                        .items(action.keys)
                        .itemsCallback { _, _, _, text ->
                            action[text]?.invoke(item)
                        }.show()
            }
            true
        }
    }
}