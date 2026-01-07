/*
package com.cryptosim.crypto_sim.service.data;


import com.cryptosim.crypto_sim.dto.data.LeaderboardEntry;
import com.cryptosim.crypto_sim.dto.portfolio.PortfolioResponse;
import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class LeaderboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioService portfolioService;

    public List<LeaderboardEntry> getGlobalLeaderboard() {
        List<User> allUsers = userRepository.findAll();
        List<LeaderboardEntry> entries = new ArrayList<>();

        for (User user : allUsers) {
            // Re-use your Step 1 logic to get their current value
            PortfolioResponse portfolio = portfolioService.getUserPortfolio(user.getId());

            entries.add(new LeaderboardEntry(
                    user.getFirstName() + " " + user.getLastName(),
                    portfolio.getTotalNetWorth(),
                    0 // Placeholder for rank
            ));
        }

        // Sort by Net Worth Descending
        entries.sort(Comparator.comparing(LeaderboardEntry::getTotalNetWorth).reversed());

        // Assign Ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }

        return entries;
    }
}*/
