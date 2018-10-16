package d.plugin;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import d.plugin.utils.DLog;
import d.plugin.utils.InjectManager;
import d.plugin.utils.RouterUtils;

public class RouterTransform extends Transform {

    private HashSet<String> mRouterUriHandlerList;

    // contains DRouterPluginTransform.class jar
    private File mRouterTransformJar;
    private File mRouterTransformDestJar;

    @Override
    public String getName() {
        return "DRouter";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) {
        mRouterUriHandlerList = new HashSet<>();
        handle(transformInvocation.getInputs(), transformInvocation.getOutputProvider(), transformInvocation.getContext());
    }

    private void handle(Collection<TransformInput> inputs, TransformOutputProvider outputProvider, Context context) {
        try {
            long lastTime = System.currentTimeMillis();
            outputProvider.deleteAll();
            handleJar(inputs, outputProvider);
            handleDirectory(inputs, outputProvider);
            inject(context, mRouterTransformJar);
            long duration = System.currentTimeMillis() - lastTime;
            DLog.e("DRouter消耗的编译时间 :    " + duration / 1000 / 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inject(Context context, File jarFile) throws Exception {
        DLog.e("开始注入");
        String hexName = DigestUtils.md5Hex(jarFile.getAbsolutePath()).substring(0, 8);
        File optJar = new File(context.getTemporaryDir(), hexName + jarFile.getName());
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar));

        JarFile file = new JarFile(jarFile);
        Enumeration enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement();
            InputStream inputStream = file.getInputStream(jarEntry);

            String entryName = jarEntry.getName();
            String className = RouterUtils.getClassName(entryName);

            ZipEntry zipEntry = new ZipEntry(entryName);

            jarOutputStream.putNextEntry(zipEntry);

            byte[] modifiedClassBytes = null;
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);

            if (RouterUtils.isCoreTransformFile(className)) {
                modifiedClassBytes = InjectManager.injectClasses(sourceClassBytes, mRouterUriHandlerList);
            }
            if (modifiedClassBytes == null) {
                jarOutputStream.write(sourceClassBytes);
            } else {
                jarOutputStream.write(modifiedClassBytes);
            }
            jarOutputStream.closeEntry();
        }

        jarOutputStream.close();
        file.close();

        FileUtils.copyFile(optJar, mRouterTransformDestJar);
    }


    private void handleJar(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) throws IOException {
        for (TransformInput input : inputs) {
            Collection<JarInput> jarInputs = input.getJarInputs();
            for (JarInput jarInput : jarInputs) {
                String destName = jarInput.getFile().getName();
                String hexName = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath()).substring(0, 8);
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4);
                }
                File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);

                String destJarPath = jarInput.getFile().getPath();

                if (destJarPath.contains("drouter")) {
                    mRouterTransformJar = jarInput.getFile();
                    mRouterTransformDestJar = dest;
                }

                if (!destJarPath.contains(".gradle/caches")) {
                    String classPath = RouterUtils.getRouterUriHandlerTargetFileFromJar(jarInput.getFile());
                    if (classPath != null) {
                        mRouterUriHandlerList.add(classPath);
                    }
                }

                FileUtils.copyFile(jarInput.getFile(), dest);
            }
        }
    }

    private void handleDirectory(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) throws IOException {
        for (TransformInput input : inputs) {
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();
            for (DirectoryInput dInput : directoryInputs) {
                File dest = outputProvider.getContentLocation(dInput.getName(),
                        dInput.getContentTypes(), dInput.getScopes(), Format.DIRECTORY);
                File dir = dInput.getFile();
                if (dir != null) {
                    traverseFolder(dir, dir.getAbsolutePath(), dest);
                    FileUtils.copyDirectory(dir, dest);
                }
            }
        }
    }


    // /Users/wudi/Desktop/Demo11/app/build/intermediates/transforms/DRouter/release/0
    private void traverseFolder(File file, String rootPath, File destFile) {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File classFile : files) {
                    if (classFile.isDirectory()) {
                        traverseFolder(classFile, rootPath, destFile);
                    } else {
                        String p = classFile.getAbsolutePath();
                        String className = RouterUtils.getClassName(p);
                        if (RouterUtils.isTargetFile(className) && className.endsWith(".class")) {
                            String target = p.replace(rootPath + "/", "");
                            mRouterUriHandlerList.add(target);
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
}
