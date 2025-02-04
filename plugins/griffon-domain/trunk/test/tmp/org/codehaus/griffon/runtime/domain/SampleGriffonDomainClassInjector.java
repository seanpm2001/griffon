package org.codehaus.griffon.runtime.domain;

import java.util.Arrays;
import java.util.Collection;

import griffon.domain.GriffonDomainClassProperty;
import griffon.domain.methods.DefaultPersistentMethods;
import griffon.domain.methods.MethodSignature;
import org.codehaus.griffon.compiler.support.DefaultGriffonDomainClassInjector;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class SampleGriffonDomainClassInjector extends DefaultGriffonDomainClassInjector {
    public MethodSignature[] getProvidedMethods() {
        Collection<MethodSignature> signatures = DefaultGroovyMethods.plus(
            Arrays.asList(DefaultPersistentMethods.FETCH.getMethodSignatures()),
            Arrays.asList(DefaultPersistentMethods.FIND_ALL.getMethodSignatures())
        ); 
        return (MethodSignature[]) signatures.toArray(new MethodSignature[signatures.size()]);
    }

    protected String getMappingValue() {
        return "sample";
    }

    protected Class getDomainHandlerClass() {
        return SampleDomainHandler.class;
    }

    protected Class getDomainHandlerHolderClass() {
        return SampleDomainHandlerHolder.class;
    }

    protected void performInjection(ClassNode classNode) {
        injectIdProperty(classNode);
        injectToStringMethod(classNode, GriffonDomainClassProperty.IDENTITY);
        injectAssociations(classNode);
    }
}
