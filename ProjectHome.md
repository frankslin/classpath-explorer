Library which allows discovering classes at runtime. In Java one can use

```
ClassLoader.getResourceAsInputStream(resource);
```

to load the data from the CLASS\_PATH provided that you know the name of the resource. This library allows you to discover what Java packages and classes are available at runtime.

```
ClassPathFactory factory = new ClassPathFactory();
ClassPath classPath = factory.createFromJVM();
for (String packageName : classPath.listPackages("")) {
  System.out.println(packageName);
}
```

http://misko.hevery.com/about