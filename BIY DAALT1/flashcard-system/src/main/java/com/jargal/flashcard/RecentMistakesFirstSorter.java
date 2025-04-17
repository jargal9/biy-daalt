// RecentMistakesFirstSorter.java
package com.jargal.flashcard;

import java.util.ArrayList;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> incorrectCards = new ArrayList<>();
        List<Card> correctCards = new ArrayList<>();
        
        // Separate cards based on last answer status
        for (Card card : cards) {
            if (!card.isAnsweredCorrectlyLastTime()) {
                incorrectCards.add(card);
            } else {
                correctCards.add(card);
            }
        }
        
        // Combine lists with incorrect cards first
        List<Card> result = new ArrayList<>(incorrectCards);
        result.addAll(correctCards);
        return result;
    }
}
