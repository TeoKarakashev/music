package music.web;

import music.model.binding.AlbumBindingModel;
import music.model.service.AlbumServiceModel;
import music.service.AlbumService;
import music.service.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    private final ModelMapper modelMapper;
    private final AlbumService albumService;
    private final ArtistService artistService;

    public AlbumController(ModelMapper modelMapper, AlbumService albumService, ArtistService artistService) {
        this.modelMapper = modelMapper;
        this.albumService = albumService;
        this.artistService = artistService;
    }

    @ModelAttribute("albumBindingModel")
    public AlbumBindingModel albumBindingModel(){
        return new AlbumBindingModel();
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView){
        modelAndView.addObject("artists", artistService.findALl());
        modelAndView.setViewName("add-album");
        return modelAndView;
    }

    @PostMapping("/add")
    public String addConfirm(@Valid AlbumBindingModel albumBindingModel, @AuthenticationPrincipal UserDetails principal){
        AlbumServiceModel albumServiceModel = this.modelMapper.map(albumBindingModel, AlbumServiceModel.class);
        albumServiceModel.setUsername(principal.getUsername());
        albumService.CreateAlbum(albumServiceModel);
        return "redirect:/";
    }



}
