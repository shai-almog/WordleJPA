package com.debugagent.wordle;

import com.debugagent.wordle.service.WordleService;
import com.debugagent.wordle.webservices.CharacterResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestWordleService {
    @Autowired
    private WordleService wordleService;

    @Test
    public void testGuess() {
        CharacterResult[] results = wordleService.calculateResults("2222", "using");
        assertThat(results).hasSize(5)
                .allMatch(c -> c == CharacterResult.BLACK);

        var result = wordleService.getAttemptsPerWord("2222");
        assertThat(result).hasSize(1)
                .containsEntry("LYMPH", 1);
    }
}
