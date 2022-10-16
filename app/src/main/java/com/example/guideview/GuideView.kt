package com.example.guideview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class GuideView:FrameLayout {

    private val erasePaint by lazy{
        Paint().apply{
            //그래픽 그리는 모드
            xfermode=PorterDuffXfermode(PorterDuff.Mode.CLEAR)

            //투명으로 그림
            color=Color.TRANSPARENT
            isAntiAlias=true
        }
    }

    private lateinit var targetViews:Array<View>
    private var targetPadding=arrayOf(20)
    private var lightShape=GuideLightShape.SHAPE_RECT

    //설명을 적는 뷰
    private lateinit var descriptionViews:Array<GuideDescriptionView>
    private var targetIndex=-1
    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs: AttributeSet?):super(context, attrs){
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE,null)
        this.setBackgroundColor(Color.parseColor("#C0000000"))
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(targetPadding.size<targetViews.size){
            targetPadding=Array(targetViews.size){
                targetPadding[targetPadding.size-1]
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        if(targetIndex==-1){
            next()
            return
        }

        canvas?.let {

            if (targetIndex<=targetViews.size-1) {
                val target = targetViews[targetIndex]

                drawShape(it,target)


            }else{
                this.removeAllViews()

            }

        }
    }

    private fun setDescriptionPosition(targetView:View, description:GuideDescriptionView){
        when(description.tag){
            DescriptionPosition.POSITION_TOP->{
                val desViewLp=description.view.layoutParams
                //타겟이 되는 뷰의 위의 위치+ 자신의 높이 - 마진
                description.view.y= (targetView.top-desViewLp.height).toFloat()-description.margin
                description.view.x=(targetView.x+targetView.width/2)-(desViewLp.width*description.anchor)
                this.addView(description.view)
            }

            DescriptionPosition.POSITION_BOTTOM->{
                val desViewLp=description.view.layoutParams
                description.view.y= (targetView.bottom).toFloat()+description.margin
                description.view.x=(targetView.x+targetView.width/2)-(desViewLp.width*description.anchor)
                this.addView(description.view)
            }

            DescriptionPosition.POSITION_LEFT->{
                val desViewLp=description.view.layoutParams
                description.view.x= (targetView.left-desViewLp.width).toFloat()-description.margin
                description.view.y=(targetView.y+targetView.height/2)-(desViewLp.height*description.anchor)
                this.addView(description.view)
            }

            DescriptionPosition.POSITION_RIGHT->{
                val desViewLp=description.view.layoutParams
                description.view.x= (targetView.right).toFloat()+description.margin
                description.view.y=(targetView.y+targetView.height/2)-(desViewLp.height*description.anchor)
                this.addView(description.view)
            }
        }
    }

    fun setTargetViews(views:Array<View>){
        targetViews=views

    }

    fun setTargetShadowPadding(paddings:Array<Int>){
        targetPadding=paddings
    }

    fun setDescriptionViews(descriptions: Array<GuideDescriptionView>){
        descriptionViews=descriptions
    }

    fun next(){
        targetIndex+=1
        this.removeAllViews()
        if (targetIndex<targetViews.size) {
            setDescriptionPosition(targetViews[targetIndex],descriptionViews[targetIndex])
        }else{
            (this.parent as ViewGroup?)?.removeView(this)
        }
    }

    private fun drawShape(canvas: Canvas,target:View){
        when(lightShape){
            GuideLightShape.SHAPE_RECT->{
                canvas.drawRect(
                    target.left.toFloat()-targetPadding[targetIndex],
                    target.top.toFloat()-targetPadding[targetIndex],
                    target.right.toFloat()+targetPadding[targetIndex],
                    target.bottom.toFloat()+targetPadding[targetIndex],erasePaint
                )
            }
            GuideLightShape.SHAPE_RECT_ROUND->{
                canvas.drawRoundRect(
                    target.left.toFloat()-targetPadding[targetIndex],
                    target.top.toFloat()-targetPadding[targetIndex],
                    target.right.toFloat()+targetPadding[targetIndex],
                    target.bottom.toFloat()+targetPadding[targetIndex], 30f,30f,erasePaint
                )
            }
            GuideLightShape.SHAPE_OVAL->{
                canvas.drawOval(
                    target.left.toFloat()-targetPadding[targetIndex],
                    target.top.toFloat()-targetPadding[targetIndex],
                    target.right.toFloat()+targetPadding[targetIndex],
                    target.bottom.toFloat()+targetPadding[targetIndex],erasePaint
                )
            }
        }
    }
    private fun setLightShape(shape: GuideLightShape){
        this.lightShape=shape
    }
}