package hk.ust.cse.hunkim.questionroom.renderer.android.font;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import hk.ust.cse.hunkim.questionroom.renderer.android.BaseObjectHelper;
import hk.ust.cse.hunkim.questionroom.renderer.share.exception.ResourceParseException;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.Font;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontLoader;

public class FontLoaderA implements FontLoader {

	private AssetManager mAssetManager;

	public FontLoaderA(AssetManager assetManager) {
		mAssetManager = assetManager;
	}

	public Font loadFont(Object fontType, String name) throws ResourceParseException {
		// TODO fontType should be a class object instead of inputstream
		Typeface typeface = Typeface.createFromAsset(mAssetManager, BaseObjectHelper.getPath(fontType, name));
		return new FontA(name, typeface, Math.round(PIXELS_PER_POINT));
	}

}
