package com.sd.challenge.booth.ui.poll_listing;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.resources.Screens.ListingSelection;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetPollListingTest extends MvcTest {

    @Autowired
    PollRepository repository;

    public static Stream<Arguments> getListingSuccessTestArguments() {
        return Stream.of(
                Arguments.of("Closed poll test", "1", "closed", "Iniciativas finalizadas"),
                Arguments.of("Open poll test", "1", "open", "Iniciativas em aberto"),
                Arguments.of("User poll test", "1", "user", "Suas iniciativas"),
                Arguments.of("Empty listing test", "4", "user", "Suas iniciativas")
        );
    }

    public static Stream<Arguments> getGatewayTagsTest() {
        return Stream.of(
                Arguments.of("Open polls test", "open", "user"),
                Arguments.of("Closed polls test","closed", "open"),
                Arguments.of("User polls test", "user", "closed")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getListingSuccessTestArguments")
    void getListingSuccessTest(String name, String userId, String listing, String title) throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("userId", userId, "listingOf", listing));
        Map<String, String> expectedData = new HashMap<>(Map.of("userId", userId, "listingOf", listing));

        Set<Poll> pollSet = getPollSet(listing, userId);

        Selection openPollListing = ListingSelection.get(baseUrl, title, pollSet, expectedData);

        performPostRequest("/ui/poll/listing/" + listing, data, status().isOk(),
                content().json(gson.toJson(openPollListing)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getGatewayTagsTest")
    void getGatewayTagsTest(
            String name, String endpoint, String tag
    ) throws Exception {
        Map<String, String> postData= Map.of(
                "userId", "1",
                "listingOf", tag
        );

        Form errorForm = PollExceptionHandler.errorForm(baseUrl, Map.of("userId", "1"));
        performPostRequest(
                "/ui/poll/listing/" + endpoint, postData,
                status().is5xxServerError(),
                content().json(gson.toJson(errorForm))
        );
    }

    private Set<Poll> getPollSet(String listing, String userId) {
        LocalDateTime now = LocalDateTime.now();
        if (listing.equals("closed"))
            return repository.findAllByEndsAtBefore(now);

        if (listing.equals("open"))
            return repository.findAllByOpenedAtBeforeAndEndsAtAfter(now, now);


        if (listing.equals("user"))
            return repository.findAllByOwner(Long.parseLong(userId));

        throw new RuntimeException();
    }

}
