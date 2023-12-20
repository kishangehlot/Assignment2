package utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.net.URL;

public class CodebaseScanner {
    public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;

        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<File>();
        while(resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add((new File(resource.getFile())));
        }

        List<Class> classes = new ArrayList<Class>();
        for(File dir: dirs) {
            classes.addAll(findClasses(dir, packageName));
        }
        return classes;
    }

    public static List<Class> findClasses(File dir, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if(!dir.exists()) {
            return classes;
        }

        File[] files = dir.listFiles();
        for(File file: files) {
            if(file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if(file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}