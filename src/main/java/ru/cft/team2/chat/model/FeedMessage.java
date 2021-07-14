package ru.cft.team2.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Отображение новостей")
public class FeedMessage {

    @ApiModelProperty(
            value = "Заголовок",
            required = true,
            example = "someTitle"
    )
    String title;

    @ApiModelProperty(
            value = "Ссылка на RSS ленту",
            required = true,
            example = "https://somesite.su"
    )
    String link;

    @ApiModelProperty(
            value = "Автор",
            required = true,
            example = "Some Author"
    )
    String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "[title = " + title + ", link = " + link + ", author = " + author + "]";
    }

}
