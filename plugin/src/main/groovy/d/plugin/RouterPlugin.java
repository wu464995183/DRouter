package d.plugin;

import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class RouterPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        BaseExtension android = project.getExtensions().getByType(BaseExtension.class);
        RouterTransform transform = new RouterTransform();
        android.registerTransform(transform);
    }
}
