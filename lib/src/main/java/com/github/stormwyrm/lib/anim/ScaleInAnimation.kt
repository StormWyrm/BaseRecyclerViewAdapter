package com.github.stormwyrm.lib.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class ScaleInAnimation(val mFrom : Float = 0.5f) : BaseAnimation {
    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view,"scaleX",mFrom,1f)
        val scaleY = ObjectAnimator.ofFloat(view,"scaleY",mFrom,1f)
        return arrayOf(scaleX,scaleY)
    }
}