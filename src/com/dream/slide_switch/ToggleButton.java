package com.dream.slide_switch;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View{

	private Bitmap toggleBg;//开关的背景图片
	private Bitmap slideImage;//滑动的图片
	private float currentX;//当前触摸的x坐标
	private boolean isSliding = false;//用来表示当前是否正在滑动中
	
	public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ToggleButton(Context context) {
		super(context);
	}
	/**
	 * 定义状态常量
	 *
	 */
	enum ToggleState{
		Open,Close
	}
	
	private ToggleState mState;//用来记录当前ToggleButton的state
	
	/**
	 * 设置ToggleButton的状态
	 * @param mState
	 */
	public void setToggleState(ToggleState mState){
		this.mState = mState;
	}
	
	/**
	 * 在该方法中设置当前View的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//设置自身的宽高
		setMeasuredDimension(toggleBg.getWidth(), toggleBg.getHeight());
	}
	
	/**
	 * 用来控制ToggleButton绘制的内容
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//1.绘制背景图片
		canvas.drawBitmap(toggleBg, 0, 0,null);
		//2.绘制滑动图片
		if(isSliding){
			//处理滑动中的绘制逻辑
			float left = currentX - slideImage.getWidth()/2;
			if(left<0) left = 0;//限制左边
			if(left>(toggleBg.getWidth()-slideImage.getWidth())){
				left = toggleBg.getWidth()-slideImage.getWidth();//限制右边
			}
			canvas.drawBitmap(slideImage, left, 0,null);
		}else {
			//处理抬起后的绘制逻辑
			if(mState==ToggleState.Open){
				canvas.drawBitmap(slideImage, toggleBg.getWidth()-slideImage.getWidth(), 0,null);
			}else {
				canvas.drawBitmap(slideImage, 0, 0, null);
			}
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSliding = true;
			currentX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			isSliding = true;
			currentX = event.getX();//move的时候需要更新当前的currentX
			break;
		case MotionEvent.ACTION_UP:
			isSliding = false;
			//获取SLideImage处于正中间的时候的left值
			if(currentX<toggleBg.getWidth()/2){
				//在左半边，应该向左边靠
				//判断当前的状态是否已经是close
				if(mState!=ToggleState.Close){
					mState = ToggleState.Close;//更改状态
					if(listener!=null){
						listener.onToggleStateChange(mState);//回调接口方法
					}
				}
			}else {
				//在右半边，应该向右边靠
				if(mState!=ToggleState.Open){
					mState = ToggleState.Open;
					if(listener!=null){
						listener.onToggleStateChange(mState);
					}
				}
			}
			break;
		}
		invalidate();//刷新，引起onDraw调用
		return true;
	}
	
	/**
	 * 设置当前开关控件的背景图片
	 * @param resId
	 */
	public void setToggleBackgroundResource(int resId){
		toggleBg = BitmapFactory.decodeResource(getResources(), resId);
	}
	
	/**
	 * 设置滑动的图片
	 * @param resId
	 */
	public void setSlideImage(int resId){
		slideImage = BitmapFactory.decodeResource(getResources(), resId);
	}
	
	private OnToggleStateChangeListener listener;
	/**
	 * 设置状态改变的监听器
	 * @param listener
	 */
	public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener){
		this.listener = listener;
	}
	
	/**
	 * 定义接口回调，暴漏自己的状态
	 *
	 */
	public interface OnToggleStateChangeListener{
		void onToggleStateChange(ToggleState mState);
	}
}
