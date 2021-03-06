package com.yuyashuai.frameanimation

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

/**
 * the frame animation view to handle the animation life circle
 * @see SurfaceView
 * @author yuyashuai   2019-05-16.
 */
class FrameAnimationSurfaceView private constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int,private val animation: FrameAnimation)
    : SurfaceView(context, attributeSet, defStyle), AnimationController by animation {

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0)
            : this(context, attributeSet, defStyle, FrameAnimation(context))

    private var lastStopIndex = 0
    private var lastStopPaths: MutableList<FrameAnimation.PathData>? = null
    /**
     * whether to resume playback
     */
    var restoreEnable = true

    init {
        animation.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        saveAndStop()
    }

    /**
     * stop the animation, save the index when the animation stops playing
     */
    private fun saveAndStop() {
        lastStopPaths = animation.mPaths
        lastStopIndex = stopAnimation()
    }

    /**
     * resume animation
     */
    private fun restoreAndStart() {
        if (lastStopPaths != null && restoreEnable) {
            playAnimation(lastStopPaths!!, lastStopIndex)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        restoreAndStart()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            saveAndStop()
        } else if (visibility == View.VISIBLE) {
            restoreAndStart()
        }
    }
}