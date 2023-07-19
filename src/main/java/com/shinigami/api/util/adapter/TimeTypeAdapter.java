/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    // untuk write toJson() dimana class LocalDateTime di conversi menjadi string di json
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }

    @Override
    // method read digunakan ketika convert json yg berisikan time string menjadi object class LocalDateTime
    // eg Gson.parse() dimana pada return resultnya perlu ada LocalDateTime sehingga cara parse dari jsonReader string ini dengan method dibawah
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString());
    }
}
