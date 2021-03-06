package com.mdt.prodigy.dto.card;

import lombok.Data;

@Data
public abstract class Card {

    private String summary;      // REQUIRED - <140-character summary sentence for display to the user inside of this card
    private String detail;       // OPTIONAL - optional detailed information to display (GitHub Flavored Markdown)
    private String indicator;    // REQUIRED - urgency/importance of what this card conveys (info/warning/critical)
    private Source source;       // REQUIRED - grouping structure for the Source of information displayed on this card
    private String suggestions;  // OPTIONAL - array of Suggestions for changes in the context of the current activity
    private String selectionBehavior;    // OPTIONAL - intended behavior of the suggestions. If suggestions present, value must be at-most-one
}
