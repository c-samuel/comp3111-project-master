package hk.ust.cse.hunkim.questionroom.renderer.android.font;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import hk.ust.cse.hunkim.questionroom.renderer.android.geom.Rectangle2DA;
import hk.ust.cse.hunkim.questionroom.renderer.android.graphics.Graphics2DA;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.TextLayout;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.geom.Rectangle2D;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Graphics2DInterface;
import android.graphics.Rect;

public class TextLayoutA implements TextLayout {

	private Paint mPaint;
	private String mString;

	private FontA mFont;

	public TextLayoutA(String string, FontA font, FontRenderContextA fontRenderContext) {
		mString = string;
		mFont = font;
		mPaint = fontRenderContext.getPaint();
	}

	public Rectangle2D getBounds() {
		updatePaint();
		Rect bounds = new Rect();

		mPaint.getTextBounds(mString, 0, mString.length(), bounds);

		return new Rectangle2DA(bounds);
	}

	public void draw(Graphics2DInterface graphics, int x, int y) {
		updatePaint();

		Graphics2DA g2d = (Graphics2DA) graphics;
		g2d.drawString(mString, x, y, mPaint);
	}

	private void updatePaint() {
		mPaint.setTypeface(mFont.getTypeface());
		mPaint.setTextSize(mFont.getSize());
		mPaint.setStyle(Style.FILL);
	}
}
