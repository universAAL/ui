/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.handler.web.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * Find the resources referenced by urls.
 *
 * @author amedrano
 *
 */
public final class ResourceMapper {

	/**
	 * The folders where the resources should be allocated, whether it is in the
	 * confDir or in the resources (inside the JAR).
	 */
	private static String[] resourceFolders = { "icons/", "images/", "resources/" };

	/**
	 * {@link ModuleContext} to enable logging.
	 */
	public static ModuleContext mc;

	/**
	 * Utility class. no instance allowed.
	 */
	private ResourceMapper() {
	}

	/**
	 * Utility method: it will first {@link ResourceMapper#search(String)} for
	 * the url. Then it will look for it in the cache, if it not available it
	 * will copy it there. Returns the cache location.
	 *
	 * @param cacheFolder
	 *            the location of the cache folder.
	 * @param url
	 *            the file to cache
	 * @return the cached location.
	 */
	public static String cached(String cacheFolder, String url) {
		URL search = search(url);
		if (search != null)
			return cached(cacheFolder, search);
		else
			return url;
	}

	/**
	 * Utility method: it will look for the resource in the cache, if it not
	 * available it will copy it there. Returns the cache location.
	 *
	 * @param cacheFolder
	 *            the location of the cache folder.
	 * @param resource
	 *            the file to cache
	 * @return the cached location.
	 */
	public static String cached(String cacheFolder, URL resource) {
		if (resource == null)
			return null;
		String extension = resource.getFile();
		if (extension != null) {
			extension = extension.substring(extension.lastIndexOf('.'));
		} else {
			extension = "";
		}
		String coded = Integer.toString(resource.toString().hashCode()) + extension;
		File cached = new File(cacheFolder, coded);
		if (!cached.exists() || cached.getParentFile().mkdirs()) {
			// copy
			try {
				new Retreiver(resource.openStream(), cached);
				// store reference for when the dialog is finished the Retriever
				// is stoped.
			} catch (IOException e) {
				if (mc != null)
					LogUtils.logError(mc, ResourceMapper.class, "cached",
							new String[] { "It seems it is not possible to cache file " }, e);
			}
		}
		return coded;
	}

	/**
	 * Searches for the specified url in the config directory and JAR resources.
	 *
	 * @param url
	 *            relative url of the resource to find
	 * @return the {@link URL} for the resource, null if not found
	 */
	static public URL search(String url) {

		URL resource;
		try {
			if (mc != null)
				LogUtils.logDebug(mc, ResourceMapper.class, "search", new String[] { "Looking for " + url }, null);
			resource = new URL(url);
			if (existsURL(resource)) {
				return resource;
			} else {
				if (mc != null)
					LogUtils.logWarn(mc, ResourceMapper.class, "search",
							new String[] { "url: " + url + " seems not to exists, or it is not accessible" }, null);
				return null;
			}
		} catch (MalformedURLException e) {
			if (mc != null)
				LogUtils.logDebug(mc, ResourceMapper.class, "search",
						new String[] { "Looking for " + url + " in folders" }, null);
			resource = searchFolder(url);
			if (resource != null) {
				return resource;
			} else {
				if (mc != null)
					LogUtils.logDebug(mc, ResourceMapper.class, "search",
							new String[] { "Looking for " + url + " in resources" }, null);
				URL retVal = searchResources(url);
				if (retVal == null && mc != null)
					LogUtils.logWarn(mc, ResourceMapper.class, "search",
							new String[] { "Resource " + url + " not found" }, null);
				return retVal;
			}
		}
	}

	/**
	 * Check that the resource pointed by the URL really exists.
	 *
	 * @param url
	 *            the URL to be checked
	 * @return true is the URL can be accessed
	 */
	static private boolean existsURL(URL url) {
		URLConnection con;
		try {
			con = url.openConnection();
			con.connect();
			con.getInputStream();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Searched for the specified url in the config directory.
	 *
	 * @param url
	 *            the relative url of the file to look for.
	 * @return the {@link URL} of the file if found, null otherwise.
	 */
	static private URL searchFolder(String url) {
		int i = 0;
		URL file = null;
		while (i < resourceFolders.length && file == null) {
			file = checkFolder(resourceFolders[i] + url);
			i++;
		}
		return file;
	}

	/**
	 * check whether the specified url exists or not.
	 *
	 * @param url
	 *            the url to test.
	 * @return the {@link URL} of existent file, null otherwise
	 */
	static private URL checkFolder(String url) {
		URL urlFile;
		File confDirFile = getConfigHome();
		if (confDirFile == null)
			return null;
		try {
			urlFile = new URL("file://" + confDirFile.getAbsolutePath().replace('\\', '/') + url.replace('\\', '/'));

			File resourceFile = new File(urlFile.getFile());
			// Activator.logDebug("Looking for: " + urlFile.toString() + ".",
			// null);
			if (resourceFile.exists()) {
				// Activator.logDebug("Found.", null);
				return urlFile;
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static File getConfigHome() {
		if (mc == null)
			return null;
		return mc.getConfigHome();
	}

	/**
	 * Searched for the specified url in the JAR.
	 *
	 * @param url
	 *            the relative url of the resource to look for.
	 * @return the {@link URL} of the resource if found, null otherwise.
	 */
	static private URL searchResources(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		int i = 0;
		URL resource = null;
		while (i < resourceFolders.length && resource == null) {
			// Activator.logDebug("looking for resources: " + resourceFolders[i]
			// + url, null);
			resource = ResourceMapper.class.getClassLoader().getResource(resourceFolders[i] + url);
			i++;
		}
		return resource;
	}

	/**
	 * A class that will perform copy operation in a thread.
	 *
	 * @author amedrano
	 *
	 */
	static public class Retreiver implements Runnable {

		private boolean work = true;
		private InputStream is;
		private File file;

		/**
		 * @param is
		 * @param file
		 */
		public Retreiver(InputStream is, File file) {
			super();
			this.is = is;
			this.file = file;
			new Thread(this, "Retriever for " + file.getName()).start();
		}

		/** {@ inheritDoc} */
		protected void finalize() throws Throwable {
			finish();
			super.finalize();
		}

		/** {@ inheritDoc} */
		public void run() {
			try {
				if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
					FileOutputStream os = new FileOutputStream(file);
					byte[] buffer = new byte[4096];
					int bytesRead;
					while (((bytesRead = is.read(buffer)) != -1) && work) {
						os.write(buffer, 0, bytesRead);
					}
					is.close();
					os.flush();
					os.close();
					if (!work) {
						file.delete();
					}
				}
			} catch (FileNotFoundException e) {
				if (mc != null)
					LogUtils.logError(mc, Retreiver.class, "run", new String[] {
							"cache seems not to exists, or file: " + file.getAbsolutePath() + " is not accessible" },
							e);
			} catch (IOException e) {
				if (mc != null)
					LogUtils.logError(mc, Retreiver.class, "run",
							new String[] { "It seems it is not possible to cache file " }, e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		public void finish() {
			work = false;
		}
	}
}
