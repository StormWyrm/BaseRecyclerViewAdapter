package com.github.stormwyrm.lib.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class SlideInRightAnimation : BaseAnimation {
    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat() / 2, 0f))
    }
}