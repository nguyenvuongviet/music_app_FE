package com.example.music_app.services;

import com.example.music_app.models.Album;
import com.example.music_app.models.Artist;
import com.example.music_app.models.ArtistResponse;
import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.ChangePasswordRequest;
import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.ListPlaylistResponse;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.OtpResponse;
import com.example.music_app.models.PlaylistRequest;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongCommentRequest;
import com.example.music_app.models.SongCommentResponse;
import com.example.music_app.models.SongResponse;

import java.util.List;
import com.example.music_app.models.ResetPasswordRequest;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.SongLikedRequest;
import com.example.music_app.models.SongLikedResponse;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.UpdateProfileRequest;
import com.example.music_app.models.User;
import com.example.music_app.models.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerModel);

    @POST("auth/authenticate")
    Call<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @POST("auth/authenticate-oauth")
    Call<LoginResponse> authenticateOAuth(@Body RegisterRequest registerRequest);

    @GET("auth/register/confirm")
    Call<OtpResponse> verifyOtp(@Query("token") String token, @Query("type") String type);

    @POST("auth/send-email")
    Call<ResponseMessage> sendOtp(@Body ForgotPassword forgotPassword);

    @PATCH("user/forgot-password")
    Call<RegisterResponse> changePassword(@Body LoginRequest loginRequest);


    // Songs API
    @GET("song/most-views")
    Call<GenericResponse<SongResponse>> getMostViewSong(@Query("page") int page, @Query("size") int size);

    @GET("song/most-likes")
    Call<GenericResponse<SongResponse>> getMostLikeSong(@Query("page") int page, @Query("size") int size);

    @GET("song/new-released")
    Call<GenericResponse<SongResponse>> getSongNewReleased(@Query("page") int page, @Query("size") int size);

    @PATCH("song/{id}/view")
    Call<GenericResponse<Song>> increaseViewOfSongBySongId(@Path("id") Long songId);

    @GET("song/{songId}/artists")
    Call<GenericResponse<List<Artist>>> getArtistsBySongId(@Path("songId") Long songId);

    @PATCH("user/forgot-password")
    Call<ResponseMessage> changePassword(@Body ResetPasswordRequest resetPasswordRequest);

    @GET("user/{id_user}/playlists")
    Call<ListPlaylistResponse> getPlaylistByIdUser(@Path("id_user") int id_user);

    @GET("user/{id_user}/liked-songs")
    Call<GenericResponse<List<Song>>> getSongLikedByIdUser(@Path("id_user") int id_user);

    @GET("songs")
    Call<GenericResponse<List<Song>>> getAllSongs();

    @POST("playlist")
    Call<PlaylistResponse> createPlaylist(@Body PlaylistRequest playlistRequest);

    @GET("playlist/{id_playlist}")
    Call<PlaylistResponse> getPlaylistById(@Path("id_playlist") int id_playlist);

    @DELETE("playlist/{id_playlist}")
    Call<ResponseMessage> deletePlaylist(@Path("id_playlist") int id_playlist);

    @GET("user/{id_user}/not-liked-songs")
    Call<GenericResponse<List<Song>>> getNotLikedSongsByIdUser(@Path("id_user") int id_user);

    @POST("songLiked/songs")
    Call<ResponseMessage> addSongsToFavourite(@Body SongLikedRequest songLikedRequest);

    @PATCH("user/{id_user}/change-password")
    Call<ResponseMessage> changePasswordWithIdUser(@Path("id_user") int id_user, @Body ChangePasswordRequest changePasswordRequest);

    @Multipart
    @PATCH("user/update")
    Call<UserResponse> updateProfile(@Part("idUser") Long idUser, @Part MultipartBody.Part imageFile, @Part("firstName") String firstName, @Part("lastName") String lastName, @Part("gender") int gender);

    @GET("songLiked/isUserLikedSong")
    Call<SongLikedResponse> isUserLikedSong(@Query("songId") Long songId, @Query("userId") Long userId);

    @POST("songLiked/toggle-like")
    Call<SongLikedResponse> toggleLike(@Query("songId") Long songId, @Query("userId") Long userId);

    @POST("playlistSong/{id_playlist}/{id_song}")
    Call<ResponseMessage> addSongToPlaylist(@Path("id_playlist") Long id_playlist, @Path("id_song") Long id_song);

    @GET("playlist")
    Call<ResponseMessage> isPlaylistNameExists(@Query("name") String name);

    @GET("song/{id_song}/comments")
    Call<SongCommentResponse> getAllCommentsOfSong(@Path("id_song") Long idSong);

    @POST("playlist/{id_playlist}")
    Call<ResponseMessage> updatePlaylistName(@Path("id_playlist") int i, @Query("name") String name);

    @DELETE("playlistSong/{id_playlist}/{id_song}")
    Call<ResponseMessage> deleteSongFromPlaylist(@Path("id_playlist") Long id_playlist, @Path("id_song") Long id_song);
  
    @POST("song/post-comment")
    Call<ResponseMessage> postComment(@Body SongCommentRequest songCommentRequest);

    @GET("user/searchArtist")
    Call<GenericResponse<List<Artist>>> searchArtist(@Query("query") String query);

    @GET("songs/search")
    Call<GenericResponse<List<Song>>> searchSong(@Query("query") String query);

    @GET("artists")
    Call<GenericResponse<ArtistResponse>> getAllArtists(@Query("page") int page, @Query("size") int size);

    @GET("artist/{id}")
    Call<GenericResponse<Artist>> getArtistById(@Path("id") int id);

    @GET("artist/{idArtist}/songs/count")
    Call<GenericResponse<Integer>> getSongCountByArtistId(@Path("idArtist") int idArtist);

    @GET("artist/{artistId}/songs")
    Call<GenericResponse<SongResponse>> getAllSongsByArtistId(@Path("artistId") int artistId, @Query("page") int page, @Query("size") int size);

    @GET("user/{id_user}/is-followed-artist")
    Call<GenericResponse<Boolean>> isFollowedArtist(@Path("id_user") int id_user, @Query("id_artist") int id_artist);

    @POST("user/{id_user}/follow-artist")
    Call<GenericResponse<Boolean>> followArtist(@Path("id_user") int id_user, @Query("id_artist") int id_artist);

    @GET("/song/isUserLikedComment")
    Call<GenericResponse<Boolean>> isUserLikedComment(@Query("commentId") Long commentId, @Query("userId") Long userId);

    @POST("song/comment/like")
    Call<GenericResponse<Boolean>> likeComment(@Query("commentId") Long commentId, @Query("userId") Long userId);

    @GET("song/comment/countLikes")
    Call<GenericResponse<Long>> countLikesOfComment(@Query("commentId") Long commentId);

    @GET("song/{id}")
    Call<GenericResponse<Song>> getSongById(@Path("id") Long id);
  
    @GET("albums/artist/{id_artist}")
    Call<GenericResponse<List<Album>>> getAlbumsByArtistId(@Path("id_artist") int id_artist);

    @GET("album/{id_album}/songs")
    Call<GenericResponse<List<Song>>> getSongsByAlbumId(@Path("id_album") int id_album);

    @GET("album/{id_album}")
    Call<GenericResponse<Album>> getAlbumById(@Path("id_album") int id_album);
}
