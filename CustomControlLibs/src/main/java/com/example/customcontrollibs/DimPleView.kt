package com.example.customcontrollibs

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import java.lang.StrictMath.sin
import java.util.*

/**
 * Author: fuxinbo

 * Date: 2021/9/27

 * Description: 类似网易云音乐粒子扩散效果
 */
class DimPleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var mWidth = 0f
    private var mHeight = 0f
    private var particleList = mutableListOf<Particle>()
    private var animator = ValueAnimator.ofFloat(0f, 1f)
    private var paint = Paint()
    private var path = Path()
    private val pathMeasure = PathMeasure()//路径，用于测量扩散圆某一处的X,Y值
    private var pos = FloatArray(2) //扩散圆上某一点的x,y
    private val tan = FloatArray(2)//扩散圆上某一点切线
    private val random = Random()
    private var particleNumber = 1000//粒子数量
    private var particleRadius = 2.2f//粒子半径
    private var diffusionRadius = 268f//扩散圆半径
    private var radius = 100;//粒子扩散长度

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DimPleView)
        array.run {
            setParticleNumber(getInteger(R.styleable.DimPleView_particlesNum, 1000))
            setParticleRadius(getFloat(R.styleable.DimPleView_particlesRadius, 2.2f))
            setDiffusionRadius(getFloat(R.styleable.DimPleView_particlesDiffusionRadius, 350f))
            setStrokeOffSet(getInt(R.styleable.DimPleView_strokeOffSet, 100))
            setStrokeColor(getInt(R.styleable.DimPleView_strokeColor, R.color.While))
        }
        animator.duration = 2000
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateParticle()
            invalidate()
        }
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        paint.isAntiAlias = true
        array.recycle()
    }

    /**
     * 设置粒子数量
     *
     * @param value 粒子数量(推荐数量为1k以上)
     */
    private fun setParticleNumber(value: Int) {
        this.particleNumber = value
    }

    /**
     * 设置粒子半径半径
     *
     * @param value 单个粒子的半径(默认为2.2f)
     */
    private fun setParticleRadius(value: Float) {
        this.particleRadius = value
    }

    /**
     * 设置扩散圆半径
     *
     * @param value 整体粒子效果扩散半径
     */
    private fun setDiffusionRadius(value: Float) {
        this.diffusionRadius = value
    }

    /**
     * 设置粒子颜色
     *
     * @param value 单个粒子的颜色
     */
    fun setStrokeColor(@ColorInt value: Int) {
        paint.color = value
    }

    /**
     * 设置粒子扩散长度
     *
     * @param value 设置整体粒子的漂浮距离
     */
    fun setStrokeOffSet(value: Int) {
        radius = value
    }

    /**
     * 更新粒子
     *
     */
    private fun updateParticle() {
        particleList.forEachIndexed { _, particle ->
            if (particle.offSet > particle.maxOffSet) {
                particle.offSet = 0f
                particle.speed = random.nextInt(3) + 1.5f
                particle.maxOffSet = random.nextInt(radius).toFloat()
            }
            particle.x =
                (mWidth / 2 + kotlin.math.cos(particle.angle) * (diffusionRadius + particle.offSet)).toFloat() + particle.offSetX * particle.direction

            if (particle.y > mHeight / 2) {
                particle.y =
                    (sin(particle.angle) * (diffusionRadius + particle.offSet) + mHeight / 2).toFloat()
            } else {
                particle.y =
                    (mHeight / 2 - sin(particle.angle) * (diffusionRadius + particle.offSet)).toFloat()
            }

            particle.offSet += particle.speed
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        particleList.forEachIndexed { _, particle ->
            if (particle.offSet > 5f) {
                paint.alpha = ((1f - particle.offSet / particle.maxOffSet) * 0.8 * 225f).toInt()
                canvas.drawCircle(particle.x, particle.y, particle.radius, paint)
            } else {
                paint.alpha = 225
            }
            canvas.drawCircle(particle.x, particle.y, particle.radius, paint)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        path.addCircle(mWidth / 2, mHeight / 2, diffusionRadius, Path.Direction.CCW)
        pathMeasure.setPath(path, false)
        for (i in 0..particleNumber) {
            pathMeasure.getPosTan(i / particleNumber.toFloat() * pathMeasure.length, pos, tan)
            particleList.add(
                Particle(
                    pos[0] + random.nextInt(6) - 3f,
                    pos[1] + random.nextInt(6) - 3f,
                    particleRadius,
                    random.nextInt(3).toFloat(),
                    random.nextInt(radius).toFloat(),
                    random.nextInt(3) - 1.5f,
                    random.nextInt(2) + 0.5f,
                    kotlin.math.acos(((pos[0] - mWidth / 2) / diffusionRadius).toDouble()),
                    random.nextInt(radius) + 0f
                )
            )
        }
        animator.start()
    }


}