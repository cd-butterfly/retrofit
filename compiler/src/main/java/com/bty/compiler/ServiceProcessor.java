package com.bty.compiler;

import com.bty.annotation.Service;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ServiceProcessor extends AbstractProcessor {

    private static final ClassName RETROFIT = ClassName.get("com.bty.retrofit.net", "RetrofitManager");

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        generateApiFile(processServiceAnnotations(roundEnvironment));
        return true;
    }

    private HashMap<String, ArrayList<MethodSpec>> processServiceAnnotations(RoundEnvironment roundEnvironment){

        HashMap<String,ArrayList<MethodSpec>> hashMap = new HashMap<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(Service.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;

            String packageName = ClassName.bestGuess(element.asType().toString()).packageName();

            Service annotation = element.getAnnotation(Service.class);

            String alias = annotation.alias();

            String baseUrl = annotation.baseUrl();

            String methodName = "get";

            if (!alias.equals("")) {
                methodName += alias;
            } else {
                methodName += element.getSimpleName();
            }

            TypeElement typeElement = (TypeElement) element;

            MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName).
                    addModifiers(Modifier.PUBLIC, Modifier.STATIC).
                    returns(ClassName.bestGuess(element.getSimpleName().toString()));

            MethodSpec methodSpec;

            if (!baseUrl.equals("")) {
                methodSpec = builder.
                        addStatement("return $T.create($T.class,$S)", RETROFIT, ClassName.bestGuess(typeElement.getQualifiedName().toString()), baseUrl).build();
            } else {
                methodSpec = builder.
                        addStatement("return $T.create($T.class)", RETROFIT, ClassName.bestGuess(typeElement.getQualifiedName().toString())).build();
            }

            if (hashMap.containsKey(packageName)){
                ArrayList<MethodSpec> methodSpecs = hashMap.get(packageName);
                methodSpecs.add(methodSpec);
                hashMap.put(packageName,methodSpecs);
            } else {
                ArrayList<MethodSpec> methodSpecs = new ArrayList<>();
                methodSpecs.add(methodSpec);
                hashMap.put(packageName,methodSpecs);
            }
        }

        return hashMap;
    }


    private void generateApiFile(HashMap<String,ArrayList<MethodSpec>> hashMap){
        for (String packageName : hashMap.keySet()){
            TypeSpec.Builder apiBuilder = TypeSpec.classBuilder("Api")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            ArrayList<MethodSpec> methodSpecs = hashMap.get(packageName);

            for (MethodSpec methodSpec : methodSpecs) {
                apiBuilder.addMethod(methodSpec);
            }

            try {
                if (packageName != null && !packageName.equals("")) {
                    messager.printMessage(Diagnostic.Kind.NOTE," Api packageName "+ packageName);
                    JavaFile javaFile = JavaFile.builder(packageName, apiBuilder.build())
                            .addFileComment("This codes are generated automatically. Do not modify!")
                            .build();
                    javaFile.writeTo(filer);
                }
            } catch (IOException e) {
                error(e.toString());
            }
        }
    }

    private void error(String string) {
        messager.printMessage(Diagnostic.Kind.WARNING, string);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Service.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
