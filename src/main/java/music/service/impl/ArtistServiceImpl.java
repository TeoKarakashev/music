package music.service.impl;

import com.google.gson.Gson;
import music.model.entities.ArtistEntity;
import music.repository.ArtistRepository;
import music.service.ArtistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class ArtistServiceImpl implements ArtistService {

    private Resource artistsFile;
    private final Gson gson;
    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(@Value("classpath:init/artists.json") Resource artistsFile, Gson gson, ArtistRepository artistRepository) {
        this.artistsFile = artistsFile;
        this.gson = gson;
        this.artistRepository = artistRepository;
        this.artistsFile =artistsFile;
    }

    @Override
    public void seedArtists() {
        if(artistRepository.count() == 0){
            try {
                ArtistEntity[] artistEntities = this.gson.fromJson(Files.readString(Path.of(artistsFile.getURI())), ArtistEntity[].class);

                Arrays.stream(artistEntities).forEach(artistRepository::save);
            } catch (IOException e) {
                throw new IllegalStateException();
            }
        }
    }
}
