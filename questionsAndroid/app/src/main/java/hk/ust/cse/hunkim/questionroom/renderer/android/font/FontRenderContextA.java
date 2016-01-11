package hk.ust.cse.hunkim.questionroom.renderer.android.font;

import android.graphics.Paint;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontRenderContext;

public class FontRenderContextA implements FontRenderContext {
	
	private Paint mPaint;
	
	public FontRenderContextA(Paint paint) {
		mPaint = paint;
	}

	public Paint getPaint() {
		return mPaint;
	}

}
