package org.pac4j.oauth.run;

import lombok.val;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.run.RunClient;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.profile.facebook.FacebookConfiguration;
import org.pac4j.oauth.profile.facebook.FacebookProfile;
import org.pac4j.oauth.profile.facebook.FacebookRelationshipStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Run manually a test for the {@link FacebookClient}.
 *
 * @author Jerome Leleu
 * @since 1.9.0
 */
public final class RunFacebookClient extends RunClient {

    public static void main(String[] args) {
        new RunFacebookClient().run();
    }

    @Override
    protected String getLogin() {
        return "testscribeup@gmail.com";
    }

    @Override
    protected String getPassword() {
        return "testpwdscribeup";
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected IndirectClient getClient() {
        val facebookClient = new FacebookClient();
        facebookClient.setKey("1002857006444390");
        facebookClient.setSecret("c352c9668493d3f9ac3f0fa71f04c187");
        facebookClient.setCallbackUrl(PAC4J_URL);
        facebookClient
                .setScope("email,user_about_me,user_actions.books,user_actions.fitness,user_actions.music,user_actions.news,"
                    + "user_actions.video,user_birthday,user_education_history,user_events,user_friends,user_games_activity,"
                    + "user_hometown,user_likes,user_location,user_managed_groups,user_photos,user_posts,user_relationship_details,"
                    + "user_relationships,user_religion_politics,user_status,user_tagged_places,user_videos,user_website,"
                    + "user_work_history");
        facebookClient.setFields(FacebookConfiguration.DEFAULT_FIELDS
                + ",friends,movies,music,books,likes,albums,events,groups,music.listens,picture");
        facebookClient.setLimit(100);
        return facebookClient;
    }

    @Override
    protected void verifyProfile(final CommonProfile userProfile) {
        val profile = (FacebookProfile) userProfile;
        assertEquals("771361542992890", profile.getId());
        assertEquals(FacebookProfile.class.getName() + Pac4jConstants.TYPED_ID_SEPARATOR + "771361542992890",
                profile.getTypedId());
        assertTrue(ProfileHelper.isTypedIdOf(profile.getTypedId(), FacebookProfile.class));
        assertTrue(CommonHelper.isNotBlank(profile.getAccessToken()));
        assertCommonProfile(userProfile, null, "Jerome", "Testscribeup", "Jerome Testscribeup", null, Gender.MALE,
                Locale.FRANCE, "https://lookaside.facebook.com/platform/profilepic/?asid=771361542992890&height=50&width=50&ext=",
                "https://www.facebook.com/app_scoped_user_id/771361542992890/", "New York, New York");
        assertNull(profile.getMiddleName());
        val languages = profile.getLanguages();
        assertTrue(languages.get(0).getName().startsWith("Fr"));
        assertTrue(CommonHelper.isNotBlank(profile.getThirdPartyId()));
        assertEquals(8, profile.getTimezone().intValue());
        assertTrue(profile.getVerified());
        assertEquals("A propos de moi", profile.getAbout());
        assertEquals("03/10/1979", new SimpleDateFormat("MM/dd/yyyy").format(profile.getBirthday()));
        val educations = profile.getEducation();
        var education = educations.get(0);
        assertEquals("lycée mixte", education.getSchool().getName());
        assertEquals("High School", education.getType());
        education = educations.get(1);
        assertEquals("IngéNieur", education.getDegree().getName());
        assertNull(profile.getEmail());
        assertEquals("San Francisco, California", profile.getHometown().getName());
        assertEquals("female", profile.getInterestedIn().get(0));
        assertEquals("New York, New York", profile.getLocationObject().getName());
        assertEquals("Sans Opinion (desc)", profile.getPolitical());
        val favoriteAthletes = profile.getFavoriteAthletes();
        assertEquals("Surfeuse", favoriteAthletes.get(0).getName());
        val favoriteTeams = profile.getFavoriteTeams();
        assertEquals("Handball Féminin de France", favoriteTeams.get(0).getName());
        assertEquals("citation", profile.getQuotes());
        assertEquals(FacebookRelationshipStatus.MARRIED, profile.getRelationshipStatus());
        assertEquals("Athéisme (desc)", profile.getReligion());
        assertNull(profile.getSignificantOther());
        assertEquals("web site", profile.getWebsite());
        val works = profile.getWork();
        val work = works.get(0);
        assertEquals("Employeur", work.getEmployer().getName());
        assertEquals("Paris, France", work.getLocation().getName());
        assertEquals("Architecte Web", work.getPosition().getName());
        assertEquals("Description", work.getDescription());
        assertNull(work.getEndDate());
        val friends = profile.getFriends();
        assertEquals(1, friends.size());
        val friend = friends.get(0);
        assertEquals("Jérôme Leleu", friend.getName());
        assertEquals("874202936003234", friend.getId());
        val movies = profile.getMovies();
        assertEquals(1, movies.size());
        val movie = movies.get(0);
        assertEquals("Jean-Claude Van Damme", movie.getName());
        assertEquals("21497365045", movie.getId());
        assertEquals(1330030350000L, movie.getCreatedTime().getTime());
        val musics = profile.getMusic();
        assertEquals(1, musics.size());
        val music = musics.get(0);
        assertEquals("Hard rock", music.getName());
        assertEquals("112175695466436", music.getId());
        assertEquals(1330030350000L, music.getCreatedTime().getTime());
        val books = profile.getBooks();
        assertEquals(1, books.size());
        val book = books.get(0);
        assertEquals("Science fiction", book.getName());
        assertEquals("108157509212483", book.getId());
        assertEquals(null, book.getCategory());
        assertEquals(1330030350000L, book.getCreatedTime().getTime());
        val likes = profile.getLikes();
        assertEquals(9, likes.size());
        val like = likes.get(0);
        assertEquals("Boxing", like.getName());
        assertEquals("105648929470083", like.getId());
        assertEquals(1360152791000L, like.getCreatedTime().getTime());
        val albums = profile.getAlbums();
        assertEquals(3, albums.size());
        val album = albums.get(1);
        assertEquals("168023009993416", album.getId());
        val from = album.getFrom();
        assertNull(from);
        assertEquals("Profile Pictures", album.getName());
        val events = profile.getEvents();
        assertEquals(2, events.size());
        val event = events.get(0);
        assertEquals("Couronnement", event.getName());
        assertEquals("301212149963131", event.getId());
        assertEquals("attending", event.getRsvpStatus());
        assertNotNull(event.getStartTime());
        assertNotNull(event.getEndTime());
        val groups = profile.getGroups();
        val group = groups.get(0);
        assertEquals("Dev ScribeUP", group.getName());
        assertEquals("167694120024728", group.getId());
        val musicListens = profile.getMusicListens();
        assertNull(musicListens);
        val picture = profile.getPicture();
        assertFalse(picture.getSilhouette());
        assertEquals(35, profile.getAttributes().size());
    }
}
