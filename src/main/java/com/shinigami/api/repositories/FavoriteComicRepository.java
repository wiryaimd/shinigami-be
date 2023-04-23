package com.shinigami.api.repositories;

import com.shinigami.api.model.favorite.FavoriteComicModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteComicRepository extends JpaRepository<FavoriteComicModel, Integer> {

}
