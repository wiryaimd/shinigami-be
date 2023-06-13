/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.repositories;

import com.shinigami.api.model.ComicHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicHistoryRepository extends JpaRepository<ComicHistoryModel, Integer> {


    Optional<ComicHistoryModel> findByComicUrl(String comicUrl);
}
