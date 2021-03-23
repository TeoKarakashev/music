package music.service.impl;

import music.model.entities.AlbumEntity;
import music.model.entities.UserEntity;
import music.model.service.AlbumServiceModel;
import music.repository.AlbumRepository;
import music.repository.UserRepository;
import music.service.AlbumService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Autowired
    public AlbumServiceImpl(ModelMapper modelMapper, AlbumRepository albumRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }



    @Override
    public void CreateAlbum(AlbumServiceModel albumServiceModel) {
        AlbumEntity albumEntity = this.modelMapper.map(albumServiceModel, AlbumEntity.class);
        UserEntity userEntity = userRepository.findByName(albumServiceModel.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Creator not found"));
        albumEntity.setUserEntity(userEntity);

        this.albumRepository.save(albumEntity);
    }
}
