package com.example.asynclayoutdemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlinx.coroutines.*

class AsyncViewStub @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @LayoutRes private var layoutRes: Int = 0
    private var inflateListener: ((View) -> Unit)? = null
    private var inflateJob: Job? = null

    init {
        visibility = GONE
        context.theme.obtainStyledAttributes(attrs, R.styleable.AsyncViewStub, 0, 0).apply {
            try {
                layoutRes = getResourceId(R.styleable.AsyncViewStub_layoutRes, 0).also {
                    if (it == 0) Log.e("AsyncViewStub", "必须指定app:layoutRes属性")
                }
            } finally {
                recycle()
            }
        }
    }

    fun setOnInflateListener(listener: (View) -> Unit) {
        inflateListener = listener
    }

    fun startAsyncInflate() {
        if (layoutRes == 0 || parent !is ViewGroup) {
            Log.e("AsyncViewStub", "加载条件不满足: layoutRes=$layoutRes, parent=${parent?.javaClass?.simpleName}")
            return
        }

        inflateJob = CoroutineScope(Dispatchers.Main.immediate).launch {
            try {
                val view = withContext(Dispatchers.IO) {
                    LayoutInflater.from(context).inflate(layoutRes, parent as ViewGroup, false)
                }

                (parent as ViewGroup).apply {
                    removeView(this@AsyncViewStub)
                    addView(view, layoutParams)
                    view.post { // 确保视图树更新完成
                        inflateListener?.invoke(view)
                    }
                }
            } catch (e: Exception) {
                Log.e("AsyncViewStub", "加载失败: ${e.message}", e)
            }
        }
    }

    override fun onDetachedFromWindow() {
        inflateJob?.cancel("Activity销毁")
        super.onDetachedFromWindow()
    }
}