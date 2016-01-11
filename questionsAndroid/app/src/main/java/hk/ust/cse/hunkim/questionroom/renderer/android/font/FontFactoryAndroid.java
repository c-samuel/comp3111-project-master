package hk.ust.cse.hunkim.questionroom.renderer.android.font;

import android.content.res.AssetManager;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.Font;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontLoader;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontRenderContext;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.TextAttributeProvider;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.TextLayout;

public class FontFactoryAndroid extends FontFactory {
	
	private AssetManager mAssetManager;
	
	public FontFactoryAndroid(AssetManager assetManager) {
		mAssetManager = assetManager;
	}

	@Override
	public Font createFont(String name, int style, int size) {
		return new FontA(name, style, size);
	}

	@Override
	public TextLayout createTextLayout(String string, Font font,
			FontRenderContext fontRenderContext) {
		return new TextLayoutA(string, (FontA) font, (FontRenderContextA) fontRenderContext);
	}

	@Override
	public TextAttributeProvider createTextAttributeProvider() {
		return new TextAttributeProviderA();
	}

	@Override
	public FontLoader createFontLoader() {
		return new FontLoaderA(mAssetManager);
	}

}
