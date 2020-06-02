package com.mdt.taymyr.dto.card;

import lombok.Data;

@Data
public class Source {
    private String label;       // REQUIRED - short, human-readable label to display source of the card’s information
    private String url;         // OPTIONAL - optional absolute URL to load to learn more about the organization or data set
    private String icon;        // OPTIONAL - absolute url for an icon for the source of this card (100x100 pixel PNG without any transparent regions)
}
