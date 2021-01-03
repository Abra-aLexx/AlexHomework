package com.abra.homework_four_dot_two

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.collections.ArrayList

class ColoredCircle : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var centerX = 0f
    private var centerY = 0f
    private val radiusSmall = 150f
    private val radiusBig = 400f
    private var rectF = RectF()
    private var colors: ArrayList<Int> = arrayListOf(Color.YELLOW, Color.BLUE, Color.RED, Color.GREEN)
    private val allColors: ArrayList<Int> = arrayListOf(Color.YELLOW, Color.BLUE, Color.RED, Color.GREEN,
            resources.getColor(R.color.dark_yellow), resources.getColor(R.color.aqua),
            resources.getColor(R.color.orange), resources.getColor(R.color.dark_green))
    private val paintSmall = Paint()
    private val arrayPaints: Array<Paint> = arrayOf(Paint(), Paint(), Paint(), Paint())
    /*
     * Решил сделать через Region, потому что не нашел другого решения, если знаете более
     * лучший способ реализации, то буду рад любому совету
     */
    private val regionsArray: Array<Region> = arrayOf(Region(), Region(), Region(), Region())
    private var smallCircleRegion = Region()
    lateinit var showCoordinatesListener: (x: Int, y: Int, view: View, color: Int) -> Unit

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        setCoordinatesAndColors(viewWidth, viewHeight)
    }

    private fun setCoordinatesAndColors(viewWidth: Int, viewHeight: Int) {
        centerX = viewWidth / 2.toFloat()
        centerY = viewHeight / 2.toFloat()
        rectF = RectF(centerX - radiusBig, centerY - radiusBig, centerX + radiusBig, centerY + radiusBig)
        for (i in arrayPaints.indices) {
            arrayPaints[i].color = colors[i]
        }
        setRegions()
        paintSmall.setColor(resources.getColor(R.color.purple))
    }

    private fun setPainters() {
        arrayPaints.forEach {
            it.color = colors[arrayPaints.indexOf(it)]
        }
    }

    private fun setRegions() {
        smallCircleRegion = Region((centerX - radiusSmall).toInt(), (centerY - radiusSmall).toInt(), (centerX + radiusSmall).toInt(), (centerY + radiusSmall).toInt())
        regionsArray[0] = Region(centerX.toInt(), centerY.toInt(), (centerX + radiusBig).toInt(), (centerY + radiusBig).toInt())
        regionsArray[1] = Region((centerX - radiusBig).toInt(), centerY.toInt(), centerX.toInt(), (centerY + radiusBig).toInt())
        regionsArray[2] = Region((centerX - radiusBig).toInt(), (centerY - radiusBig).toInt(), centerX.toInt(), centerY.toInt())
        regionsArray[3] = Region(centerX.toInt(), (centerY - radiusBig).toInt(), (centerX + radiusBig).toInt(), centerY.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawArc(rectF, 0F, 90F, true, arrayPaints[0])
        canvas.drawArc(rectF, 90F, 90F, true, arrayPaints[1])
        canvas.drawArc(rectF, 180F, 90F, true, arrayPaints[2])
        canvas.drawArc(rectF, 270F, 90F, true, arrayPaints[3])
        canvas.drawCircle(centerX, centerY, radiusSmall, paintSmall)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        val action = event?.action
        if (action == MotionEvent.ACTION_DOWN) {
            if (x != null && y != null) {
                if (smallCircleRegion.contains(x.toInt(), y.toInt())) {
                    colors.clear()
                    for (i in 1..4) {
                        colors.add(allColors.random())
                    }
                    showCoordinatesListener(x.toInt(), y.toInt(),this, colors.random())
                    setPainters()
                    invalidate()
                } else {
                    for (i in regionsArray.indices) {
                        if (regionsArray[i].contains(x.toInt(), y.toInt())!!) {
                            colors[i] = allColors.random()
                            showCoordinatesListener(x.toInt(), y.toInt(),this, colors[i])
                            setPainters()
                            invalidate()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}