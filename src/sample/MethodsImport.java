package sample;

import sample.callable.IBooleanOps;
import sample.callable.ICompareOps;
import sample.callable.IMathOps;
import sample.callable.IStringOps;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MethodsImport {

    public List<Class<?>> classes = new ArrayList<>();

    public void importJar(File dir){
        try
        {
            JarFile jarFile = new JarFile(dir);
            Enumeration<JarEntry> entries = jarFile.entries();

            URL[] urls = { new URL("jar:file:" + dir + "!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class"))
                {
                    continue;
                }

                String className = je.getName().substring(0, je.getName().length()-6);
                className = className.replace('/', '.');
                try {
                    Class<?> c = cl.loadClass(className);

                    if (isAssignable(c))
                        classes.add(c);

                }
                catch (ClassNotFoundException exp)
                {
                    continue;
                }

            }
        }
        catch (IOException exp)
        { }
    }

    public void importClassFile(File file){

        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0,className.indexOf("."));

        file = new File(path);

        try {
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);
            Class<?> c = cl.loadClass(className);

            if(isAssignable(c))
                classes.add(c);

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }


    }

    public void importClasses(File dir){

        for(File f : dir.listFiles()){
            if(f.getName().endsWith(".jar"))
                importJar(f);
            else if(f.getName().endsWith(".class"))
                importClassFile(f);
        }
    }

    public boolean isAssignable(Class<?> c){
        if(IStringOps.class.isAssignableFrom(c) ||
           IMathOps.class.isAssignableFrom(c)||
           ICompareOps.class.isAssignableFrom(c)||
           IBooleanOps.class.isAssignableFrom(c))
            return true;

        else return false;
    }




}
