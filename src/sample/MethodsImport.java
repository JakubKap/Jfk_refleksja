package sample;

import sample.callable.IBooleanOps;
import sample.callable.ICompareOps;
import sample.callable.IMathOps;
import sample.callable.IStringOps;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MethodsImport {

    private IMathOps myMathOps;

    private List<Class<?>> classes = new ArrayList<>();

    private List<Method> methodList = new ArrayList<>();

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

                    if (isAssignable(c)) {
                        classes.add(c);
                        methodList.addAll(Arrays.asList(c.getDeclaredMethods()));
                    }
                }
                catch (ClassNotFoundException exp)
                {
                    continue;
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

            if(isAssignable(c)) {
                classes.add(c);
                methodList.addAll(Arrays.asList(c.getDeclaredMethods()));
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
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


    public void importClasses(File dir){

        classes.clear();
        methodList.clear();

        for(File f : dir.listFiles()){
            if(f.getName().endsWith(".jar"))
                importJar(f);
            else if(f.getName().endsWith(".class"))
                importClassFile(f);
        }
    }

    public String invokeMethod(Method method, Object args[]){

        int numOfParams = method.getParameterCount();

        Class[] parameterTypes = method.getParameterTypes();

        Object[] params;

        if(numOfParams == 1){
            params = new Object[1];

           if(parameterTypes[0].equals(String.class))
               params[0] = args[0];
           else if (parameterTypes[0].equals(boolean.class))
               params[0] = Boolean.parseBoolean(args[0].toString());

           else if(parameterTypes[0].equals(double.class)) {
               params[0] = Double.parseDouble(args[0].toString());
               System.out.println("1params[0] + " + args[0]);
           }

        }
        else {
            params = new Object[2];

            if (parameterTypes[0].equals(String.class))
                params[0] = args[0];
            else if (parameterTypes[0].equals(int.class))
                params[0] = Integer.parseInt(args[0].toString());
            else if (parameterTypes[0].equals(boolean.class))
                params[0] = Boolean.parseBoolean(args[0].toString());


            if (parameterTypes[1].equals(int.class))
                params[1] = Integer.parseInt(args[1].toString());
            else if (parameterTypes[1].equals(boolean.class))
                params[1] = Boolean.parseBoolean(args[1].toString());

        }

            try {
                return method.invoke(method.getDeclaringClass().newInstance(), params).toString();
            } catch(IllegalAccessException e){
                e.printStackTrace();
            }  catch(InvocationTargetException e){
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }




    public List<Method> getMethodList() {
        return methodList;
    }

}
