package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.mapper.UiMapper;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.services.ui.UIType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListingSelection {

    public static Selection get(
            String baseUrl, String title, Set<Poll> polls,
            Map<String, String> data
    ) {
        UiMapper mapper = new UiMapper();

        List<Element> items = polls.stream()
                .map(p -> mapper.mapPollToElement(p, baseUrl, data))
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            data.remove("listingOf");
            items.add(Element.builder()
                    .tipo(UIType.TEXT)
                    .texto("Sentimos muito, não há dados.")
                    .url(baseUrl + "/ui/poll-gateway")
                    .body(data)
                    .build());
        }

        return Selection.builder()
                .titulo(title)
                .itens(items)
                .build();
    }

}
