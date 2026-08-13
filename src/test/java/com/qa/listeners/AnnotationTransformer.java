package com.qa.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/**
 * Automatically attaches RetryAnalyzer to every @Test method at runtime,
 * including TestRunner.scenarios() (the Cucumber TestNG data-provider test),
 * so TestRunner itself doesn't need to be touched.
 *
 * Registered as a <listener> in testng.xml.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                           Constructor testConstructor, Method testMethod) {
        Class<? extends IRetryAnalyzer> currentAnalyzer = annotation.getRetryAnalyzerClass();
        if (currentAnalyzer == null || currentAnalyzer.equals(IRetryAnalyzer.class)) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
