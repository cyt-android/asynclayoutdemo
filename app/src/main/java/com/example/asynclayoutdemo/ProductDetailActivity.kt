package com.example.asynclayoutdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {
    // 使用ViewBinding替代手动绑定
    private lateinit var binding: ActivityProductDetailBinding

    // 协程作用域管理
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 三种加载方式选择一种使用
        //loadWithCoroutine()
        //loadWithAsyncInflater()
        //loadWithViewStub()
        loadWithAsyncViewStub()
    }


    // 协程方式加载
//    private fun loadWithCoroutine() = coroutineScope.launch {
//        // 总耗时开始计时
//        val totalStartTime = System.nanoTime()
//
//        // 1. 分段记录每个inflate操作的耗时
//        val (header, headerTime) = withContext(Dispatchers.IO) {
//            val start = System.nanoTime()
//            val view = layoutInflater.inflate(R.layout.layout_header, binding.headerContainer, false)
//            Pair(view, (System.nanoTime() - start) / 1_000_000) // 返回视图和耗时(ms)
//        }
//
//        val (gallery, galleryTime) = withContext(Dispatchers.IO) {
//            val start = System.nanoTime()
//            val view = layoutInflater.inflate(R.layout.layout_gallery, binding.galleryContainer, false)
//            Pair(view, (System.nanoTime() - start) / 1_000_000)
//        }
//
//        val (recommend, recommendTime) = withContext(Dispatchers.IO) {
//            val start = System.nanoTime()
//            val view = layoutInflater.inflate(R.layout.layout_recommend, binding.recommendContainer, false)
//            Pair(view, (System.nanoTime() - start) / 1_000_000)
//        }
//
//        // 2. 主线程添加视图
//        withContext(Dispatchers.Main) {
//            binding.headerContainer.addView(header)
//            binding.galleryContainer.addView(gallery)
//            binding.recommendContainer.addView(recommend)
//        }
//
//        // 3. 计算结果并输出日志
//        val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
//        val totalInflateTime = headerTime + galleryTime + recommendTime
//
//            Log.d("Performance", """
//        ====== Layout Load Time Analysis ======
//        Header inflate: ${headerTime}ms
//        Gallery inflate: ${galleryTime}ms
//        Recommend inflate: ${recommendTime}ms
//        --------------------------
//        Total inflate time: ${totalInflateTime}ms
//        Total method time: ${totalTimeMs}ms
//        ==========================
//    """.trimIndent())
//    }



    // 推荐商品加载
    private fun loadRecommendations() {
        val recommendView = layoutInflater.inflate(
            R.layout.layout_recommend,
            binding.recommendContainer,
            false
        )
        binding.recommendContainer.addView(recommendView)
    }




//    private fun loadWithAsyncInflater() {
//        val totalStartTime = System.nanoTime()
//        val inflateTimes = mutableListOf<Long>() // 存储各inflate操作的耗时
//
//        // 1. 加载Header
//        val headerStartTime = System.nanoTime()
//        AsyncLayoutInflater(this).inflate(R.layout.layout_header, binding.headerContainer) { view, _, _ ->
//            val headerTimeMs = (System.nanoTime() - headerStartTime) / 1_000_000
//            inflateTimes.add(headerTimeMs)
//            binding.headerContainer.addView(view)
//
//            // 2. 加载Gallery
//            val galleryStartTime = System.nanoTime()
//            AsyncLayoutInflater(this).inflate(R.layout.layout_gallery, binding.galleryContainer) { view, _, _ ->
//                val galleryTimeMs = (System.nanoTime() - galleryStartTime) / 1_000_000
//                inflateTimes.add(galleryTimeMs)
//                binding.galleryContainer.addView(view)
//
//                // 3. 加载Recommend
//                val recommendStartTime = System.nanoTime()
//                AsyncLayoutInflater(this).inflate(R.layout.layout_recommend, binding.recommendContainer) { view, _, _ ->
//                    val recommendTimeMs = (System.nanoTime() - recommendStartTime) / 1_000_000
//                    inflateTimes.add(recommendTimeMs)
//                    binding.recommendContainer.addView(view)
//
//                    // 所有加载完成后打印结果
//                    val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
//                    Log.d("Performance", """
//                    ====== Async Inflate Time Analysis ======
//                    Header inflate: ${inflateTimes[0]}ms
//                    Gallery inflate: ${inflateTimes[1]}ms
//                    Recommend inflate: ${inflateTimes[2]}ms
//                    --------------------------
//                    Total inflate time: ${inflateTimes.sum()}ms
//                    Total method time: $totalTimeMs ms
//                    ==========================
//                """.trimIndent())
//                }
//            }
//        }
//    }


     //ViewStub方式加载
