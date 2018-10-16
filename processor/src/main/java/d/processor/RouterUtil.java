package d.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import static com.google.common.base.Charsets.UTF_8;

public class RouterUtil {

    private static final String HandlerClassName = "android.os.Handler";
    private static String GenClassNamePrefix = "RouterUriHandlerXXXXXMadeInWUDI";
    private static final String SuperClassName = "d.drouter.UriHandler";
    private static final String Path = "d.drouter";

    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(str.hashCode());
        }
    }

    public static void generationClass(CodeBlock code, Filer filer, Elements elements) {
        String GenClassName = GenClassNamePrefix + elements.hashCode();
        try {
            MethodSpec methodSpec = MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PUBLIC)
//                    .addModifiers(Modifier.STATIC)
                    .returns(TypeName.VOID)
//                    .addParameter(TypeName.get(typeMirror(HandlerClassName, elements)), "handler")
                    .addCode(code)
                    .build();
            TypeSpec typeSpec = TypeSpec.classBuilder(GenClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec)
                    .superclass(TypeName.get(typeMirror(SuperClassName, elements)))
                    .build();
            JavaFile.builder(Path, typeSpec)
                    .build()
                    .writeTo(filer);

//            writeClassToMETA(GenClassName, filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeClassToMETA(String fileName, Filer filer) {
        if (isEmpty(fileName)) {
            return;
        }
        try {
            FileObject res = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/DRouter/" + fileName);
            OutputStream os = res.openOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
            writer.write(fileName);
            writer.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isEmpty(String path) {
        return path == null || path.length() == 0;
    }

    private static TypeMirror typeMirror(String className, Elements elements) {
        return elements.getTypeElement(className).asType();
    }
}
