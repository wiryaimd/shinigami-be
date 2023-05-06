/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test1 {

    @Test
    void download() throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL("http://shinigami.id/wp-content/uploads/WP-manga/data/manga_625ed791a7d7a/7f724d8854dc305c43e3671a5a88c0a0/12.jpg").openConnection();
        connection.getInputStream();

    }
}
