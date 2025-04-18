package com.jargal.flashcard;

import java.util.List;

public interface CardOrganizer {
    /**
     * Organizes the cards according to the specific strategy.
     *
     * @param cards The list of cards to be organized
     * @return The organized list of cards
     */
    List<Card> organize(List<Card> cards);
}