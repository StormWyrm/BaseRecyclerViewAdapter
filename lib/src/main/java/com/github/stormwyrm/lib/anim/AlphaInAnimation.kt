package com.github.stormwyrm.lib.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class AlphaInAnimation(val formAlpah: Float = 0f) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "alpha", formAlpah, 1f))
    }
}