package com.BeeFramework.view;

import java.lang.reflect.Field;

import android.app.Activity;

public class BeeInjector {
	private static BeeInjector instance;

	private BeeInjector() {

	}

	public static BeeInjector getInstance() {
		if (instance == null) {
			instance = new BeeInjector();
		}
		return instance;
	}

	public void injectView(Activity activity) {
		// TODO Auto-generated method stub
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(BeeInjectId.class)) {
					injectView(activity, field);
				}
			}
		}
	}

	private void injectView(Activity activity, Field field) {
		// TODO Auto-generated method stub
		if (field.isAnnotationPresent(BeeInjectId.class)) {
			BeeInjectId viewInject = field.getAnnotation(BeeInjectId.class);
			int viewId = viewInject.id();
			try {
				field.setAccessible(true);
				field.set(activity, activity.findViewById(viewId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
