import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

// ===========================================================================
//  BRICOLAGE: Fake History (Bach l'chart maybanch 9re3 f l'bdaya)
// ===========================================================================
// Hna kan cree l'history b7al ila l'app kant 5ddama mn ch7al hadi.
// Kan rj3o lor (backwards) mn awwal prix wslna bach tban l'9adia seamless.
const generateBackwardsHistory = (endTime, startPrice, timeframe) => {
  const history = [];
  let price = startPrice;

  // Loop 100 times = 100 minutes dial l'kdoub (fake data)
  for (let i = 1; i <= 100; i++) {
    // Kan n9sso wa9t b 'timeframe' (d9i9a b d9i9a)
    const time = endTime - i * timeframe;

    // 1. TA7ARROKAT (Volatility)
    // (Math.random() - 0.5) * 30 -> Ze3ma l'prix kay t7rrek b +/- $15
    // Ila bghiti tzid l'action, zid f had rra9m (30)
    const move = (Math.random() - 0.5) * 30;

    // Hna l'logic m9loub 7it raj3in f zman (Backwards):
    // Close dial had l'candle hwa l'Price li 3ndna daba
    const close = price;
    const open = price - move; // Open kan 7ssboh b l'inverse

    // 2. LES WICKS (Douk l'ibari f jenb)
    // Bach l'candle matbanch b7al chi brika (box), kan zido chwia d l'mess
    // Random between $1 and $11
    const wickHeight = Math.random() * 10 + 1;

    const high = Math.max(open, close) + wickHeight; // Raiss (Top)
    const low = Math.min(open, close) - wickHeight; // L'9a3 (Bottom)

    // Zidhom f l'bdaya d l'array (.unshift) bach yrtbo mzyan
    history.unshift({ time, open, high, low, close });

    // Prepare l'candle jaya (li hiya l'candle 9dima f zman)
    price = open;
  }
  return history;
};

// ===========================================================================
//  HOOK: MOUL CHI (Le Boss)
// ===========================================================================
export const useSocketTicker = (enabled = false) => {
  // ⚙️ CONFIG: Candle dial d9i9a (60 seconds)
  const TIMEFRAME_SECONDS = 60;

  // Had l'array ghadi y3mr b l'fake history f l'bdaya
  const [history, setHistory] = useState([]);
  // Hada hwa l'candle li kay tssawb daba (Live)
  const [currentCandle, setCurrentCandle] = useState(null);

  // Refs bach nstayw à jour m3a l'websocket bla machkil d l'closure
  const activeCandleRef = useRef(null);
  const historyRef = useRef([]);

  useEffect(() => {
    if (!enabled) {
      // If WebSocket is disabled, initialize with mock data so chart can render
      const initialPrice = 95000;
      const now = Math.floor(Date.now() / 1000);
      const fakeHistory = generateBackwardsHistory(now, initialPrice, TIMEFRAME_SECONDS);
      setHistory(fakeHistory);
      historyRef.current = fakeHistory;
      return;
    }

    // 1. CONNECTI M3A L'BACKEND
    // Nginx (Port 80) -> Spring Boot (Port 8080)
    // Matenssach ra SockJS darori hna
    const socket = new SockJS("/ws-crypto");
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000, // Ila ta7et l'conx, 3awed jarreb
      onConnect: () => {
        console.log("✅ WebSocket connected successfully");

        // 2. TSSNT L'TOPIC
        client.subscribe("/topic/ticker/BTC", (message) => {
          const data = JSON.parse(message.body);
          let realPrice = parseFloat(data.price);

          //  L'ASTUCE: JITTER (Zid chwia d l'mel7a)
          // Ila l'prix dial bss7 tabet bzaf, chart kayban meyyet.
          // Kan zido chwia d l'3chwa2iya (+/- $1) bach yban l'graph 7ay.
          const jitter = (Math.random() - 0.5) * 2;
          const price = realPrice + jitter;

          // 3. TIME BUCKET LOGIC
          // Kan jam3o l'updates kamlin f d9i9a w7da
          // Ex: 10:05:15 w 10:05:55 -> bjojhom tab3in l'candle d 10:05:00
          const serverTime = Math.floor((data.timestamp || Date.now()) / 1000);
          const bucketTime =
            Math.floor(serverTime / TIMEFRAME_SECONDS) * TIMEFRAME_SECONDS;

          // ------------------------------------------------------
          // 🚀 STEP 1: AWAL MERRA (History Init)
          // ------------------------------------------------------
          // Hna kan tssnaw awwal prix ywsal mn l'backend
          // Bach nlss9o fih l'fake history nichan. Hack n9iya.
          if (historyRef.current.length === 0) {
            const fakeHistory = generateBackwardsHistory(
              bucketTime,
              price,
              TIMEFRAME_SECONDS
            );
            setHistory(fakeHistory);
            historyRef.current = fakeHistory; // Safi mchina
          }

          // ------------------------------------------------------
          // 🕯️ STEP 2: L'CANDLE LI KA TCREEA DABA
          // ------------------------------------------------------
          let candle = activeCandleRef.current;

          // WACH D5LNA F D9I9A JDIDA?
          if (!candle || candle.time !== bucketTime) {
            // Bda candle jdida a sidi
            // Open, High, Low, Close kamlin b7al b7al f l'bdaya
            candle = {
              time: bucketTime,
              open: price,
              high: price,
              low: price,
              close: price,
            };
          } else {
            // UPDATE: Mazal f nfs d9i9a
            // 1. Wach l'prix tla3 kter mn li fat? Update High.
            candle.high = Math.max(candle.high, price);
            // 2. Wach hbat? Update Low.
            candle.low = Math.min(candle.low, price);
            // 3. Close dima hwa a5ir prix wslna lih
            candle.close = price;
          }

          // Save f Ref
          activeCandleRef.current = candle;
          // Trigger Render bach l'UI tban fiha l'7araka
          setCurrentCandle({ ...candle });
        });
      },
      onStompError: (frame) => {
        console.error("❌ STOMP error:", frame);
      },
      onWebSocketError: (event) => {
        console.error("❌ WebSocket error:", event);
      },
      onDisconnect: () => {
        console.warn("⚠️ WebSocket disconnected");
      },
    });

    client.activate();

    // Ila l'component mat, 9te3 l'conx
    return () => {
      try {
        client.deactivate();
      } catch (e) {
        console.warn("Error deactivating client:", e);
      }
    };
  }, [enabled]);

  return { currentCandle, history };
};