//    private fun loadWithViewStub() {
//         val totalStartTime = System.nanoTime()
//         val inflateDurations = LongArray(3) // [header, gallery, recommend]
//         val headerStartTime = System.nanoTime()
//         binding.headerViewStub.setOnInflateListener { stub, inflated ->
//             inflateDurations[0] = (System.nanoTime() - headerStartTime) / 1_000_000
//             Log.d("ViewStubTiming", "Header inflated in ${inflateDurations[0]}ms")
//         }
//         binding.headerViewStub.inflate()
//         //2. 加载Gallery
//         val galleryStartTime = System.nanoTime()
//        binding.galleryViewStub.setOnInflateListener { stub, inflated ->
//            // 可在此初始化Gallery内容（如设置图片等）
//            inflateDurations[1] = (System.nanoTime() - galleryStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Gallery inflated in ${inflateDurations[1]}ms")
//        }
//        binding.galleryViewStub.inflate()
//         val recommendStartTime = System.nanoTime()
//        binding.recommendViewStub.setOnInflateListener { stub, inflated ->
//            inflateDurations[2] = (System.nanoTime() - recommendStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Recommend inflated in ${inflateDurations[2]}ms")
//        }
//        binding.recommendViewStub.inflate()
//         // 所有加载完成后打印结果
//                    val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
//                    Log.d("Performance", """
//                    ====== Async Inflate Time Analysis ======
//                    Header inflate: ${inflateDurations[0]}ms
//                    Gallery inflate: ${inflateDurations[1]}ms
//                    Recommend inflate: ${inflateDurations[2]}ms
//                    --------------------------
//                    Total inflate time: ${inflateDurations.sum()}ms
//                    Total method time: $totalTimeMs ms
//                    ==========================
//                """.trimIndent())
//    }




//    private fun loadWithAsyncViewStub() {
//        val totalStartTime = System.nanoTime()
//        val inflateDurations = LongArray(3) // 存储三个inflate操作的耗时 [header, gallery, recommend]
//        var inflateCount = 0
//
//        // 1. 异步加载Header
//
//        // 设置Header加载监听
//        binding.headerViewStub.setOnInflateListener { inflatedView ->
////            inflatedView.findViewById<TextView>(R.id.tv_title)?.text = "商品标题"
//
//        }
//        val headerStartTime = System.nanoTime()
//        binding.headerViewStub.startAsyncInflate()
//        inflateDurations[0] = (System.nanoTime() - headerStartTime) / 1_000_000
//        Log.d("ViewStubTiming", "Header async inflated in ${inflateDurations[0]}ms")
//
//        // 设置Gallery加载监听
//        val galleryStartTime = System.nanoTime()
//        binding.galleryViewStub.setOnInflateListener { inflatedView ->
//            inflateDurations[1] = (System.nanoTime() - galleryStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Gallery async inflated in ${inflateDurations[1]}ms")
//        }
//        binding.galleryViewStub.startAsyncInflate()
//
//        // 设置Recommend加载监听
//        val recommendStartTime = System.nanoTime()
//        binding.recommendViewStub.setOnInflateListener { inflatedView ->
//            inflateDurations[2] = (System.nanoTime() - recommendStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Recommend async inflated in ${inflateDurations[2]}ms")
//        }
//        binding.recommendViewStub.startAsyncInflate()
//        val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
//        Log.d("Performance", """
//       ====== Async Inflate Time Analysis ======
//                    Header inflate: ${inflateDurations[0]}ms
//                    Gallery inflate: ${inflateDurations[1]}ms
//                    Recommend inflate: ${inflateDurations[2]}ms
//                    --------------------------
//                    Total inflate time: ${inflateDurations.sum()}ms
//                    Total method time: $totalTimeMs ms
//                    ==========================
//    """.trimIndent())
//    }



