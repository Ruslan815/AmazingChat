package ru.cft.team2.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "Новость")
public class Feed {

    @ApiModelProperty(
            value = "Заголовок",
            required = true,
            example = "someTitle"
    )
    final String title;

    @ApiModelProperty(
            value = "Ссылка на RSS ленту",
            required = true,
            example = "https://somesite.su"
    )
    final String link;

    @ApiModelProperty(
            value = "Описание",
            required = true,
            example = "someDescription"
    )
    final String description;

    @ApiModelProperty(
            value = "Язык",
            required = true,
            example = "russian"
    )
    final String language;

    @ApiModelProperty(
            value = "Авторское право",
            required = true
    )
    final String copyright;

    @ApiModelProperty(
            value = "Дата публикации",
            required = true,
            example = "14-07-2021"
    )
    final String pubDate;

    @ApiModelProperty(
            value = "Список входящих новостей"
    )
    final List<FeedMessage> entries = new ArrayList<FeedMessage>();

    public Feed(String title, String link, String description, String language,
                String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<FeedMessage> getMessages() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description
                + ", language=" + language + ", link=" + link + ", pubDate="
                + pubDate + ", title=" + title + "]";
    }

}
