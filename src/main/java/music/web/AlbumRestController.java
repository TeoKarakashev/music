package music.web;

import music.model.view.AlbumViewModel;
import music.repository.AlbumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

//@RestController
@RequestMapping("/albums")
public class AlbumRestController {

    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    public AlbumRestController(AlbumRepository albumRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api")
    public List<AlbumViewModel> findAll() {

        this.albumRepository.findAll().stream()
                .map(artistEntity -> modelMapper.map(artistEntity, AlbumViewModel.class)).collect(Collectors.toList());
        throw new UnsupportedOperationException("TODO");
    }
}
