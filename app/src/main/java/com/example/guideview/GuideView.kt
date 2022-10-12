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
            xfermode=PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            color=Color.TRANSPARENT
            isAntiAlias=true
        }
    }

    private lateinit var targetViews:Array<View>

    private lateinit var descriptionViews:Array<GuideDescriptionView>
    private var targetIndex=-1
    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs: AttributeSet?):super(context, attrs){
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE,null)
        this.setBackgroundColor(Color.parseColor("#C0000000"))
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

                it.drawRoundRect(
                    target.left.toFloat()-20, target.top.toFloat()-20,
                    target.right.toFloat()+20, target.bottom.toFloat()+20, 30f,30f,erasePaint
                )


            }else{
                this.removeAllViews()

            }

        }
    }

    private fun setDescriptionPosition(targetView:View, description:GuideDescriptionView){
        when(description.tag){
            DescriptionPositionTag.POSITION_TOP->{
                val desViewLp=description.view.layoutParams
                //타겟이 되는 뷰의 위의 위치+ 자신의 높이 - 마진
                description.view.y= (targetView.top-desViewLp.height).toFloat()-description.margin
                description.view.x=(targetView.x+targetView.width/2)-(desViewLp.width*description.anchor)
                this.addView(description.view)
            }

            DescriptionPositionTag.POSITION_BOTTOM->{
                val desViewLp=description.view.layoutParams
                description.view.y= (targetView.bottom).toFloat()+description.margin
                description.view.x=(targetView.x+targetView.width/2)-(desViewLp.width*description.anchor)
                this.addView(description.view)
            }

            DescriptionPositionTag.POSITION_LEFT->{
                val desViewLp=description.view.layoutParams
                description.view.x= (targetView.left-desViewLp.width).toFloat()-description.margin
                description.view.y=(targetView.y+targetView.height/2)-(desViewLp.height*description.anchor)
                this.addView(description.view)
            }

            DescriptionPositionTag.POSITION_RIGHT->{
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

}