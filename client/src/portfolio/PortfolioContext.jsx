import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
} from "react";
import { useAuth } from "../auth/authContext";
import { CheckCircle, XCircle } from "lucide-react";

const PortfolioContext = createContext(null);

export const PortfolioProvider = ({ children }) => {
  const { user } = useAuth();
  const userKey = user ? `user_${user.id}_` : "guest_";

  const [balance, setBalance] = useState(10000.0);
  const [portfolioItems, setPortfolioItems] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [isSyncing, setIsSyncing] = useState(false);
  const [notification, setNotification] = useState(null);

  // --- SOUND SYSTEM ---
  const playSound = useCallback((type) => {
    const audio = new Audio(type === "success" ? "/success.mp3" : "/error.mp3");
    audio.volume = 0.4; // Set to 40% volume
    audio
      .play()
      .catch((err) => console.log("Audio play blocked by browser policy"));
  }, []);

  // --- DATA LOADING ---
  useEffect(() => {
    if (user) {
      const savedBalance = localStorage.getItem(`${userKey}usd_balance`);
      const savedItems = localStorage.getItem(`${userKey}portfolio_items`);
      const savedHistory = localStorage.getItem(
        `${userKey}transaction_history`
      );

      setBalance(savedBalance ? parseFloat(savedBalance) : 10000.0);
      setPortfolioItems(savedItems ? JSON.parse(savedItems) : []);
      setTransactions(savedHistory ? JSON.parse(savedHistory) : []);
    }
  }, [user, userKey]);

  // --- DATA PERSISTENCE ---
  useEffect(() => {
    if (user) {
      localStorage.setItem(`${userKey}usd_balance`, balance);
      localStorage.setItem(
        `${userKey}portfolio_items`,
        JSON.stringify(portfolioItems)
      );
      localStorage.setItem(
        `${userKey}transaction_history`,
        JSON.stringify(transactions)
      );
    }
  }, [balance, portfolioItems, transactions, userKey, user]);

  const notify = (message, type = "success") => {
    setNotification({ message, type });
    playSound(type); // Play sound whenever a notification is triggered
    setTimeout(() => setNotification(null), 3500);
  };

  // --- MOCK API TRADE LOGIC ---
  const executeTrade = async (type, symbol, usdAmount, currentPrice) => {
    if (isSyncing) return;
    setIsSyncing(true);

    // Simulate database processing time
    await new Promise((resolve) => setTimeout(resolve, 1000));

    const quantity = usdAmount / currentPrice;

    if (type === "BUY") {
      if (usdAmount > balance) {
        notify("Insufficient funds in your USD Wallet", "error");
        setIsSyncing(false);
        return;
      }
      setBalance((prev) => prev - usdAmount);
      setPortfolioItems((prev) => {
        const existing = prev.find((i) => i.symbol === symbol);
        if (existing)
          return prev.map((i) =>
            i.symbol === symbol ? { ...i, quantity: i.quantity + quantity } : i
          );
        return [...prev, { symbol, quantity }];
      });
      notify(`Purchase Confirmed: ${quantity.toFixed(6)} BTC`, "success");
    } else if (type === "SELL") {
      const item = portfolioItems.find((i) => i.symbol === symbol);
      if (!item || item.quantity < quantity) {
        notify(`Insufficient BTC balance to complete sale`, "error");
        setIsSyncing(false);
        return;
      }
      setBalance((prev) => prev + usdAmount);
      setPortfolioItems((prev) =>
        prev.map((i) =>
          i.symbol === symbol ? { ...i, quantity: i.quantity - quantity } : i
        )
      );
      notify(`Sale Confirmed: +$${usdAmount.toLocaleString()}`, "success");
    }

    setTransactions((prev) => [
      {
        symbol,
        type,
        quantity,
        price: currentPrice,
        timestamp: new Date().toISOString(),
      },
      ...prev,
    ]);

    setIsSyncing(false);
  };

  return (
    <PortfolioContext.Provider
      value={{ balance, portfolioItems, transactions, executeTrade, isSyncing }}
    >
      {children}

      {/* FLOATING NOTIFICATION UI */}
      {notification && (
        <div className="fixed top-8 right-8 z-[9999] pointer-events-none">
          <div
            className={`flex items-center gap-4 px-6 py-4 rounded-xl shadow-[0_20px_50px_rgba(0,0,0,0.5)] border transition-all duration-500 animate-in fade-in slide-in-from-right-10 ${
              notification.type === "success"
                ? "bg-emerald-500/20 border-emerald-500/50 text-emerald-400"
                : "bg-rose-500/20 border-rose-500/50 text-rose-400"
            } backdrop-blur-xl`}
          >
            {notification.type === "success" ? (
              <CheckCircle className="animate-bounce" size={24} />
            ) : (
              <XCircle size={24} />
            )}
            <div>
              <p className="text-[10px] uppercase tracking-widest font-black opacity-60">
                System Message
              </p>
              <p className="font-bold text-sm leading-tight">
                {notification.message}
              </p>
            </div>
          </div>
        </div>
      )}
    </PortfolioContext.Provider>
  );
};

export const usePortfolio = () => useContext(PortfolioContext);
