package com.example.backend.controller;

import com.example.backend.dto.Favorites.FavoritesRequest;
import com.example.backend.entity.Favorites.Favorites;
import com.example.backend.repository.Favorites.FavoritesRepository;
import com.example.backend.dto.Response;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesHandler {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @GetMapping("/findAll")
    public List<Favorites> findAllFavorites() {
        return favoritesRepository.findAll();
    }

    @PostMapping("/add")
    public Response addFavorite(@RequestBody FavoritesRequest favoritesRequest) {
        Long workId = favoritesRequest.getWorkID();
        Long userId = favoritesRequest.getUserID();

        // 检查是否已经收藏过
        if (favoritesRepository.existsByWorkIDAndUserID(workId, userId)) {
            return new Response(400, "Already favorited");
        }

        Favorites newFavorite = new Favorites();
        newFavorite.setWorkID(workId);
        newFavorite.setUserID(userId);

        favoritesRepository.save(newFavorite);

        return new Response(200, "Favorite added successfully");
    }

    @PostMapping("/remove")
    @Transactional
    public Response removeFavorite(@RequestBody FavoritesRequest favoritesRequest) {
        Long workId = favoritesRequest.getWorkID();
        Long userId = favoritesRequest.getUserID();

        // 检查收藏记录是否存在
        if (!favoritesRepository.existsByWorkIDAndUserID(workId, userId)) {
            return new Response(404, "Favorite does not exist");
        }

        favoritesRepository.removeFavorite(workId, userId);

        return new Response(200, "Favorite removed successfully");
    }

    @GetMapping("/findByUserId")
    public List<Favorites> getFavoritesByUserId(@RequestHeader("userId") Long userId) {
        return favoritesRepository.findAllByUserID(userId);
    }


}