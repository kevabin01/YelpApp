package com.codequark.yelp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.annotation.NonNull
import com.codequark.yelp.databinding.DialogLoadingBinding

class LoadingDialog private constructor(
    @NonNull
    context: Context,
): Dialog(context) {
    companion object {
        const val ROTATE_ANIMATION_DURATION = 800
    }

    @NonNull
    private val rotate = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )

    private lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun show() {
        super.show()

        binding.progressBar.startAnimation(getRotation())
    }

    override fun cancel() {
        super.cancel()

        binding.progressBar.clearAnimation()
    }

    @NonNull
    fun getRotation(): RotateAnimation {
        rotate.duration = ROTATE_ANIMATION_DURATION.toLong()
        rotate.repeatCount = Animation.INFINITE

        return rotate
    }

    class Builder(@NonNull context: Context) {
        @NonNull
        private val dialog: LoadingDialog = LoadingDialog(context)

        fun create() {
            if(dialog.isShowing) {
                Log.d("Kevin", "Mostrando dialogo")
            } else {
                dialog.show()
            }
        }

        fun cancel() {
            if(dialog.isShowing) {
                dialog.cancel()
            }
        }
    }
}