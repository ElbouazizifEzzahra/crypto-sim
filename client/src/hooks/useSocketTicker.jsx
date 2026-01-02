import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const useSocketTicker = (enabled = false) => {
    const [lastCandle, setLastCandle] = useState(null);

    useEffect(() => {
        if (!enabled) return; // STOP if disabled

        console.log("🔌 Attempting Real WebSocket Connection...");
        
        // 1. Setup Client
        const socket = new SockJS('http://localhost:8080/ws'); // Backend URL
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log(" Connected to Backend!");
                client.subscribe('/topic/ticker', (message) => {
                    const data = JSON.parse(message.body);
                    setLastCandle(data);
                });
            },
            onStompError: (frame) => {
                console.error(' Broker reported error: ' + frame.headers['message']);
            }
        });

        client.activate();

        return () => client.deactivate();
    }, [enabled]);

    return lastCandle;
};