//    private fun loadWithAsyncViewStub() {
//        val totalStartTime = System.nanoTime()
//        val inflateDurations = LongArray(3)
//        var inflateCount = 0
//
//        // Header加载
//        binding.headerViewStub.setOnInflateListener { inflatedView ->
//            inflateDurations[0] = (System.nanoTime() - totalStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Header async inflated in ${inflateDurations[0]}ms")
//            if (++inflateCount == 3) printFinalSummary(totalStartTime, inflateDurations)
//        }
//        binding.headerViewStub.startAsyncInflate()
//
//        // Gallery加载（延迟500ms模拟网络请求）
//        binding.galleryViewStub.setOnInflateListener { inflatedView ->
//            inflateDurations[1] = (System.nanoTime() - totalStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Gallery async inflated in ${inflateDurations[1]}ms")
//            if (++inflateCount == 3) printFinalSummary(totalStartTime, inflateDurations)
//        }
//        binding.galleryViewStub.startAsyncInflate()
//
//        // Recommend加载
//        binding.recommendViewStub.setOnInflateListener { inflatedView ->
//            inflateDurations[2] = (System.nanoTime() - totalStartTime) / 1_000_000
//            Log.d("ViewStubTiming", "Recommend async inflated in ${inflateDurations[2]}ms")
//            if (++inflateCount == 3) printFinalSummary(totalStartTime, inflateDurations)
//        }
//        binding.recommendViewStub.startAsyncInflate()
//    }
//
//    private fun printFinalSummary(totalStartTime: Long, inflateDurations: LongArray) {
//        val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
//        Log.d("Performance", """
//        ====== Async Inflate Time Analysis ======
//        Header inflate: ${inflateDurations[0]}ms
//        Gallery inflate: ${inflateDurations[1]}ms
//        Recommend inflate: ${inflateDurations[2]}ms
//        --------------------------
//        Total inflate time: ${inflateDurations.maxOrNull()}ms (实际并行加载)
//        Total method time: $totalTimeMs ms
//        ==========================
//    """.trimIndent())
//    }


    private fun loadWithAsyncViewStub() {
        val totalStartTime = System.nanoTime()
        val inflateDurations = LongArray(3)
        val individualStartTimes = LongArray(3)
        var inflateCount = 0

        // Header inflation
        individualStartTimes[0] = System.nanoTime()
        binding.headerViewStub.setOnInflateListener { inflatedView ->
            inflateDurations[0] = (System.nanoTime() - individualStartTimes[0]) / 1_000_000
            Log.d("InflateTiming", "Header: ${inflateDurations[0]}ms")
            if (++inflateCount == 3) printSummary(totalStartTime, inflateDurations)
        }

        // Gallery inflation
        individualStartTimes[1] = System.nanoTime()
        binding.galleryViewStub.setOnInflateListener { inflatedView ->
            inflateDurations[1] = (System.nanoTime() - individualStartTimes[1]) / 1_000_000
            Log.d("InflateTiming", "Gallery: ${inflateDurations[1]}ms")
            if (++inflateCount == 3) printSummary(totalStartTime, inflateDurations)
        }

        // Recommend inflation
        individualStartTimes[2] = System.nanoTime()
        binding.recommendViewStub.setOnInflateListener { inflatedView ->
            inflateDurations[2] = (System.nanoTime() - individualStartTimes[2]) / 1_000_000
            Log.d("InflateTiming", "Recommend: ${inflateDurations[2]}ms")
            if (++inflateCount == 3) printSummary(totalStartTime, inflateDurations)
        }

        // Start all inflations together
        binding.headerViewStub.startAsyncInflate()
        binding.galleryViewStub.startAsyncInflate()
        binding.recommendViewStub.startAsyncInflate()
    }

    private fun printSummary(totalStartTime: Long, inflateDurations: LongArray) {
        val totalTimeMs = (System.nanoTime() - totalStartTime) / 1_000_000
        Log.d("Performance", """
        ====== Inflation Results ======
        Header: ${inflateDurations[0]}ms
        Gallery: ${inflateDurations[1]}ms
        Recommend: ${inflateDurations[2]}ms
        --------------------------
        Longest: ${inflateDurations.maxOrNull()}ms
        Total: $totalTimeMs ms
        ==========================
    """.trimIndent())
    }



    companion object {
        @JvmStatic
        @BindingAdapter("asyncVisible")
        fun View.setAsyncVisible(visible: Boolean) {
            isVisible = visible
            if (visible && this is AsyncViewStub) {
                startAsyncInflate()
            }
        }
    }
}

// 简化版ViewBinding（实际项目应使用自动生成的绑定类）
data class ActivityProductDetailBinding(
    val root: View,
    val headerContainer: ViewGroup,
    val galleryContainer: ViewGroup,
    val recommendContainer: ViewGroup,
    val headerViewStub: AsyncViewStub,
    val galleryViewStub: AsyncViewStub,
    val recommendViewStub: AsyncViewStub
//    val headerViewStub: ViewStub,
//    val galleryViewStub: ViewStub,
//    val recommendViewStub: ViewStub

) {
    companion object {
        fun inflate(layoutInflater: LayoutInflater): ActivityProductDetailBinding {
            val root = layoutInflater.inflate(
                R.layout.activity_product_detail,
                null,
                false
            )
            return ActivityProductDetailBinding(
                root = root,
                headerContainer = root.findViewById(R.id.headerContainer),
                galleryContainer = root.findViewById(R.id.galleryContainer),
                recommendContainer = root.findViewById(R.id.recommendContainer),
                headerViewStub = root.findViewById(R.id.headerViewStub),
                galleryViewStub = root.findViewById(R.id.galleryViewStub),
                recommendViewStub = root.findViewById(R.id.recommendViewStub)

            )
        }
    }
}
