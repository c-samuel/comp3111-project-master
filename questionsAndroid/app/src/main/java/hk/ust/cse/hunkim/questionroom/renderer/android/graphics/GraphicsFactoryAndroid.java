package hk.ust.cse.hunkim.questionroom.renderer.android.graphics;

import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.BasicStroke;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Color;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.GraphicsFactory;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Image;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Transform;

public class GraphicsFactoryAndroid extends GraphicsFactory {

	@Override
	public BasicStroke createBasicStroke(float width, int cap, int join,
			float miterLimit) {
		return new BasicStrokeA(width, miterLimit, cap, join);
	}

	@Override
	public Color createColor(int red, int green, int blue) {
		return new ColorA(red, green, blue);
	}

	@Override
	public Image createImage(int width, int height, int type) {
		return new ImageA(width, height, type);
	}

	@Override
	public Image getImage(String path) {
		// TODO Get image in graphics factory
		return null;
	}

	@Override
	public Transform createTransform() {
		return new TransformA();
	}

}
