package com.debugagent.wordle.service;

import com.debugagent.wordle.db.Game;
import com.debugagent.wordle.db.GameRepository;
import com.debugagent.wordle.db.UserRepository;
import com.debugagent.wordle.db.WebUser;
import com.debugagent.wordle.webservices.CharacterResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WordleService {
    private static final String WORD = "LYMPH";
    private static final List<String> DICTIONARY = new ArrayList<>();

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    static {
        try(InputStream is = new ClassPathResource("words.txt").getInputStream()) {
            Scanner scanner = new Scanner(is).useDelimiter("\n");
            while(scanner.hasNext()) {
                DICTIONARY.add(scanner.next().toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String validate(String word) {
        if(word.length() != 5) {
            return "Bad Length!";
        }
        word = word.toUpperCase();
        if(!DICTIONARY.contains(word)) {
            return "Not a word!";
        }
        return null;
    }

    public CharacterResult[] calculateResults(String userId, String word) {
        word = word.toUpperCase();

        WebUser webUser = userRepository.findByUuid(userId)
                        .orElseGet(() -> new WebUser(userId));

        Game game = new Game();
        game.setWord(WORD);
        game.setGuess(word);
        if(webUser.getId() != null) {
            game.setAttempt((int) gameRepository.countByWordAndWebUser(WORD, webUser));
        }

        webUser.addGame(game);
        userRepository.save(webUser);
        gameRepository.save(game);

        CharacterResult[] result = new CharacterResult[WORD.length()];
        for(int iter = 0 ; iter < word.length() ; iter++) {
            char currentChar = word.charAt(iter);
            if(currentChar == WORD.charAt(iter)) {
                result[iter] = CharacterResult.GREEN;
                continue;
            }
            if(WORD.indexOf(currentChar) > -1) {
                result[iter] = CharacterResult.YELLOW;
                continue;
            }
            result[iter] = CharacterResult.BLACK;
        }
        return result;
    }

    public Map<String, Integer> getAttemptsPerWord(String userId) {
        Optional<WebUser> webUser = userRepository.findByUuid(userId);
        if(webUser.isPresent()) {
            Map<String, Integer> response = new HashMap<>();
            for(Game g : webUser.get().getGames()) {
                Integer count = response.getOrDefault(g.getWord(), 0);
                response.put(g.getWord(), count + 1);
            }
            return response;
        }
        return Collections.emptyMap();
    }
}
