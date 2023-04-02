package com.shinigami.api.service;

import com.shinigami.api.model.BrowseModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BrowseService {

    public BrowseModel scrapBrowse() throws IOException {
        Document document = Jsoup.connect("https://shinigami.id/")
                .userAgent("Mozilla/5.0")
                .get();

        Elements elements = document.select("a.series-link");

        List<String> all = new ArrayList<>();

        for (Element element : elements) {
            all.add(element.attr("abs:href"));
        }

        List<String> hotList = all.size() >= 8 ? all.stream().limit(8).toList() : all;
        List<String> newsList = all.size() >= 16 ? all.stream().skip(8).limit(16).toList() : List.of();
//        List<String> udpateList = all.size() >= 16 ? all.stream().skip(8).limit(16).toList() : List.of();

        return new BrowseModel(hotList, newsList, null);
    }

}
