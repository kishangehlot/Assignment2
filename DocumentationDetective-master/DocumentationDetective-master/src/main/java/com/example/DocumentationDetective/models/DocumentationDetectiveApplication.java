package com.example.DocumentationDetective.models;

import annotations.ClassDocumentation;
import annotations.MethodDocumentation;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import utils.CodebaseScanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
public class DocumentationDetectiveApplication {
	//all classes
	//classes with annotation
	//all methods
	//methods with annotation
	//classes with annotation but no documentation
	//classes with no annotation but documentation
	//Methods with annotation but no documentation
	//Methods with no annotation but documentation
	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(DocumentationDetectiveApplication.class, args);

		PrintStream o = new PrintStream(new File("ExtractedDocumentation.txt"));

		System.setOut(o);
		String packageName = "models";
		try {
			List<Class> classes = CodebaseScanner.getClasses(packageName);
			System.out.println("All classes in the package 'models': ");
			for (Class cls: classes) {
				System.out.println(cls.getName());
			}
			System.out.println();

			Reflections reflections = new Reflections(new ConfigurationBuilder()
					.setUrls(ClasspathHelper.forPackage(packageName))
					.setScanners(new TypeAnnotationsScanner(), new org.reflections.scanners.SubTypesScanner(), new MethodAnnotationsScanner())
			);

			Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(ClassDocumentation.class);
			System.out.println("Classes annotated with '@ClassDocumentation': ");
			for (Class<?> cls: annotatedClasses) {
				System.out.println(cls.getName());
			}
			System.out.println();

			Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(annotations.MethodDocumentation.class);
			System.out.println("Methods annotated with '@MethodDocumentation': ");
			for(Method method: annotatedMethods) {
				System.out.println(method);
			}
			System.out.println();

			Map<String, String> methodsWithoutAnnotationButDocumented = new HashMap<>();
			Set<String> methodsWithAnnotationButNoJavaDoc = new HashSet<>();
			Set<Class<?>> annotatedAndUndocumentedClasses = new HashSet<>();
			try {
				System.out.println("Classes not annotated with '@ClassDocumentation' but have documentation: ");
				for (Class<?> cls: classes) {
					String classPath = cls.getName().replace(".", "\\") + ".java";

					String sourceCode = new String(Files.readAllBytes(Paths.get("C:\\Users\\kishan.gehlot\\eclipse-workspace\\DocumentationDetective-master\\DocumentationDetective-master\\src\\main\\java", classPath)));
					System.out.println(sourceCode);
					CompilationUnit cu = StaticJavaParser.parse(sourceCode);

					cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDec -> {
						if (classDec.getComment().isPresent() && classDec.getComment().get() instanceof JavadocComment) {
							if(!classDec.isAnnotationPresent(ClassDocumentation.class)) {
								String classJavadoc = ((JavadocComment) classDec.getComment().get()).getContent();
								System.out.println("Class: " + cls.getSimpleName());
								System.out.println("JavaDoc: " + classJavadoc);
							}
						} else {
							annotatedAndUndocumentedClasses.add(cls);
						}
					});
					System.out.println();

					for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
						method.getJavadoc().ifPresent(javadoc -> {
							if(!method.isAnnotationPresent(MethodDocumentation.class)) {
								String methodJavadoc = javadoc.toText();
								methodsWithoutAnnotationButDocumented.put(String.valueOf(cls.getName() + "." + method.getName()), methodJavadoc);
							}
						});
						if(method.isAnnotationPresent(MethodDocumentation.class) && !method.getJavadoc().isPresent()) {
							methodsWithAnnotationButNoJavaDoc.add(cls.getName() + "." + method.getName());
						}
					}
				}
				System.out.println("Classes annotated with '@ClassDocumentation' but have no javadoc: ");
				for (Class<?> cls: annotatedClasses) {
					System.out.println(cls.getName());
				}
				System.out.println();

				System.out.println("Methods not annotated with '@MethodDocumentation' but have javadoc: ");
				System.out.println(methodsWithoutAnnotationButDocumented);
				System.out.println();

				System.out.println("Methods annotated with '@MethodDocumentation' but have no javadoc: ");
				System.out.println(methodsWithAnnotationButNoJavaDoc);
				System.out.println();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}

//	public static void parseClassJavaDocs(Class cls) throws FileNotFoundException {
//		try {
//
//
////				for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
////					method.getJavadoc().ifPresent(javadoc -> {
////						String methodJavadoc = javadoc.toText();
////						methodsToJavaDoc.put(String.valueOf(method.getName()), methodJavadoc);
////					});
////				}
//
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//	}
//
//}
