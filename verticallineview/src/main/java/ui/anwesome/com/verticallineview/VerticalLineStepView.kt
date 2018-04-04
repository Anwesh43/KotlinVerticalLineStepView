package ui.anwesome.com.verticallineview

/**
 * Created by anweshmishra on 05/04/18.
 */
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.MotionEvent

class VerticalLineStepView (ctx : Context) : View(ctx) {

    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {
        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(prevScale - scale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {
        fun animate (updatecb : () -> Unit) {
            if (animated) {
                try {
                    updatecb()
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start () {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop () {
            if (animated) {
                animated = false
            }
        }
    }

    data class VerticalLineStep (var i : Int, val state : State = State()) {
        fun draw (canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val n : Int = 4
            val w_gap : Float = w / (2 * n)
            val h_gap : Float = h / (2 * n)
            canvas.save()
            canvas.translate(w/2, h/2)
            for (i in 0..n) {
                canvas.save()
                canvas.translate(-state.scale * (w_gap) * i, -h_gap * i)
                canvas.drawLine(0f, 0f, 0f , -h_gap, paint)
                canvas.restore()
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer(var view : VerticalLineStepView) {
        val step : VerticalLineStep = VerticalLineStep(0)
        val animator : Animator = Animator(view)
        fun render (canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            paint.color = Color.parseColor("#ef5350")
            paint.strokeWidth = 5f
            paint.strokeCap = Paint.Cap.ROUND
            step.draw(canvas, paint)
            animator.animate {
                step.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            step.startUpdating {
                animator.stop()
            }
        }
    }
    companion object {
        fun create(activity : Activity) {
            val view = VerticalLineStepView(activity)
            activity.setContentView(view)
        }
    }
}