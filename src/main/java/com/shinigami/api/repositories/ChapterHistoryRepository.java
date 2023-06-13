/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.repositories;

import com.shinigami.api.model.ChapterHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterHistoryRepository extends JpaRepository<ChapterHistoryModel, Integer> {



}
