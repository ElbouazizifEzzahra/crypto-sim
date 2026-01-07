import { useEffect, useState, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const useSocketTicker = (enabled = false) => {
    const [currentCandle, setCurrentCandle] = useState(null); // Changed default to null
    const [history, setHistory] = useState([]);
    
    // Use a Ref to prevent stale closures in the callback
    const historyRef = useRef([]);

    useEffect(() => {
        if (!enabled) return;

        const socket = new SockJS('http://localhost/ws-crypto/websocket'); 
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log("Connected to Real Data");
                
                client.subscribe('/topic/ticker/BTC', (message) => {
                    const data = JSON.parse(message.body);
                    
                    // 1. FORMAT DATA for Area Chart (Time + Value)
                    const price = parseFloat(data.price);
                    
                    // 2. CRITICAL FIX: Convert MS to Seconds for Lightweight Charts
                    // If we don't divide by 1000, the chart thinks the year is 50,000 AD
                    const timeInSeconds = Math.floor((data.timestamp || Date.now()) / 1000);

                    const newPoint = {
                        time: timeInSeconds, 
                        value: price
                    };

                    setCurrentCandle(newPoint);

                    setHistory(prev => {
                        const newHistory = [...prev, newPoint].slice(-100); // Keep last 100 points
                        historyRef.current = newHistory;
                        return newHistory;
                    });
                });
            },
        });

        client.activate();

        return () => client.deactivate();
    }, [enabled]);

    return { currentCandle, history };
};