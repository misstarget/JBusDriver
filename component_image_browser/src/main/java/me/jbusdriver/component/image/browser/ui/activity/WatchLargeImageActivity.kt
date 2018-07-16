package me.jbusdriver.component.image.browser.ui.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gyf.barlibrary.ImmersionBar
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.image_browser_activity_watch_large_image.*
import kotlinx.android.synthetic.main.image_browser_layout_large_image_item.view.*
import me.jbusdriver.base.*
import me.jbusdriver.base.common.BaseActivity
import me.jbusdriver.base.common.toGlideUrl
import me.jbusdriver.base.http.OnProgressListener
import me.jbusdriver.base.http.addProgressListener
import me.jbusdriver.base.http.removeProgressListener
import me.jbusdriver.component.image.browser.R
import java.io.File
import java.util.concurrent.TimeUnit


class WatchLargeImageActivity : BaseActivity() {

    private val urls by lazy {
        intent.getStringArrayListExtra(INTENT_IMAGE_URL) ?: emptyList<String>()
    }
    private val imageViewList: ArrayList<View> = arrayListOf()
    private val index by lazy { intent.getIntExtra(INDEX, -1) }
    private val imageSaveDir by lazy {
        val pathSuffix = File.separator + "download" + File.separator + "image" + File.separator
        createDir(Environment.getExternalStorageDirectory().absolutePath + File.separator + this.packageName + pathSuffix)
                ?: createDir(this.externalCacheDir.absolutePath + this.packageName + pathSuffix)
                ?: error("cant not create collect dir in anywhere")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_browser_activity_watch_large_image)
        initWidget()
    }


    @SuppressLint("SetTextI18n")
    private fun initWidget() {
        immersionBar.transparentBar().init()
        val statusBarHeight = ImmersionBar.getStatusBarHeight(this)

        urls.mapTo(imageViewList) {
            this@WatchLargeImageActivity.inflate(R.layout.image_browser_layout_large_image_item).apply {
                (pb_hor_progress.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = statusBarHeight
            }
        }

        vp_largeImage.adapter = MyViewPagerAdapter()
        vp_largeImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                this@WatchLargeImageActivity.tv_url_index.text = "${position + 1} / ${imageViewList.size}"
            }
        })
        vp_largeImage.currentItem = if (index == -1) 0 else index
        this@WatchLargeImageActivity.tv_url_index.text = "${vp_largeImage.currentItem + 1} / ${imageViewList.size}"

        iv_download.setOnClickListener {
            KLog.d("download image ${urls[vp_largeImage.currentItem]}")
            Schedulers.io().scheduleDirect {
                val url = urls[vp_largeImage.currentItem]
                GlideApp.with(this).asFile().load(url).submit()
                        .get(3, TimeUnit.SECONDS)?.let {
                            //copy file
                            val fileName = url.urlPath.split("/").lastOrNull() ?: kotlin.run {
                                viewContext.toast("无法获取文件名！")
                                return@scheduleDirect
                            }
                            it.copyTo(File(imageSaveDir + fileName), true)
                            viewContext.toast("文件保存至${imageSaveDir}下")
                        }
            }.addTo(rxManager)

        }

    }


    companion object {

        private const val INTENT_IMAGE_URL = "INTENT_IMAGE_URL"
        private const val INDEX = "currentIndex"

        fun startShow(context: Context, images: List<String>, index: Int = -1) {
            val intent = Intent(context, WatchLargeImageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.putStringArrayListExtra(INTENT_IMAGE_URL, ArrayList(images))
            intent.putExtra(INDEX, index)
            context.startActivity(intent)
        }

    }

    inner class MyViewPagerAdapter : PagerAdapter() {

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(imageViewList[position])//删除页卡
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return imageViewList.getOrNull(position)?.apply {
                container.addView(this, 0)//添加页卡
                loadImage(this, position)
            } ?: error("can not instantiateItem for $position in $imageViewList")
        }

        private fun loadImage(view: View, position: Int) {
            view.findViewById<View>(R.id.pb_hor_progress)?.animate()?.alpha(1f)?.setDuration(300)?.start()
            val offset = Math.abs(vp_largeImage.currentItem - position)
            val priority = when (offset) {
                in 0..1 -> Priority.IMMEDIATE
                in 2..5 -> Priority.HIGH
                in 6..10 -> Priority.NORMAL
                else -> Priority.LOW
            }
            KLog.d("load $position for ${vp_largeImage.currentItem} offset = $offset : $priority")
            val url = urls[position]
            GlideApp.with(this@WatchLargeImageActivity)
                    .load(url.toGlideUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.image_browser_ic_error)
                    .fitCenter()
                    .priority(priority)
                    .into(object : DrawableImageViewTarget(view.pv_image_large) {
                        val listener = object : OnProgressListener {
                            override fun onProgress(imageUrl: String, bytesRead: Long, totalBytes: Long, isDone: Boolean, exception: GlideException?) {
                                if (totalBytes == 0L) return
                                if (url != imageUrl) return
                                postMain {
                                    //view.pb_hor_progress.visibility = View.GONE
                                    view.pb_hor_progress.isIndeterminate = false
                                    view.pb_hor_progress?.apply {
                                        progress = (bytesRead * 1.0f / totalBytes * 100.0f).toInt()
                                    }
                                }

                                if (isDone) {
                                    removeProgressListener(this)
                                }
                            }
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            view.pb_hor_progress?.animate()?.alpha(1f)?.setDuration(300)?.start()
                            addProgressListener(listener)
                            super.onLoadStarted(placeholder)
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            super.onResourceReady(resource, transition)
                            view.pb_hor_progress?.animate()?.alpha(0f)?.setDuration(300)?.start()
                            removeProgressListener(listener)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            removeProgressListener(listener)
                            view.pb_hor_progress?.animate()?.alpha(0f)?.setDuration(300)?.start()
                        }

                    })
        }

        override fun getCount() = imageViewList.size//返回页卡的数量


        override fun isViewFromObject(arg0: View, arg1: Any) = arg0 === arg1//官方提示这样写
    }


}
