package music.service.impl;

import com.google.gson.Gson;
import music.model.entities.ArtistEntity;
import music.model.view.ArtistViewModel;
import music.repository.ArtistRepository;
import music.service.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private Resource artistsFile;
    private final Gson gson;
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper;

    public ArtistServiceImpl(@Value("classpath:init/artists.json") Resource artistsFile, Gson gson, ArtistRepository artistRepository, ModelMapper modelMapper) {
        this.artistsFile = artistsFile;
        this.gson = gson;
        this.artistRepository = artistRepository;
        this.artistsFile =artistsFile;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ArtistViewModel> findALl() {
        return artistRepository.findAll().stream()
                .map(artistEntity -> this.modelMapper.map(artistEntity, ArtistViewModel.class))
                .collect(Collectors.toList());
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
