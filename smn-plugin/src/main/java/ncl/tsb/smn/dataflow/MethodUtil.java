/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MethodUtil {
	private static final Logger logger = Logger.getLogger(MethodUtil.class.getName());

	public static Method getMethod(final Class<?> nodeClass, final String nodeMethodName, final Class<?> dataClass) {
		try {
			return nodeClass.getMethod(nodeMethodName, new Class[] { dataClass });
		} catch (final Throwable throwable) {
			logger.log(Level.WARNING, "Unable to find method \"" + nodeMethodName + "\"", throwable);

			return null;
		}
	}
}
