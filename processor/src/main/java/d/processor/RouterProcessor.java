package d.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.util.LinkedHashSet;
import java.util.List;
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
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

import ime.annotation.UriAnnotation;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private Messager mMessager;
    protected Filer filer;
    protected Types types;
    protected Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(UriAnnotation.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        CodeBlock.Builder builder = CodeBlock.builder();
        String hash = null;
        for (Element element : env.getElementsAnnotatedWith(UriAnnotation.class)) {
            if (!(element instanceof Symbol.ClassSymbol)) {
                continue;
            }

            boolean isActivity = isActivity(element);
            if (!isActivity) {
                continue;
            }

            Symbol.ClassSymbol cls = (Symbol.ClassSymbol) element;
            UriAnnotation uri = cls.getAnnotation(UriAnnotation.class);
            if (uri == null) {
                continue;
            }

            if (hash == null) {
                hash = RouterUtil.hash(cls.className());
            }

            CodeBlock interceptor = getInterceptor(uri);
            CodeBlock handler = CodeBlock.builder().add("$S", cls.className()).build();
            builder.addStatement("regist($S, $S, $S, $L, $L$L)",
                    uri.scheme(),
                    uri.host(),
                    uri.path(),
                    handler,
                    uri.exported(),
                    interceptor);
        }

        RouterUtil.generationClass(builder.build(), filer, elements);


        return true;
    }

    private CodeBlock getInterceptor(UriAnnotation uri) {
        CodeBlock.Builder b = CodeBlock.builder();
        try {
            uri.interceptors();
        } catch (MirroredTypesException mte) {
            List<? extends TypeMirror> typeMirrors = mte.getTypeMirrors();
            if (typeMirrors != null && typeMirrors.size() > 0) {
                for (TypeMirror type : typeMirrors) {
                    if (type instanceof Type.ClassType) {
                        Symbol.TypeSymbol e = ((Type.ClassType) type).asElement();
                        if (e instanceof Symbol.ClassSymbol && isConcreteSubType(e, "d.drouter.Interceptor")) {
                            b.add(", new $T()", e);
                        }
                    }
                }
            }
        }

        return b.build();
    }

    private boolean isActivity(Element element) {
        return isConcreteSubType(element, "android.app.Activity");
    }

    private boolean isConcreteType(Element element) {
        return element instanceof TypeElement && !element.getModifiers().contains(
                Modifier.ABSTRACT);
    }

    private boolean isConcreteSubType(Element element, String className) {
        return isConcreteType(element) && isSubType(element, className);
    }

    private TypeMirror typeMirror(String className) {
        return elements.getTypeElement(className).asType();
    }

    private boolean isSubType(TypeMirror type, String className) {
        return type != null && types.isSubtype(type, typeMirror(className));
    }

    private boolean isSubType(Element element, String className) {
        return element != null && isSubType(element.asType(), className);
    }

    private void info(String msg) {
        mMessager.printMessage(
                Diagnostic.Kind.WARNING,
                msg);
    }
}
