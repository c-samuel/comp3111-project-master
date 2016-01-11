package hk.ust.cse.hunkim.questionroom.renderer.android;

import hk.ust.cse.hunkim.questionroom.renderer.share.cyrillic.CyrillicRegistration;
import hk.ust.cse.hunkim.questionroom.renderer.share.greek.GreekRegistration;

public class BaseObjectHelper {
	public static String getPath(Object base, String name) {
		String ret = null;
		if (base == CyrillicRegistration.class) {
			ret = "cyrillic/" + name;
		} else if (base == GreekRegistration.class) {
			ret = "greek/" + name;
		} else {
			ret = name;
		}
		return ret;
	}
}
