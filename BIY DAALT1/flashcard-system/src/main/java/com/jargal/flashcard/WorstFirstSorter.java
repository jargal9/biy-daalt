package com.jargal.flashcard;

import java.util.ArrayList;
import java.util.List;

public class WorstFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> worstFirst = new ArrayList<>();

    for (int i = 0; i < cards.size(); i++) {
        for (int j = i + 1; j < cards.size(); j++) {
            Card a = cards.get(i);
            Card b = cards.get(j);
            
            boolean swap = false;
            if (a.getCorrectAnswers() > b.getCorrectAnswers()) {
                swap = true;
            } else if (a.getCorrectAnswers() == b.getCorrectAnswers()) {
                if (a.getAttempts() < b.getAttempts()) {
                    swap = true;
                }
            }
            
            if (swap) {
                cards.set(i, b);
                cards.set(j, a);
            }
        }
    }

    return cards;
}
}
