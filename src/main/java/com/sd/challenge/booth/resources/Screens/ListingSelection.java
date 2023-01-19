package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.mapper.UiMapper;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.services.ui.UIType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListingSelection {

    public static Selection get(
            String baseUrl, String title, Stream<Poll> polls,
            Map<String, String> data
    ) {
        UiMapper mapper = new UiMapper();

        List<Element> items = polls
                .map(p -> mapper.mapPollToElement(p, baseUrl, data))
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            items.add(Element.builder()
                    .tipo(UIType.TEXT)
                    .texto("Sentimos muito, não há dados.")
                    .build());
        }

        return Selection.builder()
                .titulo(title)
                .itens(items)
                .build();
    }

}
