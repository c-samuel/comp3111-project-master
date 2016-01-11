package hk.ust.cse.hunkim.questionroom.renderer.android;

import android.content.res.AssetManager;
import hk.ust.cse.hunkim.questionroom.renderer.android.font.FontFactoryAndroid;
import hk.ust.cse.hunkim.questionroom.renderer.android.geom.GeomFactoryAndroid;
import hk.ust.cse.hunkim.questionroom.renderer.android.graphics.GraphicsFactoryAndroid;
import hk.ust.cse.hunkim.questionroom.renderer.android.parser.ParserFactoryAndroid;
import hk.ust.cse.hunkim.questionroom.renderer.android.resources.ResourceLoaderFactoryAndroid;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.FactoryProvider;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.font.FontFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.geom.GeomFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.GraphicsFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.parser.ParserFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.resources.ResourceLoaderFactory;

public class FactoryProviderAndroid extends FactoryProvider {
	
	private AssetManager mAssetManager;
	
	public FactoryProviderAndroid(AssetManager assetManager) {
		mAssetManager = assetManager;
	}

	@Override
	protected GeomFactory createGeomFactory() {
		return new GeomFactoryAndroid();
	}

	@Override
	protected FontFactory createFontFactory() {
		return new FontFactoryAndroid(mAssetManager);
	}

	@Override
	protected GraphicsFactory createGraphicsFactory() {
		return new GraphicsFactoryAndroid();
	}

	@Override
	protected ParserFactory createParserFactory() {
		return new ParserFactoryAndroid();
	}

	@Override
	protected ResourceLoaderFactory createResourceLoaderFactory() {
		return new ResourceLoaderFactoryAndroid(mAssetManager);
	}
}
