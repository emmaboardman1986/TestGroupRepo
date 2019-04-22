package com.example.testandroidapplication;

import android.app.Activity;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.testandroidapplication.objects.Artist;
import com.example.testandroidapplication.utils.ArtistResult;
import com.example.testandroidapplication.utils.WebClientMethods;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private String artistUserIdforTesting = "test";

    private TextView userName;
    private TextView userTagline;
    private TextView userDescription;
    private RatingBar userRating;
    private WebView webView;
    private ImageButton userFacebook;
    private ImageButton userInstagram;
    private ImageButton userTwitter;
    private ImageButton userSoundcloud;
    private ImageButton userWebsite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_profile,container,false);

        new ReadArtistProfileAsyncTask().execute();

        webView = v.findViewById(R.id.profile_web_view);

        userName = v.findViewById(R.id.profile_username);
        userTagline = v.findViewById(R.id.profile_tagline);
        userDescription = v.findViewById(R.id.profile_bio);
        userRating = v.findViewById(R.id.profile_rating);
        userFacebook = v.findViewById(R.id.profile_facebook);
        userInstagram = v.findViewById(R.id.profile_instagram);
        userTwitter = v.findViewById(R.id.profile_twitter);
        userSoundcloud = v.findViewById(R.id.profile_soundcloud);
        // image should be changed from email to website - want to encourage contact within app messaging, not email
        userWebsite = v.findViewById(R.id.profile_email);

        return v;
    }


    private class ReadArtistProfileAsyncTask extends AsyncTask<String, String, Artist> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Artist doInBackground(String... params) {
            new WebClientMethods();
            ArtistResult artistResult = WebClientMethods.readArtistProfile(artistUserIdforTesting);
            return artistResult.getArtist();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(final Artist artist) {

            populateUI(artist);

                }
            }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void populateUI(final Artist artist) {

        userName.setText(artist.getUser().getName());

        if (artist.getProfileInformation().getTagLine() != null || !artist.getProfileInformation().getTagLine().equals("")) {
            userTagline.setText(artist.getProfileInformation().getTagLine());
        } else {
            userTagline.setVisibility(View.GONE);
        }

        userDescription.setText(artist.getProfileInformation().getDescription());
        userRating.setRating(Float.parseFloat(artist.getProfileInformation().getOverallRatingNum()));
        userFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       webView.loadUrl("https://www.facebook.com/" + artist.getProfileInformation().getFacebookLink());
                    }
                });

//        !artist.getProfileInformation().getInstagramLink().equals("") ||
//         artist.getProfileInformation().getInstagramLink() != null

        String instagram = artist.getProfileInformation().getInstagramLink();

        if (!Objects.equals(instagram, "null")
                || !Objects.equals(instagram, "")
                || !Objects.equals(instagram, "not populated")
                || instagram != null ){
            Log.i("Tag", "not null");
            userInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("https://www.instagram.com/" + artist.getProfileInformation().getInstagramLink());
                }
            });
        } else {
            Log.i("Tag", "null");
            userInstagram.setVisibility(View.GONE);
        }




        userTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://www.twitter.com/" + artist.getProfileInformation().getTwitterLink());
            }
        });
        userSoundcloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://www.soundcloud.com/" + artist.getSoundCloudLink());
            }
        });
        userWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(artist.getProfileInformation().getWebPageLink());
            }
        });

    }
}
