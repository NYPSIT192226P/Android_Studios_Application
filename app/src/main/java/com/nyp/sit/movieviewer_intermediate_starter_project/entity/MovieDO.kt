package com.nyp.sit.movieviewer_intermediate_starter_project.entity

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import java.util.*

@DynamoDBTable(tableName = "UserData")
class MovieDO {

    @DynamoDBHashKey(attributeName = "id")
    var id: String? = null

    @DynamoDBAttribute(attributeName = "favMovie")
    var DOmovieList: MutableList<Movie>? = null

    @DynamoDBDocument
    class Movie() {
        @DynamoDBAttribute(attributeName = "PosterPath")
        var posterpath: String? = null

        @DynamoDBAttribute(attributeName = "adult")
        var adult: String? = null

        @DynamoDBAttribute(attributeName = "overview")
        var overview: String? = null

        @DynamoDBAttribute(attributeName = "ReleaseDate")
        var releasedate: String? = null

        @DynamoDBAttribute(attributeName = "GenreIds")
        var genreid: String? = null

        @DynamoDBAttribute(attributeName = "movieId")
        var movidId: String? = null

        @DynamoDBAttribute(attributeName = "OriginalTitle")
        var originaltitle: String? = null

        @DynamoDBAttribute(attributeName = "OriginalLanguage")
        var originallanguage: String? = null

        @DynamoDBAttribute(attributeName = "title")
        var title: String? = null

        @DynamoDBAttribute(attributeName = "BackdropPath")
        var backdroppath: String? = null

        @DynamoDBAttribute(attributeName = "popularity")
        var popularity: String? = null

        @DynamoDBAttribute(attributeName = "VoteCount")
        var votecount: String? = null

        @DynamoDBAttribute(attributeName = "video")
        var video: String? = null

        @DynamoDBAttribute(attributeName = "VoteAverage")
        var voteaverage: String? = null

    }

}