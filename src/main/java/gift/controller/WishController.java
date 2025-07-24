package gift.controller;

import gift.dto.WishDTO;
import gift.dto.WishRequestDTO;
import gift.annotation.LoginUser;
import gift.model.User;
import gift.model.Wish;
import gift.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @PostMapping("/wish/add")
    public void addWish(@RequestBody WishRequestDTO request, @LoginUser User user){
        wishService.addWish(user.getId(), request.getProductid());
    }

    @DeleteMapping("/wish/delete")
    public void deleteWish(@RequestBody WishRequestDTO request,@LoginUser User user){
        wishService.deleteWish(user.getId(), request.getProductid());
    }
    @GetMapping("/wish/list")
    public List<Wish> getWishList(@LoginUser User user){return wishService.getAllWish(user.getId());}

    @PatchMapping("/wish/inc")
    public void incrementWish(@RequestBody WishRequestDTO request, @LoginUser User user){
        wishService.increaseWish(user.getId(), request.getProductid());
    }
    @PatchMapping("wish/dec")
    public void decrementWish(@RequestBody WishRequestDTO request, @LoginUser User user){
        wishService.decreaseWish(user.getId(), request.getProductid());
    }
    @GetMapping("/wish/paged")
    public Page<WishDTO> getPagedWishList(
            @LoginUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return wishService.getPagedWishList(user.getId(), pageable)
                .map(WishDTO::new);
    }
}