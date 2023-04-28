package com.shinigami.api.repositories.favorite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shinigami.api.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_favorite_comic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteComicModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title, url, cover, latestChapter;

    @ManyToOne // means that many instances of this entity are mapped to one instance of UserModel entity
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserModel userModel;

}
