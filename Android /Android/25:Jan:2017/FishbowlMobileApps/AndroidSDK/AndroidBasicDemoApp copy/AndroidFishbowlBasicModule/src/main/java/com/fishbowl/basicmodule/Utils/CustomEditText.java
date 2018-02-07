package com.fishbowl.basicmodule.Utils;

/**
 * Created by Digvijay Chauhan on 27/06/15.
 */
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class CustomEditText extends EditText
{
  private Drawable dRight;
  private Rect rBounds;
  private boolean cleanTextOnClick = false;

  public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  public CustomEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public CustomEditText(Context context) {
    super(context);
  }

  @Override
  public void setCompoundDrawables(Drawable left, Drawable top,
      Drawable right, Drawable bottom)
  {
    if(right !=null)
    {
      dRight = right;
    }
    super.setCompoundDrawables(left, top, right, bottom);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {

    if((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) && dRight!=null)
    {
      rBounds = dRight.getBounds();
      final int x = (int)event.getX();
      final int y = (int)event.getY();
//      System.out.println("x:/y: "+x+"/"+y);
//      System.out.println("bounds: "+bounds.left+"/"+bounds.right+"/"+bounds.top+"/"+bounds.bottom);
      //check to make sure the touch event was within the bounds of the drawable
      
//      System.out.println("x:/y: "+x+"/"+y);
//      System.out.println("this.getWidth:/this.getHeight(): "+this.getWidth()+"/"+this.getHeight());
     
      
//      (x>=(this.getRight()-rBounds.width()) && x<=(this.getRight()-this.getPaddingRight())
//              && y>=this.getPaddingTop() && y<=(this.getHeight()-this.getPaddingBottom()))
      if(cleanTextOnClick || ((x >= (this.getWidth()-rBounds.width()) && x<= this.getWidth()) && ((y >= (this.getHeight()-rBounds.height()) && y<= this.getHeight()))))
      {
        //System.out.println("touch");
        this.setText("");

//        event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
      }
    }
    return super.onTouchEvent(event);
  }

  public void setCleanTextOnClick(boolean flag) {
	  cleanTextOnClick = flag;
  }

  @Override
  protected void finalize() throws Throwable
  {
    dRight = null;
    rBounds = null;
    super.finalize();
  }
}