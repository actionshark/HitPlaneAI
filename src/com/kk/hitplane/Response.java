package com.kk.hitplane;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;

import net.sf.json.JSONObject;

public abstract class Response {
	public static final String PKG = "com.kk.hitplane.response";

	public static boolean dispatch(String str) {
		try {
			JSONObject jo = JSONObject.fromString(str);
			String name = jo.getString(Request.KEY_NAME);

			Class<?> clazz = Class.forName(PKG + "." + name);
			Response instance = (Response) clazz.newInstance();
			instance.mJson = jo;

			for (Field field : clazz.getFields()) {
				int modifiers = field.getModifiers();
				if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & Modifier.STATIC) == 0) {
					String fieldName = field.getName();
					if (jo.has(fieldName)) {
						field.set(instance, jo.get(fieldName));
					}
				}
			}

			return instance.exe();
		} catch (Exception e) {
			Logger.getInstance().print(null, Level.E, e);
		}

		return false;
	}

	//////////////////////////////////////////////////////

	protected JSONObject mJson;

	public abstract boolean exe();
}
