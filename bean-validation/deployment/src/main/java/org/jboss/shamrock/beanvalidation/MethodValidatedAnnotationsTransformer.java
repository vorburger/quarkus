package org.jboss.shamrock.beanvalidation;

import java.util.Set;

import org.jboss.jandex.AnnotationTarget.Kind;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.protean.arc.processor.AnnotationsTransformer;
import org.jboss.shamrock.beanvalidation.runtime.interceptor.MethodValidated;
import org.jboss.shamrock.beanvalidation.runtime.jaxrs.JaxrsEndPointValidated;

/**
 * Add {@link MethodValidated} annotations to the methods requiring validation.
 */
public class MethodValidatedAnnotationsTransformer implements AnnotationsTransformer {

    private static final DotName JAXRS_PATH = DotName.createSimple("javax.ws.rs.Path");

    private final Set<DotName> consideredAnnotations;

    MethodValidatedAnnotationsTransformer(Set<DotName> consideredAnnotations) {
        this.consideredAnnotations = consideredAnnotations;
    }

    @Override
    public boolean appliesTo(Kind kind) {
        return Kind.METHOD == kind;
    }

    @Override
    public void transform(TransformationContext transformationContext) {
        MethodInfo method = transformationContext.getTarget().asMethod();

        if (requiresValidation(method)) {
            if (method.hasAnnotation(JAXRS_PATH) || method.declaringClass().annotations().containsKey(JAXRS_PATH)) {
                transformationContext.transform().add(DotName.createSimple(JaxrsEndPointValidated.class.getName())).done();
            } else {
                transformationContext.transform().add(DotName.createSimple(MethodValidated.class.getName())).done();
            }
        }
    }

    private boolean requiresValidation(MethodInfo method) {
        if (method.annotations().isEmpty()) {
            return false;
        }

        for (DotName consideredAnnotation : consideredAnnotations) {
            if (method.hasAnnotation(consideredAnnotation)) {
                return true;
            }
        }

        return false;
    }
}