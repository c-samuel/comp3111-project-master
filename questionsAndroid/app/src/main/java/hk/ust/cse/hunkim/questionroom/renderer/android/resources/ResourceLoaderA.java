package hk.ust.cse.hunkim.questionroom.renderer.android.resources;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import hk.ust.cse.hunkim.questionroom.renderer.android.BaseObjectHelper;
import hk.ust.cse.hunkim.questionroom.renderer.share.exception.ResourceParseException;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.resources.ResourceLoader;

public class ResourceLoaderA implements ResourceLoader {
	
	private AssetManager mAssetManager;
	
	public ResourceLoaderA(AssetManager assetManager) {
		mAssetManager = assetManager;
	}

	public InputStream loadResource(Object base, String path) throws ResourceParseException {
		try {
			return mAssetManager.open(BaseObjectHelper.getPath(base, path));
		} catch (IOException e) {
			throw new ResourceParseException("Could not load resource.", e);
		}
	}

}
