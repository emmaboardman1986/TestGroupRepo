package com.example.testandroidapplication.View.createProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testandroidapplication.Presenter.createProfile.ArtistProfileCreationPresenter;
import com.example.testandroidapplication.Presenter.createProfile.IArtistProfileCreationContract;
import com.example.testandroidapplication.R;
import com.example.testandroidapplication.WelcomeFragement;
import com.example.testandroidapplication.objects.Artist;
import com.example.testandroidapplication.objects.ProfileInformation;
import com.example.testandroidapplication.objects.Tags;
import com.example.testandroidapplication.objects.User;
import com.example.testandroidapplication.utils.CheckNetworkStatus;

import java.util.List;

public class ArtistProfileCreation extends Fragment implements IArtistProfileCreationContract.View {


    private User user;
    private ArtistProfileCreationPresenter presenter;

    private EditText artistNameEditText;
    private EditText artistTaglineEditText;
    private EditText artistLocationEditText;
    private Spinner artistExperienceSpinner;
    private Spinner artistGenreSpinner;
    private Spinner artistInstrumentSpinner;
    private Spinner artistGroupTypeSpinner;
    private Spinner artistLookingForSpinner;
    private EditText artistDescriptionEditText;
    private EditText artistFacebookEditText;
    private EditText artistTwitterEditText;
    private EditText artistInstagramEditText;
    private EditText artistWebsiteEditText;
    private EditText artistSoundcloudEditText;


    private String artistNameInput;
    private String artistTaglineInput;
    private String artistLocationInput;
    private Tags artistTags;
    private String artistDescriptionInput;
    private String artistFacebookInput;
    private String artistTwitterInput;
    private String artistInstagramInput;
    private String artistWebsiteInput;
    private String artistSoundcloudInput;

    Artist artist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.artist_profile_creation, container, false);

        presenter = new ArtistProfileCreationPresenter(this);
        presenter.readArtistTags();

        final String userId = getArguments().getString("USER_ID");
        final String userEmail = getArguments().getString("USER_EMAIL");
        final String userName = getArguments().getString("USER_NAME");

        artistNameEditText = v.findViewById(R.id.artist_name);
        artistTaglineEditText = v.findViewById(R.id.artist_tagline);
        artistLocationEditText = v.findViewById(R.id.artist_location);
        artistExperienceSpinner = v.findViewById(R.id.artist_tag_search_spinner_experience);
        artistGenreSpinner = v.findViewById(R.id.artist_tag_search_spinner_genre);
        artistInstrumentSpinner = v.findViewById(R.id.artist_tag_search_spinner_instruments);
        artistGroupTypeSpinner = v.findViewById(R.id.artist_tag_search_spinner_group_type);
        artistLookingForSpinner = v.findViewById(R.id.artist_tag_search_spinner_looking_for);
        artistDescriptionEditText = v.findViewById(R.id.artist_description);
        artistFacebookEditText = v.findViewById(R.id.artist_facebook);
        artistTwitterEditText = v.findViewById(R.id.artist_twitter);
        artistInstagramEditText = v.findViewById(R.id.artist_instagram);
        artistWebsiteEditText = v.findViewById(R.id.artist_webpage);
        artistSoundcloudEditText = v.findViewById(R.id.artist_soundcloud_link);

        Button createProfile = v.findViewById(R.id.artist_create_profile);

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckNetworkStatus.isNetworkAvailable(getActivity().getApplicationContext())) {

                    user = new User(userId, userName, userEmail);

                    artistNameEditText.setText(userName);
                    artistNameInput = artistNameEditText.getText().toString();
                    artistTaglineInput = artistTaglineEditText.getText().toString();
                    artistLocationInput = artistLocationEditText.getText().toString();

                    artistTags = new Tags();

                    populateTagsFromSpinner(artistExperienceSpinner, "Experience");
                    populateTagsFromSpinner(artistGenreSpinner, "Genre");
                    populateTagsFromSpinner(artistInstrumentSpinner, "Instruments");
                    populateTagsFromSpinner(artistGroupTypeSpinner, "Group Type");
                    populateTagsFromSpinner(artistLookingForSpinner, "Looking For");

                    artistDescriptionInput = artistDescriptionEditText.getText().toString();
                    artistFacebookInput = artistFacebookEditText.getText().toString();
                    artistTwitterInput = artistTwitterEditText.getText().toString();
                    artistInstagramInput = artistInstagramEditText.getText().toString();
                    artistWebsiteInput = artistWebsiteEditText.getText().toString();
                    artistSoundcloudInput = artistSoundcloudEditText.getText().toString();
                    buildArtistObject();

                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(getActivity(),
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
                WelcomeFragement welcomeFragement = new WelcomeFragement();

                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", userId);
                bundle.putString("USER_EMAIL", userEmail);
                bundle.putString("USER_NAME", userName);
                welcomeFragement.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,
                        welcomeFragement).commit();

            }
        });

        return v;
    }

    private void populateTagsFromSpinner(Spinner spinner, String category) {
        if (!spinner.getSelectedItem().toString().contains("Select")) {
            artistTags.addTag(category, spinner.getSelectedItem().toString());
        }
    }

    public void buildArtistObject(){

        ProfileInformation profileInformation = new ProfileInformation
                .ProfileBuilder()
                .withTagline(artistTaglineInput)
                .withLocation(artistLocationInput)
                .withSearchTags(artistTags)
                .withDescription(artistDescriptionInput)
                .withFacebookLink(artistFacebookInput)
                .withTwitterLink(artistTwitterInput)
                .withInstagramLink(artistInstagramInput)
                .withWebPageLink(artistWebsiteInput)
                .build();

        artist = new Artist
                .ArtistBuilder(user)
                .withProfileInformation(profileInformation)
                .withSoundcloudLink(artistSoundcloudInput)
                .build();

        presenter.validateArtistObject(artist);

    }

    public void showExperienceSpinner(List<String> experienceTagList){
        showSpinner(artistExperienceSpinner, experienceTagList);
    }

    public void showGenreSpinner(List<String> genreTagList){
        showSpinner(artistGenreSpinner, genreTagList);
    }

    public void showInstrumentsSpinner(List<String> instrumentsTagList){
        showSpinner(artistInstrumentSpinner, instrumentsTagList);
    }

    public void showGroupTypeSpinner(List<String> groupTypeTagList){
        showSpinner(artistGroupTypeSpinner, groupTypeTagList);
    }

    public void showLookingForSpinner(List<String> lookingForTagList){
        showSpinner(artistLookingForSpinner, lookingForTagList);
    }

    private void showSpinner(Spinner spinner, List<String> tagList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, tagList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

