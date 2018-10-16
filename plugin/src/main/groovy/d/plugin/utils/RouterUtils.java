package d.plugin.utils;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RouterUtils {

    public static String getRouterUriHandlerTargetFileFromJar(File jarFile) {
        try {
            if (jarFile != null) {
                JarFile file = new JarFile(jarFile);
                Enumeration enumeration = file.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.endsWith(".class")) {
                        if (isTargetFile(getClassName(entryName))) {
                            return entryName;
                        }
                    }
                }
                file.close();
            }

            return null;
        } catch (Exception e) {

        }

        return null;
    }

    public static boolean isTargetFile(String className) {
        if ((!className.contains("R\\$") && !className.endsWith("R") && !className.endsWith("BuildConfig"))) {
            return className.startsWith("RouterUriHandlerXXXXXMadeInWUDI");
        }

        return false;
    }

    public static boolean isCoreTransformFile(String className) {
        if ((!className.contains("R\\$") && !className.endsWith("R") && !className.endsWith("BuildConfig"))) {
            return "RouterManager.class".equals(className);
        }

        return false;
    }

    public static String getClassName(String path) {
        String[] split = path.split("/");
        return split[split.length - 1];
    }
}
