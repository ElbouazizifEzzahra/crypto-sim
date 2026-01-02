import { useEffect, useState } from 'react';

export const useMockTicker = (initialPrice = 95000, enabled = true) => {
    const [currentCandle, setCurrentCandle] = useState(null);
    const [history, setHistory] = useState([]);

    useEffect(() => {
        if (!enabled) return;

        // 1. Generate 100 bars of Fake History
        const initialData = [];
        let time = Math.floor(Date.now() / 1000) - 100; // Start 100 seconds ago
        let price = initialPrice;

        for (let i = 0; i < 100; i++) {
            const volatility = 20; 
            const change = (Math.random() - 0.5) * volatility;
            const nextPrice = price + change;
            
            initialData.push({
                time: time + i,
                open: price,
                high: Math.max(price, nextPrice) + 15,
                low: Math.min(price, nextPrice) - 15,
                close: nextPrice,
            });
            price = nextPrice;
        }
        setHistory(initialData);

        // 2. Start Live Updates
        let livePrice = price; 
        let liveTime = Math.floor(Date.now() / 1000);

        const interval = setInterval(() => {
            const change = (Math.random() - 0.5) * 30;
            const nextPrice = livePrice + change;

            const candle = {
                time: liveTime,
                open: livePrice,
                high: Math.max(livePrice, nextPrice) + 5,
                low: Math.min(livePrice, nextPrice) - 5,
                close: nextPrice,
            };

            setCurrentCandle(candle);
            livePrice = nextPrice;
            liveTime++;
        }, 1000);

        return () => clearInterval(interval);
    }, [enabled, initialPrice]);

    // Return object with BOTH history and live data
    return { currentCandle, history }; 
};