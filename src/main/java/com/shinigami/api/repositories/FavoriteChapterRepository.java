/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.repositories;

import com.shinigami.api.model.FavoriteChapterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteChapterRepository extends JpaRepository<FavoriteChapterModel, Integer> {
    void deleteByUserModel_UserId(String userId);
    Optional<List<FavoriteChapterModel>> findAllByUserModel_Id(int userId);
}
