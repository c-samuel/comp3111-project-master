package hk.ust.cse.hunkim.questionroom.renderer.android.resources;

import android.content.res.AssetManager;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.resources.ResourceLoader;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.resources.ResourceLoaderFactory;

public class ResourceLoaderFactoryAndroid implements ResourceLoaderFactory {
	
	private AssetManager mAssetManager;
	
	public ResourceLoaderFactoryAndroid(AssetManager assetManager) {
		mAssetManager = assetManager;
	}

	public ResourceLoader createResourceLoader() {
		return new ResourceLoaderA(mAssetManager);
	}

}
