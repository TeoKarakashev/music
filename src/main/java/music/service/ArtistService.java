package music.service;

import music.model.view.ArtistViewModel;

import java.util.List;

public interface ArtistService {

    List<ArtistViewModel> findALl();

    void seedArtists();
}
