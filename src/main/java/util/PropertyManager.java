package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author sunil
 * 
 *         Stores the configuration properties for the application
 * 
 */
public class PropertyManager {

	private static PropertyManager propertyManager;

	private Properties props;

	/**
	 * Returns the value for given key
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return props.getProperty(key);
	}

	private PropertyManager() {
		props = new Properties();

		InputStream input = null;

		try {
			input = getClass().getClassLoader().getResourceAsStream(
					"openid-urls.properties");

			props.load(input);

			props.load(input);

		} catch (IOException e) {
			// load failed:
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static PropertyManager newInstance() {
		if (propertyManager == null) {
			propertyManager = new PropertyManager();
		}

		return propertyManager;
	}

}
