package lu.cnw.ranking.init;

import lu.cnw.ranking.service.ImportService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LoadAllAthletes implements InitializingBean {
    final ImportService importService;
    final String cacheFolder;

    public LoadAllAthletes(ImportService importService, @Value("${ranking.cache}") String cacheFolder) {
        this.importService = importService;
        this.cacheFolder = cacheFolder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (cacheFolder == null) return;
        Path cachePath = Paths.get(this.cacheFolder + "/athlete");
        try (var stream = Files.find(cachePath, 2, (path, basicFileAttributes) ->
                path.toFile().getName().matches("\\d+")
        )) {
            stream.map(Path::toFile)
                    .map(File::getName).forEach(
                            id -> {
                                try {
                                    importService.importAthlete(id);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }
    }
}
