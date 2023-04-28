package com.shinigami.api.repositories;

import com.shinigami.api.repositories.favorite.FavoriteChapterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteChapterRepository extends JpaRepository<FavoriteChapterModel, Integer> {


}
