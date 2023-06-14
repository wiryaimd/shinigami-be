/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.repositories;

import com.shinigami.api.model.UserHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistoryModel, Integer> {


    // select * from _users_history where _users_history.chapterUrl = param1 and _users.id = param2
    Optional<UserHistoryModel> findByChapterUrlAndUserModel_Id(String chapterUrl, int userId);

    Optional<List<UserHistoryModel>> findAllByComicUrlAndUserModel_UserId(String comicUrl, String userId);
}
