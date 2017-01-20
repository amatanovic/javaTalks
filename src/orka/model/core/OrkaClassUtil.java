package orka.model.core;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class OrkaClassUtil {
	private static final Logger log = Logger.getLogger(OrkaClassUtil.class);

	public static Object getSessionBean(String name, boolean local) {

		Object o = null;
		try {
			InitialContext ctx = new InitialContext();
		 
			o = ctx.lookup("java:global/javaTalks/" + name);
		} catch (Exception e) {
			log.error("GRESKA u instanciranju bean");
			throw new RuntimeException(e);
		}
		return o;
	}

	public static String getPorukaFromBundle(String key, Object[] parametri,
			Locale locale) {

		ResourceBundle bundle = ResourceBundle.getBundle(
				"orka.view.core.localization.messages", locale);
		String poruka = "";
		try {
			poruka = bundle.getString(key);
		} catch (MissingResourceException mrbe) {
			poruka = "?? " + key + " ??";
		} catch (NullPointerException mrbe) {
			poruka = "null - " + key + " - null";
		}

		MessageFormat mf = new MessageFormat(poruka, locale);

		poruka = mf.format(parametri, new StringBuffer(), null).toString();

		return poruka;
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException {

		Iterator<Class<?>> it2 = getSuperClasses(
				Class.forName("java.lang.Object")).iterator();
		int i = 1;
		while (it2.hasNext()) {
			System.out.println(i + "  " + it2.next());
			i++;
		}
	}

	public static List<Class<?>> getSveKlase(String baza) {
		List<Class<?>> sveKlase = new Vector<Class<?>>();
		List<String> svi = new Vector<String>();
		getPaketi("orka", svi);
		Iterator<String> it = svi.iterator();

		while (it.hasNext()) {
			String paket = it.next();
			try {
				sveKlase.addAll(getClassesForPackage(paket));
			} catch (ClassNotFoundException e) {

			}
		}
		return sveKlase;
	}

	public static void getPaketi(String baza, List<String> sviPaketi) {

		List<String> podPak = getPackageNames(baza);
		for (int i = 0; i < podPak.size(); i++) {
			String paket = podPak.get(i);
			getPaketi(paket, sviPaketi);
			sviPaketi.add(paket);
		}

	}

	public static List<String> getPackageNames(String basepkgname) {
		ArrayList<String> packages = new ArrayList<String>();
		try {
			File directory = getClassesHelper(basepkgname);

			if (directory.isDirectory() && directory.exists()) {
				for (File f : directory.listFiles()) {
					if (f.isDirectory()) {
						packages.add(basepkgname + "." + f.getName());
					}
				}
			}
			return packages;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static File getClassesHelper(String pckgname)
			throws ClassNotFoundException {
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			return directory;
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package");
		}
	}

	public static List<Class<?>> getClassessOfInterface(String thePackage,
			Class<?> theInterface) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			for (Class<?> discovered : getClassesForPackage(thePackage)) {
				if (Arrays.asList(discovered.getInterfaces()).contains(
						theInterface)) {
					classList.add(discovered);
				}
			}
		} catch (ClassNotFoundException ex) {

		}

		return classList;
	}

	public static List<Class<?>> getSuperClasses(Class<?> class1) {
		Vector<Class<?>> klase = new Vector<Class<?>>();
		Class<?> nad = class1.getSuperclass();
		if (nad != null) {
			List<Class<?>> kk = getSuperClasses(nad);
			if (kk != null) {
				klase.addAll(kk);
			}
			klase.add(nad);
			return klase;
		}

		return klase;

	}

	public static List<Class<?>> getSubClasses(List<Class<?>> allClasses,
			Class<?> superClass) {
		List<Class<?>> subClasses = new Vector<Class<?>>();
		Iterator<Class<?>> it = allClasses.iterator();
		while (it.hasNext()) {
			Class<?> c = it.next();
			// log.debug("Ime klase ---> " + c.getName() + ", abstraktna: " +
			// Modifier.isAbstract(c.getModifiers()));
			if (!Modifier.isAbstract(c.getModifiers())
					&& getSuperClasses(c).contains(superClass)) {
				subClasses.add(c);
			}
		}

		return subClasses;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<?>> getNonAbstractClassesWithInterface(
			List<Class<?>> allClasses, Class<?> intf) {
		List<Class<?>> subClasses = new Vector<Class<?>>();
		Iterator<Class<?>> it = allClasses.iterator();
		while (it.hasNext()) {
			Class<?> c = it.next();
			if (!Modifier.isAbstract(c.getModifiers()) && !c.isInterface()) {

				Vector<Class> interfaces = new Vector<Class>();
				Class<?> superKlasa = c;
				interfaces.addAll(Arrays.asList(superKlasa.getInterfaces()));
				while (true) {
					superKlasa = superKlasa.getSuperclass();
					if (superKlasa == null)
						break;
					interfaces
							.addAll(Arrays.asList(superKlasa.getInterfaces()));
				}
				if (interfaces.contains(intf)) {
					subClasses.add(c);
				}

			}
		}

		return subClasses;
	}

	public static List<Class<?>> getSubClassesNonAbstract(
			List<Class<?>> allClasses, Class<?> superClass) {
		List<Class<?>> subClasses = new Vector<Class<?>>();
		Iterator<Class<?>> it = allClasses.iterator();
		while (it.hasNext()) {
			Class<?> c = it.next();
			if (!Modifier.isAbstract(c.getModifiers())
					&& getSuperClasses(c).contains(superClass)) {
				subClasses.add(c);
			}
		}

		return subClasses;
	}

	/**
	 * vraca geter seter metode za neki field po java bean standardu
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Method[] getGetersSeters(Class klasa, Field field) {
		log.trace("KLASA " + klasa + "  FIELD " + field.getName());
		String gg = "get";
		if (field.getType().equals(boolean.class)) {
			gg = "is";
		}

		StringBuffer sb = new StringBuffer(field.getName());
		String pc = String.valueOf(sb.charAt(0));
		pc = pc.toUpperCase();
		sb = sb.replace(0, 1, pc);
		String getter = gg.concat(sb.toString());
		String setter = "set".concat(sb.toString());
		Method get;
		Method set;
		try {
			Class[] c = null;
			get = klasa.getMethod(getter, c);

			set = klasa.getMethod(setter, field.getType());

		} catch (SecurityException e) {
			throw new RuntimeException("greska", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("greska", e);
		}
		Method[] m = { get, set };
		return m;
	}

	/**
	 * Vraca sve field-ove neke klase c i njezinih nad klasa
	 * 
	 * @param polja
	 * @param c
	 */
	public static void getFields(List<Field> polja, Class<?> c) {
		Class<?> nad = c.getSuperclass();
		if (nad != null)
			getFields(polja, nad);
		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			polja.add(f);

		}

	}

	/**
	 * Vraca sve field-ove neke klase c i njezinih nad klasa uz primjenu filtera
	 * 
	 * @param polja
	 *            Mora primiti praznu listu polja jer ce u njih staviti fildove
	 * @param c
	 * @param filter
	 */
	public static void getFields(List<Field> polja, Class<?> c, int filter) {
		Class<?> nad = c.getSuperclass();
		if (nad != null)
			getFields(polja, nad, filter);
		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			int mod = f.getModifiers();
			if (mod == filter) {
				polja.add(f);
			}

		}

	}

	public static List<Field> getAnnotatedFields(List<Field> polja,
			Class<? extends Annotation> annotation) {
		List<Field> vrati = new Vector<Field>();
		for (int i = 0; i < polja.size(); i++) {
			Field f = polja.get(i);
			if (!f.isAnnotationPresent(annotation)) {
				continue;
			}
			vrati.add(f);
		}
		return vrati;

	}

	/**
	 * Trazi field po imenu u klasi c i njegovim nad klasama
	 * 
	 * @param c
	 * @param name
	 * @return
	 */
	public static Field getField(Class<?> c, String name) {
		Vector<Field> polja = new Vector<Field>();
		getFields(polja, c);
		Iterator<Field> it = polja.iterator();
		while (it.hasNext()) {
			Field f = it.next();
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	public static List<Class<?>> getClassesForPackage(String pckgname)
			throws ClassNotFoundException {
		// This will hold a list of directories matching the pckgname.
		// There may be more than one if a package is split over multiple
		// jars/paths
		List<Class<?>> classes = new ArrayList<Class<?>>();
		ArrayList<File> directories = new ArrayList<File>();
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			// Ask for all resources for the path
			Enumeration<URL> resources = cld.getResources(pckgname.replace('.',
					'/'));
			while (resources.hasMoreElements()) {
				URL res = resources.nextElement();
				if (res.getProtocol().equalsIgnoreCase("jar")) {
					JarURLConnection conn = (JarURLConnection) res
							.openConnection();
					JarFile jar = conn.getJarFile();
					for (JarEntry e : Collections.list(jar.entries())) {
						log.trace("RAW NAME FROM JAR >" + e.getName() + "<");
						if (e.getName().startsWith(pckgname.replace('.', '/'))
								&& e.getName().endsWith(".class")
								&& !e.getName().contains("$")) {
							String className = e.getName().replace("/", ".")
									.substring(0, e.getName().length() - 6);
							log.trace("POKUSAVA UCITAT KLASU >" + className
									+ "<");
							try {
								try {
									Class<?> c = Class.forName(className);
									classes.add(c);
								} catch (Exception e3) {
									log.error("KLASA SE NEMOZE UCITAT "
											+ className + " " + e3.getMessage());
								}
							} catch (Error e5) {
								log.error("KLASA SE NEMOZE UCITAT " + className
										+ " " + e5.getMessage());
							}
						}
					}
				} else
					directories.add(new File(URLDecoder.decode(res.getPath(),
							"UTF-8")));
			}
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be "
					+ "a valid package (Null pointer exception)");
		} catch (UnsupportedEncodingException encex) {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be "
					+ "a valid package (Unsupported encoding)");
		} catch (IOException ioex) {
			throw new ClassNotFoundException(
					"IOException was thrown when trying "
							+ "to get all resources for " + pckgname);
		}
		List<String> srcClasspaths = new Vector<String>();
		List<File> dirs = new ArrayList<File>();
		for (File directory : directories) {
			if (directory.exists() && directory.isDirectory()) {
				String dir = directory.getAbsolutePath();
				if (dir.lastIndexOf(pckgname) != -1) {
					srcClasspaths.add(dir.substring(0,
							dir.lastIndexOf(pckgname)));
				}

				File[] subs = directory.listFiles();

				pretraziSveDir(dirs, Arrays.asList(subs));

			}
		}

		directories.addAll(dirs);

		searchDirs(pckgname, classes, directories, srcClasspaths);
		return classes;
	}

	private static void pretraziSveDir(List<File> dirs, List<File> subs) {
		for (File s : subs) {
			if (s.exists() && s.isDirectory()) {
				dirs.add(s);
				pretraziSveDir(dirs, Arrays.asList(s.listFiles()));

			}
		}

	}

	private static void searchDirs(String pckgname, List<Class<?>> classes,
			ArrayList<File> directories, List<String> srcClasspaths)
			throws ClassNotFoundException {
		// For every directory identified capture all the .class files
		for (File directory : directories) {
			if (directory.exists()) {
				// Get the list of the files contained in the package
				File[] files = directory.listFiles();
				for (File file : files) {
					// we are only interested in .class files
					if (file.getName().endsWith(".class")) {
						// removes the .class extension

						try {
							String absPath = file.getAbsolutePath();
							for (String srcPath : srcClasspaths) {
								if (absPath.indexOf(srcPath) != -1) {
									absPath = absPath.replace(srcPath, "");
									break;
								}
							}
							log.trace("RAW NAME >" + absPath + "<");

							String className = absPath.replace(File.separator,
									".").substring(0, absPath.length() - 6);
							if (className.startsWith("WEB-INF.classes.")) {
								className = className.replaceFirst(
										"WEB-INF.classes.", "");
							}
							log.trace("POKUSAVA UCITAT KLASU >" + className
									+ "<");
							try {
								Class<?> c = Class.forName(className);
								classes.add(c);
							} catch (Exception e) {
								log.error("KLASA SE NEMOZE UCITAT " + className);
							}
						} catch (Error e) {
							log.error("KLASA SE NEMOZE UCITAT " + file);
						}
					}
				}
			} else {
				throw new ClassNotFoundException(pckgname + " ("
						+ directory.getPath()
						+ ") does not appear to be a valid package");
			}
		}
	}

	public static List<Object> getObjectsFromClasses(List<Class<?>> klase) {
		List<Object> lista = new Vector<Object>();
		for (Class<?> k : klase) {
			try {
				Object o = k.newInstance();
				lista.add(o);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return lista;
	}

	public static Object newInstance(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			return null;
		}
	}
}
